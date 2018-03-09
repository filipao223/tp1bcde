#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <limits.h>
#include <float.h>
#include <time.h>

#define MAX_TEMP 1024

typedef struct Coord{
  int x,y;
  struct Coord* next;
}CNode;

float searchDistance(float, int, CNode*);
float procuraExaustiva(int, CNode*);
float stripList(float, CNode*);
void addToLinkedList(int*, CNode*);
void addToStripLinkedList(CNode*, CNode*);

int main(int argc, char const *argv[]) {
  int num_pontos;
  float minTotal=FLT_MAX;
  char temp[MAX_TEMP];

  /*Recebe o numero de pontos*/
  fgets(temp, MAX_TEMP, stdin);
  num_pontos = atoi(temp);

  CNode* head = malloc(sizeof(struct Coord));
  head->next = NULL;

  /*Recebe as coordenadas*/
  int coord_temp[2];
  while(fgets(temp, MAX_TEMP, stdin) != NULL){
    char* tokens = strtok(temp, " ");
    while(tokens != NULL){
      coord_temp[0] = atoi(tokens); tokens = strtok(NULL, " ");
      coord_temp[1] = atoi(tokens); tokens = strtok(NULL, " ");
      addToLinkedList(coord_temp, head);
    }
  }


  printf("%.3f\n", searchDistance(minTotal,num_pontos, head->next));

  return 0;
}

float searchDistance(float dMin, int num_pontos, CNode* start){
  int mid = num_pontos/2;
  float dLeft, dRight;

  /*Se só houver 2 pontos, calcula a distancia entre eles*/
  if(num_pontos <= 5){
    float dMinTemp2 = procuraExaustiva(num_pontos, start);
    return dMin<dMinTemp2?dMin:dMinTemp2;
  }

  /*Subdivide os pontos em dois conjuntos em num_pontos/2*/
  CNode *current = NULL, *subconj = NULL, *midPoint = NULL;
  int i=1;
  /*Encontra coordenada do meio*/
  for(current=start; current!=NULL && (i <= num_pontos); current = current->next){
    if(i == num_pontos/2){
      subconj = current->next;
      midPoint = current;
      break;
    }
    i++;
  }

  dLeft = searchDistance(dMin, mid, start);
  dRight = searchDistance(dMin, num_pontos-mid, subconj);

  dMin = dLeft>dRight?dRight:dLeft;

  CNode *new_head = malloc(sizeof(struct Coord));
  new_head->next = NULL;

  i=1;
  for(current = start; current!=NULL && (i <= num_pontos); current = current->next){
    if( abs(current->x - midPoint->x) < dMin) addToStripLinkedList(current, new_head);
    i++;
  }

  float dMinTemp = stripList(dMin, new_head);
  return dMinTemp<dMin ? dMinTemp:dMin;
}

float stripList(float min, CNode* head){
  float d;

  CNode *current = NULL, *current2 = NULL, *prox = head;
  for(current = head->next; prox!=NULL; current = prox){
    if (prox == NULL || current == NULL) break;
    prox = current->next;
    if(prox==NULL) break;
    for(current2 = prox; current2!=NULL; current2 = current2->next){
      d = sqrt(pow(current2->x - current->x, 2) + pow(current2->y - current->y, 2));
      if(d < min) min = d;
      else if (d>min) return min;
    }
  }
  return min;
}

float procuraExaustiva(int num_pontos, CNode *start){
  float d=0;
  float menor_dist = FLT_MAX;
  int i=1;

  CNode *current = NULL, *current2 = NULL, *prox = current;
  for(current = start; current!=NULL && (i <= num_pontos); current=prox){
    prox = current->next;
    if(prox == NULL) break;
    int j=num_pontos-i;
    for(current2 = prox; current2!=NULL && (j <= num_pontos); current2 = current2->next){
      d = sqrt(pow(current2->x - current->x, 2) + pow(current2->y - current->y, 2));
      if(d < menor_dist) menor_dist = d;
      j++;
    }
    i++;
  }

  return menor_dist;
}

void addToLinkedList(int coord[], CNode* head){
  CNode *current = NULL, *anterior = head;

  if(head->next == NULL){
    head->next = malloc(sizeof(struct Coord));
    head->next->x = coord[0];
    head->next->y = coord[1];
    head->next->next = NULL;
  }
  else{
    for(current = head->next; current!=NULL; current = current->next){
      if(coord[0] < current->x){
        anterior->next = malloc(sizeof(struct Coord));
        anterior->next->x = coord[0];
        anterior->next->y = coord[1];
        anterior->next->next = current;
        break;
      }
      else if((coord[0] >= current->x) && (current->next == NULL)){
        current->next = malloc(sizeof(struct Coord));
        current->next->x = coord[0];
        current->next->y = coord[1];
        current->next->next = NULL;
        break;
      }

      anterior = current;
    }
  }
}

void addToStripLinkedList(CNode* new, CNode* head){
  CNode *current = NULL, *anterior = head;

  if(head->next == NULL){
    head->next = malloc(sizeof(struct Coord));
    head->next->x = new->x;
    head->next->y = new->y;
    head->next->next = NULL;
  }
  else{
    for(current = head->next; current!=NULL; current = current->next){
      if(new->y < current->y){
        anterior->next = malloc(sizeof(struct Coord));
        anterior->next->x = new->x;
        anterior->next->y = new->y;
        anterior->next->next = current;
        break;
      }
      else if((new->y >= current->y) && (current->next == NULL)){
        current->next = malloc(sizeof(struct Coord));
        current->next->x = new->x;
        current->next->y = new->y;
        current->next->next = NULL;
        break;
      }

     anterior = current;
   }
 }
}
