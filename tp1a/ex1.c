<<<<<<< HEAD
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <limits.h>

#define MAX_TEMP 1024

float searchDistance(int, int*);

int main(int argc, char const *argv[]) {
  int num_pontos;
  char temp[MAX_TEMP];

  /*Recebe o numero de pontos*/
  fgets(temp, MAX_TEMP, stdin);
  num_pontos = atoi(temp);

  int coord[num_pontos*2];

  /*Recebe as coordenadas*/
  int i=0;
  while(fgets(temp, MAX_TEMP, stdin) != NULL){
    char* tokens = strtok(temp, " ");
    while(tokens != NULL){
      coord[i] = atoi(tokens);
      i++;
      tokens = strtok(NULL, " ");
    }
  }

  /*Procura distancias (exaustiva)*/
  printf("%.3f\n", searchDistance(num_pontos, coord));

  return 0;
}

float searchDistance(int num_p, int coord[]){
  int x1, x2, y1, y2, i, j;
  float d=0;
  float menor_dist = INT_MAX;

  for(j=0; j<num_p*2; j+=2){ /*Seleciona ponto 1*/
    x1 = coord[j];
    y1 = coord[j+1];

    for(i=j+2; i<num_p*2; i+=2){ /*Seleciona ponto 2*/
      x2 = coord[i];
      y2 = coord[i+1];

      if(i == j) continue; /*Para nao comparar um ponto com ele mesmo*/
      /*printf("Ponto %d - ponto %d\n",j,i);*/

      d = sqrt(pow(x2-x1, 2) + pow(y2-y1, 2)); /*Distancia entre 2 pontos*/

      if(d < menor_dist) menor_dist = d;
    }
  }
  return menor_dist;
}
=======
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <limits.h>

#define MAX_TEMP 1024

float searchDistance(int, int*);

int main(int argc, char const *argv[]) {
  int num_pontos;
  char temp[MAX_TEMP];

  /*Recebe o numero de pontos*/
  fgets(temp, MAX_TEMP, stdin);
  num_pontos = atoi(temp);

  int coord[num_pontos*2];

  /*Recebe as coordenadas*/
  int i=0;
  while(fgets(temp, MAX_TEMP, stdin) != NULL){
    char* tokens = strtok(temp, " ");
    while(tokens != NULL){
      coord[i] = atoi(tokens);
      i++;
      tokens = strtok(NULL, " ");
    }
  }

  /*Procura distancias (exaustiva)*/
  printf("%.3f\n", searchDistance(num_pontos, coord));

  return 0;
}

float searchDistance(int num_p, int coord[]){
  int x1, x2, y1, y2, i, j;
  float d=0;
  float menor_dist = INT_MAX;

  for(j=0; j<num_p*2; j+=2){ /*Seleciona ponto 1*/
    x1 = coord[j];
    y1 = coord[j+1];

    for(i=j+2; i<num_p*2; i+=2){ /*Seleciona ponto 2*/
      x2 = coord[i];
      y2 = coord[i+1];

      if(i == j) continue; /*Para nao comparar um ponto com ele mesmo*/
      /*printf("Ponto %d - ponto %d\n",j,i);*/

      d = sqrt(pow(x2-x1, 2) + pow(y2-y1, 2)); /*Distancia entre 2 pontos*/

      if(d < menor_dist) menor_dist = d;
    }
  }
  return menor_dist;
}
>>>>>>> e5a6b9723da6813e68aa864d6eaed68bf03da113
