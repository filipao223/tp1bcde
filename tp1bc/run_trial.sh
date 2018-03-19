#!/bin/bash

SELECT=0

if [ "$#" -eq 1 ]; then
  echo "Running $1"
  if [ $1 == "ex1" ]; then
    SELECT=1
  elif [ $1 == "ex2" ]; then
    SELECT=2
  else
    echo "Incorrect argument"
    exit 1
  fi
else
  echo "Missing arg"
  exit 1
fi

COUNTER=1

IN_STR="input0"
OUT_STR="out0"

while [ $COUNTER -ne 8 ]
do
  FILE=$IN_STR$COUNTER".txt"
  OUTPUT=$OUT_STR$COUNTER".txt"
  echo -n "TESTING $FILE"
  ./$1 < testes/$FILE > result.txt
  if cmp -s result.txt $OUTPUT; then
    echo " --------- OK"
  else
    echo " --------- FAIL"
  fi
  ((COUNTER++))
done
echo "Finished"
