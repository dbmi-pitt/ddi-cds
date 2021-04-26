## Setup

pip3 install -r requirements.txt

## Configuration

There are multiple supplemental files that are required to build and update a ValueSet.

* ingredient.csv
    * Contains a comma separated list of the base ingredients that will be searched for through RXNAV

* rxNorm.csv
    * Serves as a cache of ingredients to RXNORM codes

* blacklist.csv
    * Contains a comma separated list of RXNORM codes of excluded forms

* SCD-SBD.json
    * Serves as a cache file of RXNORM codes to the final name of the drug after multiple RXNAV queries and the base
      ingredient

* status.txt
    * Will contains updates as the cronjob runs to list either success or error codes

## Hard Reset

In the scenario where you wish to reset all of the cache files or recreate/update the ValueSet you can add "force" to
the end of "ingredient.csv". The next time the script runs it will detect this option, rebuild the cache and ValueSet,
then remove that option of "ingredient.csv".
