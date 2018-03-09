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

typedef struct toReturn{
  int flag;
  Node* ptr;
}toReturn;

void addBST(Node*, char*, int);
Node* getBST(Node*, char*);
toReturn removeBST(Node*, Node*, char*);
void printBST(Node*);
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
      else if(root->right == NULL && strcmp(root->cartao, cartao)==0){
        Node* newRoot = root->left;
        free(root);
        root = newRoot;
      }
      else{
        Node* oldRoot = root;
        toReturn toFree = removeBST(root, NULL, cartao);

        if(toFree.flag == 0) free(toFree.ptr);
        else if(toFree.flag == -1) printf("%s INEXISTENTE\n", cartao);
        else root = toFree.ptr;
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
        current->left = calloc(1,sizeof(struct Node));
        current->left->left = NULL;
        current->left->right = NULL;
        if(current->left == NULL){
          printf("ERRO MALLOC\n");
          return;
        }
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
        current->right = calloc(1,sizeof(struct Node));
        current->right->left = NULL;
        current->right->right = NULL;
        if(current->right == NULL){
          printf("ERRO MALLOC\n");
          return;
        }
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
        nodeWithNullLeft->left = current->left;
        ret.flag = 1;
        ret.ptr = nodeWithNullLeft;
        free(current);
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
  else if(current->left == NULL && current->right == NULL){ /*Folha*/
    ret.flag = -1;
    return ret;
  }
  else{
    if(valueOf(cartao) < valueOf(current->cartao)) return removeBST(current->left, current, cartao); /*Pede ao da esquerda*/
    else return removeBST(current->right, current, cartao); /*Pede ao da direita*/
  }
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
