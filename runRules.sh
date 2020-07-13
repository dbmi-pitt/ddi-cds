#!/bin/bash
if [ "$1" = "simulated" ]; then
  dates=(2008-02-13 2008-02-14) ## simulated data public schema dates
  mkdir $1-run

  for i in "${dates[@]}"; do
    echo INFO: Running engine on data extracted for $i
    echo INFO: Output for this run will be written to $1-run/output-$i/idia-rules-run-$i.txt
    mkdir $1-run/output-$i
    java -Xmx8192m -jar target/droolstest-1.0.jar $i ${@:2} >$1-run/output-$i/idia-rules-run-$i.txt
    sleep 1
  done

elif [ "$1" = "custom" ]; then
  if [ -z "$2" ]; then
    echo "ERROR: Must enter start date followed by end date (e.g. custom 2020-01-01 2020-02-01)"
    exit 1
  fi
  if [ -z "$3" ]; then
    echo "ERROR: Must enter start date followed by end date (e.g. custom 2020-01-01 2020-02-01)"
    exit 1
  fi

  startdate=$(date -I -d "$2") || echo "ERROR: Must enter start date followed by end date (e.g. custom 2020-01-01 2020-02-01)" exit 1
  enddate=$(date -I -d "$3") || echo "ERROR: Must enter start date followed by end date (e.g. custom 2020-01-01 2020-02-01)" exit 1
  enddate=$(date -I -d "$enddate + 1 day")
  if [[ "$enddate" < "$startdate" ]]; then
    echo "ERROR: Must enter start date followed by end date (e.g. custom 2020-01-01 2020-02-01)"
    exit 1
  fi

  d="$startdate"
  while [ "$d" != "$enddate" ]; do
    echo INFO: Running engine on data extracted for $d
    echo INFO: Output for this run will be written to $1-run/output-$d/idia-rules-run-$d.txt
    mkdir $1-run/output-"$d"
    java -Xmx8192m -jar target/droolstest-1.0.jar "$d" >$1-run/output-"$d"/idia-rules-run-"$d".txt
    sleep 1
    d=$(date -I -d "$d + 1 day")
  done
else
  echo "ERROR: Must input a command line argument for either 'simulated' or 'custom' when running this script"
  exit 1
fi

echo INFO: run completed. Assembling the resulting data and writing it to $1-run/full-$1-run.tsv
grep -r "^DATA" $1-run/output* | sort | cut --complement -f1 >$1-run/full-$1-run.tsv
echo INFO: Assembling the unique resulting data and writing it to $1-run/uniq-$1-run.tsv
grep -r "^DATA" $1-run/output* | sort | uniq | cut --complement -f1 >$1-run/uniq-$1-run.tsv
echo INFO: All done!
