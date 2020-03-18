#!/bin/bash

service postgresql start
RETRIES=5

while ! pg_isready -h 127.0.0.1 -p 5432 > /dev/null 2> /dev/null || [ $RETRIES -eq 0 ]; do
  echo "Waiting for postgres server, $((RETRIES--)) remaining attempts..."
  sleep 10
done


echo "Arguments: $@"
./runRules.sh $@