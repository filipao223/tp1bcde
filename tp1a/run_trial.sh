#!/bin/bash

if [ "$#" -eq 0 ]; then
   echo "No paramaters; using default values (N=1000, C<9, R=1)"
   NUM_PONTOS=1000
   NUM_ITER=9
   NUM_REPEAT=1
elif [ "$#" -ne 3 ]; then
   echo "Illegal number of parameters (N_POINTS N_ITER N_REPEAT)"
   exit 1
else
   NUM_PONTOS=$1
   NUM_ITER=$2
   NUM_REPEAT=$3
fi

COUNTER=0

echo "PROCURA EXAUSTIVA|MELHORADA" > results.txt
<<<<<<< HEAD
echo "DISTANCIA MAIS PEQUENA" >> results.txt
=======
echo "DISTANCIA MINIMA" >> results.txt
>>>>>>> e5a6b9723da6813e68aa864d6eaed68bf03da113
echo "TEMPOS EM SEGUNDOS" >> results.txt
echo "" >> results.txt

while [ $COUNTER -lt $NUM_ITER ]
do
  REPEAT=0
  python3 point_gen.py $NUM_PONTOS
  echo "-----------------------------------" >> results.txt
  echo "NUMERO PONTOS = $NUM_PONTOS" >> results.txt
  while [ $REPEAT -lt $NUM_REPEAT ]
  do
     echo "PROCURA EXAUSTIVA" >> results.txt
     /usr/bin/time -p --append -o results.txt ./ex1 < input.txt | tee -a results.txt
     echo "PROCURA MELHORADA" >> results.txt
     /usr/bin/time -p --append -o results.txt ./ex2 < input.txt | tee -a results.txt
     ((REPEAT++))
  done
  ((COUNTER++))
  ((NUM_PONTOS+=1000))
done
echo "Finished"
