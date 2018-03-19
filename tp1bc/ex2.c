<<<<<<< HEAD
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_STR 16
#define MAX_TEMP 1024

typedef struct Node{
  int saldo;
  char cartao[MAX_STR];
  struct Node *left, *right;
}Node;

void addBST(Node*, char*, int);
Node* getBST(Node*, char*);
Node* removeBST(Node*, Node*, char*);
void printBST(Node*);
int heightBST(Node*);
long long valueOf(char*);

int main(int argc, char** argv){
  char temp[MAX_TEMP];
  Node *root = NULL;
  char cartao[MAX_STR];
  int newValue;

  /*Recebe os comandos*/
  while(fgets(temp, MAX_TEMP, stdin) != NULL){
    temp[strlen(temp)-1] = '\0';
    /*Verifica se termina*/
    if(strcmp("TERMINA", temp)==0) return 0;

    /*Traduz*/
    char* tokens = strtok(temp, " ");

    /*Se for para atualizar ("UPDATE")*/
    if(strcmp("UPDATE", tokens)==0){
      tokens = strtok(NULL, " "); strcpy(cartao, tokens);
      tokens = strtok(NULL, " ");
      newValue = atoi(tokens);
      if(root == NULL){
        root = malloc(sizeof(struct Node)); root->left = NULL; root->right = NULL;
        strcpy(root->cartao, cartao);
        root->saldo = newValue;
      }
      else{
        addBST(root, cartao, newValue);
      }
    }

    /*Se for para verificar saldo*/
    else if(strcmp("SALDO", tokens)==0){
      tokens = strtok(NULL, " "); strcpy(cartao, tokens);
      if(root == NULL) printf("%s INEXISTENTE\n", cartao);
      else{
        Node *card = getBST(root, cartao);
        if(card == NULL) printf("%s INEXISTENTE\n", cartao);
        else{
          printf("%s SALDO %d\n", cartao, card->saldo);
        }
      }
    }

    /*Se for para remover*/
    else if(strcmp("REMOVE", tokens)==0){
      tokens = strtok(NULL, " "); strcpy(cartao, tokens);
      if(root == NULL) printf("%s INEXISTENTE\n", cartao);
      else if(root->right == NULL){
        Node* newRoot = root->left;
        free(root);
        root = newRoot;
      }
      else{
        Node* oldRoot = root;
        Node* toFree = removeBST(root, NULL, cartao);

        if(toFree != oldRoot){
          free(toFree);
          toFree = NULL;
        }
        else{
          root = toFree;
        }
      }
    }

    /*Verifica se esta equilibrada*/
    else if(strcmp("BALANCE", tokens)==0){
      if(root == NULL) printf("EQUILIBRADA\n");
      else{
        int balance = heightBST(root);
        if(balance == -1) printf("NAO EQUILIBRADA\n");
        else printf("EQUILIBRADA");
      }
    }
    else{
      printBST(root);
    }
  }
  return 1;
}

void addBST(Node* current, char cartao[], int valor){
  if(strcmp(current->cartao, cartao)==0){
    current->saldo += valor;
    return;
  }

  else{ /*Não está no node atual*/
    if(valueOf(cartao) < valueOf(current->cartao)){
      /*Node da esquerda*/
      if(current->left == NULL){ /*Ainda nao existe cartao*/
        current->left = malloc(sizeof(struct Node)); current->left->left = NULL; current->left->right = NULL;
        strcpy(current->left->cartao, cartao);
        current->left->saldo = valor;
      }
      else{ /*Pede ao node da esquerda para procurar*/
        addBST(current->left, cartao, valor);
      }
    }
    else{
      /*Node da direita*/
      if(current->right == NULL){ /*Ainda nao existe cartao*/
        current->right = malloc(sizeof(struct Node)); current->right->left = NULL; current->right->right = NULL;
        strcpy(current->right->cartao, cartao);
        current->right->saldo = valor;
      }
      else{ /*Pede ao node da direita para procurar*/
        addBST(current->right, cartao, valor);
      }
    }
  }
}

Node* getBST(Node* current, char cartao[]){
  if (current == NULL) return NULL;
  if(strcmp(current->cartao, cartao) == 0) return current;
  else if(current->left== NULL && current->right== NULL) return NULL;
  else{
    if(valueOf(cartao) < valueOf(current->cartao)) return getBST(current->left, cartao);
    else return getBST(current->right, cartao);
  }
}

Node* removeBST(Node* current, Node* previous, char cartao[]){
  if(strcmp(current->cartao, cartao)==0){
    /*Se é uma folha nao precisa de fazer nada, devolve para ser libertado*/
    if(current->left == NULL && current->right == NULL) return current;
    else if(current->right == NULL){
      previous->left = current->left;
      return current;
    }
    else{
      /*Pesquisa a partir de current->right o primeiro node da esquerda que seja NULL*/
      Node* nodeWithNullLeft = current->right;
      while(nodeWithNullLeft->left!=NULL){
        nodeWithNullLeft = nodeWithNullLeft->left;
      }

      /*Verifica se pretendemos apagar a raiz*/
      if(previous == NULL){
        current->right->left = nodeWithNullLeft->right;
        nodeWithNullLeft->right = current->right;
        nodeWithNullLeft->left = current->left;
        Node* oldRoot = current;
        current = nodeWithNullLeft;
        return oldRoot;
      }

      /*Que node de previous queremos apagar?*/
      if(previous->left == current) previous->left = nodeWithNullLeft;
      else previous->right = nodeWithNullLeft;
      nodeWithNullLeft->right = current->right;
      current->right->left = nodeWithNullLeft->right;

      return current;
    }
  }
  else if(current->left == NULL && current->right == NULL) return NULL;
  else{
    if(valueOf(cartao) < valueOf(current->cartao)) return removeBST(current->left, current, cartao);
    else return removeBST(current->right, current, cartao);
  }
}

void printBST(Node* current){
  if(current==NULL) return;

  printBST(current->left);
  printf("%s SALDO %d\n", current->cartao, current->saldo);
  printBST(current->right);
}

int heightBST(Node *current){
  if(current==NULL) return 0;

  int hLeft = heightBST(current->left);
  int hRight = heightBST(current->right);

  return hLeft!=hRight?-1:0;
}

long long valueOf(char string[]){
  return strtoll(string+2, NULL, 10);
}
=======
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <errno.h>
#include <math.h>
#include <time.h>

#define clear() printf("\033[H\033[J")
#define gotoxy(x,y) printf("\033[%d;%dH", (x), (y))

#define MAX_STR 16
#define MAX_TEMP 1024

typedef struct Node{
  int saldo, weightLeft, weightRight;
  char cartao[MAX_STR];
  struct Node *left, *right;
}Node;

typedef struct ReturnStruct{
  int weight;
  Node* ptr;
}ReturnStruct;

ReturnStruct addBST(Node*, Node*, char*, int); /*Devolve tamanho atual da arvore (ou subarvore)*/
Node* getBST(Node*, char*);
Node* removeBST(Node*, char*);
void printBST(Node*);
Node* balanceBST(Node*);
void printTree(Node*, int);
long long int valueOf(char*);

int main(int argc, char** argv){
  char temp[MAX_TEMP];
  Node *root = NULL;
  char cartao[MAX_STR];
  int newValue;

  /*Recebe os comandos*/
  while(fgets(temp, MAX_TEMP, stdin) != NULL){
    temp[strlen(temp)-1] = '\0';
    /*Verifica se termina*/
    if(strcmp("TERMINA", temp)==0) return 0;

    /*Traduz*/
    char* tokens = strtok(temp, " ");

    /*Se for para atualizar ("UPDATE")*/
    if(strcmp("UPDATE", tokens)==0){
      tokens = strtok(NULL, " "); strcpy(cartao, tokens);
      tokens = strtok(NULL, " ");
      newValue = atoi(tokens);
      if(root == NULL){
        root = malloc(sizeof(struct Node)); root->left = NULL; root->right = NULL; root->weightLeft = 0; root->weightRight=0;
        strcpy(root->cartao, cartao);
        root->saldo = newValue;
      }
      else{
        ReturnStruct returnStruct = addBST(root, NULL, cartao, newValue);
        if(returnStruct.ptr != NULL){ /*Se equilibrou o root*/
          root = returnStruct.ptr;
        }
      }
    }

    /*Se for para verificar saldo*/
    else if(strcmp("SALDO", tokens)==0){
      tokens = strtok(NULL, " "); strcpy(cartao, tokens);
      if(root == NULL) printf("%s INEXISTENTE\n", cartao);
      else{
        Node *card = getBST(root, cartao);
        if(card == NULL) printf("%s INEXISTENTE\n", cartao);
        else{
          printf("%s SALDO %d\n", cartao, card->saldo);
        }
      }
    }

    /*Se for para remover*/
    else if(strcmp("REMOVE", tokens)==0){
      tokens = strtok(NULL, " "); strcpy(cartao, tokens);
      if(root == NULL) printf("%s INEXISTENTE\n", cartao);

      root = removeBST(root, cartao);
    }
    else{
      printBST(root);
      printTree(root,0);
      printf("\n\n");
    }
  }
  return 1;
}

ReturnStruct addBST(Node* current, Node* previous, char cartao[], int valor){
  ReturnStruct returnStruct;
  returnStruct.ptr = NULL;
  if(strcmp(current->cartao, cartao)==0){
    current->saldo += valor;

    returnStruct.weight = -1;
    return returnStruct;
  }

  else{ /*Não está no node atual*/
    if(valueOf(cartao) < valueOf(current->cartao)){
      /*Node da esquerda*/
      if(current->left == NULL){ /*Ainda nao existe cartao, inicia os valores*/
        current->left = malloc(sizeof(struct Node)); current->left->left = NULL; current->left->right = NULL;
        current->left->weightLeft = 0; current->left->weightRight=0;

        current->weightLeft +=1; /*Criou novo node, atualiza o peso correspondente*/
        strcpy(current->left->cartao, cartao);
        current->left->saldo = valor;
        printf("ADDED %s\n", cartao);

        returnStruct.weight = 1;

        return returnStruct;
      }
      else{ /*Pede ao node da esquerda para procurar*/
        returnStruct = addBST(current->left, current, cartao, valor); int leftSubTreeWeight = returnStruct.weight;
        /*Verifica se equlibrou a arvore*/
        if(returnStruct.ptr != NULL){
          current->left = returnStruct.ptr; /*Equlibrou, atualiza o child node*/
          returnStruct.ptr = NULL;
        }

        if (leftSubTreeWeight != -1) current->weightLeft = leftSubTreeWeight + 1; /*Foi criado um novo node, aumenta o peso respectivo*/

        if(current->weightLeft - current->weightRight > 1){
          printf("TREE UNBALANCED LEFT on %s with WL -> %d and WR -> %d\n", current->cartao, current->weightLeft, current->weightRight); /*balanceBST(root)*/
          current = balanceBST(current);
          current->weightRight += 1;
          printf("BALANCED\n");
          returnStruct.ptr = current;
        }

        returnStruct.weight = current->weightRight > current->weightLeft ? current->weightRight : current->weightLeft; /*Devolve o maior caminho*/
        return returnStruct;
      }
    }
    else{
      /*Node da direita*/
      if(current->right == NULL){ /*Ainda nao existe cartao, inicia os valores*/
        current->right = malloc(sizeof(struct Node)); current->right->left = NULL; current->right->right = NULL;
        current->right->weightLeft = 0; current->right->weightRight=0;

        current->weightRight +=1; /*Criou novo node, atualiza o peso correspondente*/
        strcpy(current->right->cartao, cartao);
        current->right->saldo = valor;
        printf("ADDED %s\n", cartao);

        returnStruct.weight = 1;

        return returnStruct;
      }
      else{ /*Pede ao node da direita para procurar*/
        returnStruct = addBST(current->right, current, cartao, valor); int rightSubTreeWeight = returnStruct.weight;
        /*Verifica se equilibrou a arvore*/
        if(returnStruct.ptr != NULL){
          current->right = returnStruct.ptr; /*Equlibrou, atualiza o child node*/
          returnStruct.ptr = NULL;
        }

        if(rightSubTreeWeight != -1) current->weightRight = rightSubTreeWeight + 1; /*Foi criado um novo node, aumenta o peso respectivo*/

        if(current->weightRight - current->weightLeft > 1){
          printf("TREE UNBALANCED RIGHT on %s with WL -> %d and WR -> %d\n", current->cartao, current->weightLeft, current->weightRight);
          current = balanceBST(current);
          current->weightLeft += 1;
          printf("BALANCED\n");
          returnStruct.ptr = current;
        }

        returnStruct.weight = current->weightRight > current->weightLeft ? current->weightRight : current->weightLeft; /*Devolve o maior caminho*/
        return returnStruct;
      }
    }
  }
}

Node* getBST(Node* current, char cartao[]){
  if (current == NULL) return NULL;
  if(strcmp(current->cartao, cartao) == 0) return current;
  else if(current->left== NULL && current->right== NULL) return NULL;
  else{
    if(valueOf(cartao) < valueOf(current->cartao)) return getBST(current->left, cartao);
    else return getBST(current->right, cartao);
  }
}

Node* removeBST(Node* current, char cartao[]){
  if(current == NULL) return current;

  if(valueOf(cartao) < valueOf(current->cartao)) current->left =  removeBST(current->left, cartao);
  else if(valueOf(cartao) > valueOf(current->cartao)) current->right =  removeBST(current->right, cartao);
  else{
    /*Verifica se sao folhas ou so têm um node*/
    if (current->left == NULL){
      Node *temp = current->right;
      free(current);
      return temp;
    }
    else if (current->right == NULL){
      Node *temp = current->left;
      free(current);
      return temp;
    }

    Node* temp = current;
    while(temp->left != NULL){ /*Procura o elemento mais a esquerda*/
      temp = temp->left;
    }

    strcpy(current->cartao, temp->cartao);
    current->right = removeBST(current->right, temp->cartao);
  }

  return current;
}

void printBST(Node* current){
  if(current==NULL) return;

  printBST(current->left);
  printf("%s SALDO %d\n", current->cartao, current->saldo);
  printBST(current->right);
}

Node* balanceBST(Node* root){
  /*Descobrir se o childnode tem algum child node e se é esquerda ou direita*/
  /*Verifica se o root node esta desequilibrado*/
  if(root->weightLeft - root->weightRight > 1){
    if(root->left->weightRight - root->left->weightLeft >= 1){
      /*Rotaçao dupla para a direita*/
      Node* temp = root->left->right;
      temp->left = root->left; root->left->right = NULL;
      root->left = temp;

      temp->right = root;
      root->left = NULL;
      root->right = NULL;
      root = temp;
    }
    else{
      /*Rotaçao para a direita*/
      Node* temp = root->left;
      temp->right = root;
      root->left = NULL;
      root->right = NULL;
      root = temp;
    }
  }
  else if(root->weightRight - root->weightLeft > 1){
    if(root->right->weightLeft - root->right->weightRight >= 1){
      /*Rotaçao dupla para a esquerda*/
      Node *temp = root->right->left;
      temp->right = root->right; root->right->left = NULL;
      root->right = temp;

      temp = root->right;
      temp->left = root;
      root->left = NULL;
      root->right = NULL;
      root = temp;
    }
    else{
      /*Rotaçao para a esquerda*/
      Node* temp = root->right;
      temp->left = root;
      root->left = NULL;
      root->right = NULL;
      root = temp;
    }
  }

  /*Change child nodes weights to 0*/
  root->left->weightLeft = 0; root->left->weightRight =0;
  root->right->weightLeft = 0; root->right->weightRight =0;

  return root;
}

void printTree(Node* root, int height){
  if(root==NULL) return;

  printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

  gotoxy(1,55);
  printf("%s %d", root->cartao, root->saldo);
  gotoxy(2,55); printf("/"); gotoxy(3,54); printf("/");
  gotoxy(2, 55+strlen(root->cartao)+3); printf("\\"); gotoxy(3, 55+strlen(root->cartao)+4); printf("\\");

  if(root->left != NULL){
    int startX=4, startY = 54-strlen(root->left->cartao)-3;
    gotoxy(startX,startY);
    printf("%s %d", root->left->cartao, root->left->saldo);
    gotoxy(startX+1,startY); printf("/"); gotoxy(startX+2, startY-1); printf("/");
    gotoxy(startX+1, startY+strlen(root->left->cartao) + 3); printf("\\"); gotoxy(startX+2, startY+strlen(root->left->cartao)+4); printf("\\");
  }
  if(root->right != NULL){
    int startX=4, startY = 54+strlen(root->left->cartao + 4)+10;
    gotoxy(startX,startY);
    printf("%s %d", root->right->cartao, root->right->saldo);
    gotoxy(startX+1,startY); printf("/"); gotoxy(startX+2, startY-1); printf("/");
    gotoxy(startX+1, startY+strlen(root->right->cartao) + 3); printf("\\"); gotoxy(startX+2, startY+strlen(root->right->cartao)+4); printf("\\");
  }
  if(root->left->left != NULL){
    int startX=7, startY = 54-strlen(root->left->left->cartao + 4)-10-strlen(root->left->cartao);
    gotoxy(startX,startY);
    printf("%s %d", root->left->left->cartao, root->left->left->saldo);
    gotoxy(startX+1,startY); printf("/"); gotoxy(startX+2, startY-1); printf("/");
    gotoxy(startX+1, startY+strlen(root->left->left->cartao) + 3); printf("\\"); gotoxy(startX+2, startY+strlen(root->left->left->cartao)+4); printf("\\");
  }
}

long long valueOf(char string[]){
  return strtoll(string+2, NULL, 10);
}
>>>>>>> e5a6b9723da6813e68aa864d6eaed68bf03da113
