#!/usr/bin/env python3

from datetime import date, datetime
from requests.adapters import HTTPAdapter
from requests.packages.urllib3.util.retry import Retry
import requests
import json
import csv
FILE_PATH = "/Users/maxsibilla/Documents/Dev/ddi-cds/valueset/CYP1A2/"

# FILE_PATH = "FILEPATH_TO_DIRECTORY"

# A user can put "force" at the end of "ingredient.csv" to recreate all cache files and the ValueSet
force = False
base_url = "https://rxnav.nlm.nih.gov/REST/"

session = requests.Session()
retry = Retry(connect=3, backoff_factor=0.5)
adapter = HTTPAdapter(max_retries=retry)
session.mount('https://', adapter)

today = date.today()
now = datetime.now()
dt_string = now.strftime("%d/%m/%Y %H:%M:%S")

all_drug_names = []
with open(FILE_PATH + 'ingredient.csv', newline='') as f:
    reader = csv.reader(f)
    for row in reader:  # each row is a list
        all_drug_names.append(row[0])
if 'force' in all_drug_names:
    force = True
    all_drug_names.remove('force')

excluded_cuis = []
with open(FILE_PATH + 'blacklist.csv', newline='') as f:
    reader = csv.reader(f)
    for row in reader:  # each row is a list
        excluded_cuis.append(row[0])

drug_cuis = {}
with open(FILE_PATH + 'rxNorm.csv') as f:
    drug_cuis = dict(filter(None, csv.reader(f)))

scd_json = json.load(open(FILE_PATH + "SCD-SBD.json"))

# Check what drugs are not cached
drug_names = []
newlist = list(drug_cuis.keys())
for drug in all_drug_names:
    if drug not in newlist:
        drug_names.append(drug)

# We will forcibly recreate all cache files and rebuild the ValueSet
if force is True:
    drug_names = all_drug_names

if len(drug_names) > 0:
    for name in drug_names:
        try:
            rx_cui_search_url = base_url + "rxcui.json?name=" + name + "&search=0"

            response = session.get(rx_cui_search_url)

            if response.status_code == 200:
                response_data = json.loads(response.content)

                try:
                    drug_cuis[name] = response_data['idGroup']['rxnormId'][0]
                except KeyError:
                    drug_cuis[name] = "N/A"
            else:
                drug_cuis[name] = "N/A"
                response.raise_for_status()
        except Exception as e:
            error_string = repr(e)
            with open(FILE_PATH + "status.txt", 'a') as f:
                f.write(dt_string + ": Failed to update ValueSet: " + error_string + "\n")

    # Save dict to csv
    with open(FILE_PATH + 'rxNorm.csv', 'w') as f:
        for key in drug_cuis.keys():
            f.write("%s,%s\n" % (key, drug_cuis[key]))

    # Iterate over ingredient cui dict and perform getRelatedByType query
    for key in drug_cuis:
        try:
            rx_related_type_url = base_url + "rxcui/" + drug_cuis[key] + "/related.json?tty=SCD+SBD"

            response = session.get(rx_related_type_url)

            if response.status_code == 200:
                # Extract the concept group with TTY=SCDC
                response_data = json.loads(response.content)
                concept_groups = response_data['relatedGroup']['conceptGroup']
                for group in concept_groups:
                    if 'conceptProperties' in group:
                        for property in group['conceptProperties']:
                            scd_json[property['rxcui']] = {"name": property['name'], "ingredient": key}
        except Exception as e:
            error_string = repr(e)
            with open(FILE_PATH + "status.txt", 'a') as f:
                f.write(dt_string + ": Failed to update ValueSet: " + error_string + "\n")

    # Perform getRelatedByRelationshipQuery on new cuis
    black_list_cuis = []
    for key in scd_json:
        try:
            rx_related_relationship_url = base_url + "rxcui/" + key + "/related.json?rela=has_dose_form+has_form"
            response = session.get(rx_related_relationship_url)

            if response.status_code == 200:
                response_data = json.loads(response.content)
                concept_groups = response_data['relatedGroup']['conceptGroup']
                for group in concept_groups:
                    if 'conceptProperties' in group:
                        for property in group['conceptProperties']:
                            if property['rxcui'] in excluded_cuis:
                                black_list_cuis.append(key)
        except Exception as e:
            error_string = repr(e)
            with open(FILE_PATH + "status.txt", 'a') as f:
                f.write(dt_string + ": Failed to update ValueSet: " + error_string + "\n")

    for black_list in black_list_cuis:
        scd_json.pop(black_list, None)

    # Save ingredient dictionary
    with open(FILE_PATH + "SCD-SBD.json", "w") as f:
        json.dump(scd_json, f, indent=4, sort_keys=True)

# Create ValueSet based on dictionary
valueset = {}
valueset["resourceType"] = "ValueSet"
valueset["id"] = "valueset-cyp1a2"
valueset["version"] = "US 1.1"
valueset["name"] = "valueset-cyp1a2"
valueset["title"] = "CYP1A2 Inhibitor Value Set"
valueset["url"] = "http://localhost:8080/cqf-ruler-r4/fhir/ValueSet/valueset-cyp1a2"
valueset["status"] = "draft"
valueset["date"] = today.strftime("%Y-%m-%d")
valueset["publisher"] = "University of Pittsburgh Department of Biomedical Informatics"
valueset["contact"] = [{"name": "Richard Boyce rdb20@pitt.edu"}]
valueset[
    "description"] = "RxNorm semantic clinical drug and semantic branded drug concepts for drugs that inhibit Cytochrome P450 (CYP) 1A2."
valueset["jurisdiction"] = [{
    "coding": [{
        "system": "urn:iso:std:iso:3166",
        "code": "US",
        "display": "United States of America"
    }]
}]
valueset["purpose"] = "Provide terminology for PDDI CDS (CYP1A2 Inhibitors) FHIR resources"
valueset["copyright"] = "Attribution CC BY"
valueset["compose"] = {
    "include": [{
        "system": "http://www.nlm.nih.gov/research/umls/rxnorm",
        "version": today.strftime("%Y-%m-%d"),
        "concept": []
    }]
}

for key in scd_json:
    print(key)
    concept = {}
    concept["code"] = key
    concept["display"] = scd_json[key]["name"]
    valueset["compose"]["include"][0]["concept"].append(concept)

json_object = json.dumps(valueset)
print(json_object)

# PUT ValueSet to CQF-Ruler
# try:
#     headers = {'Content-type': 'application/fhir+json;charset=utf-8'}
#     response = requests.put("https://cds.ddi-cds.org/cqf-ruler-r4/fhir/ValueSet/valueset-cyp1a2", data=json_object,
#                             headers=headers)
#     if response.status_code == 200:
#         with open(FILE_PATH + "status.txt", 'a') as f:
#             f.write(dt_string + ": ValueSet was successfully updated.\n")
#
#         # Check if this was a forced recreation, if so then at this point everything was done
#         # correctly so remove that from ingredient.csv
#         if force:
#             with open(FILE_PATH + "ingredient.csv", "w") as outfile:
#                 outfile.write(",\n".join(drug_names))
#     else:
#         response_data = json.loads(response.content)
#         with open(FILE_PATH + "status.txt", 'a') as f:
#             f.write(dt_string + ": Could not successfully PUT ValueSet: " + response_data + "\n")
# except Exception as e:
#     error_string = repr(e)
#     with open(FILE_PATH + "status.txt", 'a') as f:
#         f.write(dt_string + ": Failed to update ValueSet: " + error_string + "\n")
