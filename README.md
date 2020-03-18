This is a sample application to demonstrates using Drools rules
against the OHDSI data structure for the AHRQ funded Individualized
Drug Interaction Alerts (IDIA) project (https://goo.gl/t4eqGw). 

## Setup

You must change the following in the `config.properties` file.

```
username=<USERNAME>
password=<PASSWORD>
connectionURL=jdbc:postgresql://<IP>:<PORT>/<DATABASE>
schema=<schema to query e.g., 'public'>
```

Replace the variables in <> with your values.

Additionally, there are two folders that can be fired, `src/main/resources/rules-complete/` which includes all rules, and `src/main/resources/rules-progress/` which is an empty folder that select rules can be placed into for firing.
To switch between these two folders, you must edit the `config.properties` file in the root of this repository. If you want all rules to fire, set rule_folder to `ksession-rules`. If you want just the rules you placed into the progress folder to fire, set rule_folder to `ksession-progress`.

__BUILD__

Note this requires Java >= 8.

There is a maven __pom.xml__ file used to build the application. Before running that, execute the following statements:

```
mvn install:install-file -Dfile=./lib/opencsv-2.3.jar -DgroupId=org.opencsv -DartifactId=opencsv -Dversion=2.3 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/hibernate-core-3.3.0.SP1.jar -DgroupId=hibernate -DartifactId=hibernate -Dversion=3.3.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/hibernate-annotations-3.4.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-annotations -Dversion=3.4.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/hibernate-commons-annotations-3.1.0.GA.jar -DgroupId=hibernate -DartifactId=hibernate-commons-annotations -Dversion=3.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/ehcache-core-2.1.0.jar -DgroupId=ehcache -DartifactId=ehcache-core -Dversion=2.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/antlr.jar -DgroupId=antlr -DartifactId=antlr -Dversion=unknown -Dpackaging=jar
```

Then you should be able to run:
```mvn install```

__RUN VIA JAVA__

```java -Xmx1536 -jar target/droolstest-1.0.jar```

__RUN VIA BASH__

Run the bash script "runRules.sh" and pass in the argument "simulated"

```
bash runRules.sh simulated
```

Output is then written to a folder "simulated-run". This folder location should be at the base of the project.

__OUTPUT FILES__

Within simulated-run, two .csv files summarize the output in "full-banner-run.csv" and "uniq-banner-run.csv". The latter uses the unix "uniq" command to remove duplicate rows, while "full-banner-run.csv" keeps these duplicates. Both of these files should include the following columns:
* date = The date the rule fired on
* person = Unique identifier for the relevant patient
* rule set = The name of the algorithm that fired
* rule = The specific branch within the algorithm
* leaf node alert = TRUE or FALSE for if this branch is a leaf node that triggers a "red" or "yellow" alert.
* classification = "basic concomitant", blank, or "green" indicate that this is branch does not yield an interruptive alert. "basic concomitant" or blank rows typically help contextualize other subsequent rows. "yellow" and "red" are indicative of an interruptive alert.
* gender = Gender concept ID
* age = Age in years
* The following rows are repeated twice for each drug in the drug pair (X = 1 or 2)
  * drugX = Drug concept ID
  * drugXname = The concept name corresponding with the drug concept ID
  * drugXingr = The primary relevant ingredient for drugX
  * drugXdailydose = The calculated daily dosage that this patient is taking for this specific drug ingredient
  * drugXstart = The start date/time for this drug
  * drugXend = The end date/time for this drug

Within the outputted subfolders, you can examine detailed logs in which additional information such as how many persons, visits, drug exposures, measurements, conditions, etc. were loaded, daily dosage calculations, and more.

## Docker Container

A [Docker container](https://hub.docker.com/r/ddicds/idia_rules) for this project can be pulled using the command:
```docker pull ddicds/idia_rules:localdb``` 

The following command can be used to run the docker container over the default synthetic population:

```docker run -v ~/simulated-run/:/app/simulated-run -it --rm ddicds/idia_rules:localdb simulated```

The "-v" flag is used to mount the running docker container and will create a directory called "simulated-run" in the user's home folder that the docker container will write both the output for each day of the run and the aggregated results.

To run the rules over a custom database connection and/or specify a particular rule  to isolate in the run, the following additional arguments can be added to the above command:

```connectionURL={URL} ruleFolder={rule options listed below} schema={schema} user={user} password={password} sslmode=require ```

The sslmode argument is optional and its presence is dependent on the specific configuration of the database that the user wishes to connect to. 

The ruleFolder argument is useful to specify the run by focusing on a particular rule. This argument can be used independently as such:

```docker run -v ~/simulated-run/:/app/simulated-run -it --rm ddicds/idia_rules:localdb simulated ruleFolder=rules_warfarin_antidepressants```

ruleFolder argument options:
* rules_acei_arb_ksparing_diuretics
* rules_ceftriaxone_calcium
* rules_citalopram_QT_agents
* rules_clonidine_betablockers
* rules_epi_betablockers
* rules_fluconazole_opioids
* rules_immunosuppressants_fluconazole
* rules_k_ksparing_diuretics
* rules_warfarin_antidepressants
* rules_warfarin_nsaids
* rules_warfarin_salicylates

All rules are run by default if no argument is specified.

This container was tested using Docker version 19.03.5 on Mac OS and Ubuntu Linux. 
For additional questions and discussions please visit: https://forums.dikb.org/t/docker-container-for-the-individualized-drug-interaction-alerts-idia-project/228
