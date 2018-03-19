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

<<<<<<< HEAD
typedef struct toReturn{
  int flag;
  Node* ptr;
}toReturn;

void addBST(Node*, char*, int);
Node* getBST(Node*, char*);
toReturn removeBST(Node*, Node*, char*);
void printBST(Node*);
=======
void addBST(Node*, char*, int);
Node* getBST(Node*, char*);
Node* removeBST(Node*, char*);
void printBST(Node*);
void printTree(Node*, int);
>>>>>>> e5a6b9723da6813e68aa864d6eaed68bf03da113
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
<<<<<<< HEAD
      else if(root->right == NULL && strcmp(root->cartao, cartao)==0){
        Node* newRoot = root->left;
        free(root);
        root = newRoot;
      }
      else{
        Node* oldRoot = root;
        toReturn toFree = removeBST(root, NULL, cartao);

        if(toFree.flag == 0) free(toFree.ptr);
        else root = toFree.ptr;
      }
=======

      root = removeBST(root, cartao);
>>>>>>> e5a6b9723da6813e68aa864d6eaed68bf03da113
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

<<<<<<< HEAD
toReturn removeBST(Node* current, Node* previous, char cartao[]){
  toReturn ret;
  if(strcmp(current->cartao, cartao)==0){
    /*Se é uma folha nao precisa de fazer nada, devolve para ser libertado*/
    if(current->left == NULL && current->right == NULL){
      ret.flag = 0;
      ret.ptr= current;
      return ret;
    }
    else if(current->right == NULL && previous!=NULL){
      previous->left = current->left;
      ret.flag = 0;
      ret.ptr = current;
      return ret;
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
        nodeWithNullLeft->right = nodeWithNullLeft!=current->right?current->right:nodeWithNullLeft->right;
        nodeWithNullLeft->right = current->right;
        nodeWithNullLeft->left = current->left;
        ret.flag = 1;
        ret.ptr = nodeWithNullLeft;
        return ret;
      }

      /*Que node de previous queremos apagar?*/
      if(previous->left == current) previous->left = nodeWithNullLeft;
      else previous->right = nodeWithNullLeft;
      nodeWithNullLeft->right = current->right;
      current->right->left = nodeWithNullLeft->right;

      ret.flag=0;
      ret.ptr = current;
      return ret;
    }
  }
  else if(current->left == NULL && current->right == NULL){
    ret.flag = -1;
    return ret;
  }
  else{
    if(valueOf(cartao) < valueOf(current->cartao)) return removeBST(current->left, current, cartao);
    else return removeBST(current->right, current, cartao);
  }
=======
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
>>>>>>> e5a6b9723da6813e68aa864d6eaed68bf03da113
}

void printBST(Node* current){
  if(current==NULL) return;

  printBST(current->left);
  printf("%s SALDO %d\n", current->cartao, current->saldo);
  printBST(current->right);
}

long long valueOf(char string[]){
  return strtoll(string+2, NULL, 10);
}
