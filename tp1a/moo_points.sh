#!/bin/bash

echo "RESULTADOS MOOSHAK" > res_moo.txt

COUNTER=1
TOTAL=10
TXT_STR=".txt"
INPUT="input0"
INPUT_10="input"
OUTPUT="output0"
OUTPUT_10="output"
INPUT_FILE=""
OUTPUT_FILE=""
RESULT=""

while [ $COUNTER -le $TOTAL ]
do
   if [ "$COUNTER" -ne 10 ]; then
       INPUT_FILE="$INPUT$COUNTER$TXT_STR"
       OUTPUT_FILE="$OUTPUT$COUNTER$TXT_STR"
   else
       INPUT_FILE="$INPUT_10$COUNTER$TXT_STR"
       OUTPUT_FILE="$OUTPUT_10$COUNTER$TXT_STR"
   fi

   RESULT="$(/usr/bin/time -p --append -o res_moo.txt ./ex2 < $INPUT_FILE |tee -a res_moo.txt)"
   echo -n $RESULT
   TEMP="$(cat $OUTPUT_FILE)"
   echo -n " ----- $TEMP"

   if [ "$RESULT" = "$TEMP" ]; then
       echo -n "  OK"
   else
       echo -n "  FAIL"
   fi

   echo ""
   INPUT_FILE=""
   ((COUNTER++))
done
