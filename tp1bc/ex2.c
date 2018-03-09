#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <errno.h>
#include <math.h>
#include <time.h>

#define MAX_STR 16
#define MAX_TEMP 1024

typedef struct Node{
  int saldo, weightLeft, weightRight;
  char cartao[MAX_STR];
  struct Node *left, *right;
}Node;

int addBST(Node*, Node*, char*, int); /*Devolve tamanho atual da arvore (ou subarvore)*/
Node* getBST(Node*, char*);
Node* removeBST(Node*, char*);
void printBST(Node*);
Node* balanceBST(Node*);
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
        addBST(root, NULL, cartao, newValue);
      }

      /*Verifica se o root node esta desequilibrado*/
      if(root->weightLeft - root->weightRight > 1){
        /*Rotaçao para a direita*/
      }
      else if(root->weightRight - root->weightLeft > 1){
        /*Rotaçao para a esquerda*/
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
    }
  }
  return 1;
}

int addBST(Node* current, Node* previous, char cartao[], int valor){
  if(strcmp(current->cartao, cartao)==0){
    current->saldo += valor;
    return -1;
  }

  else{ /*Não está no node atual*/
    if(valueOf(cartao) < valueOf(current->cartao)){
      /*Node da esquerda*/
      if(current->left == NULL){ /*Ainda nao existe cartao, inicia os valores*/
        current->left = malloc(sizeof(struct Node)); current->left->left = NULL; current->left->right = NULL;
        current->left->weightLeft = 0; current->left->weightRight=0;
        current->weightLeft +=1; /*Criou novo node*/
        strcpy(current->left->cartao, cartao);
        current->left->saldo = valor;
        printf("ADDED %s\n", cartao);
        return 1;
      }
      else{ /*Pede ao node da esquerda para procurar*/
        int leftSubTreeWeight = addBST(current->left, current, cartao, valor);
        if (leftSubTreeWeight != -1) current->weightLeft = leftSubTreeWeight + 1;
        if(current->weightLeft - current->weightRight > 1){
          printf("TREE UNBALANCED LEFT\n"); /*balanceBST(root)*/
        }
        return current->weightLeft;
      }
    }
    else{
      /*Node da direita*/
      if(current->right == NULL){ /*Ainda nao existe cartao, inicia os valores*/
        current->right = malloc(sizeof(struct Node)); current->right->left = NULL; current->right->right = NULL;
        current->right->weightLeft = 0; current->right->weightRight=0;
        current->weightRight +=1; /*Criou novo node*/
        strcpy(current->right->cartao, cartao);
        current->right->saldo = valor;
        printf("ADDED %s\n", cartao);
        return 1;
      }
      else{ /*Pede ao node da direita para procurar*/
        int rightSubTreeWeight = addBST(current->right, current, cartao, valor);
        if(rightSubTreeWeight != -1) current->weightRight = rightSubTreeWeight + 1;
        if(current->weightRight - current->weightLeft > 1) printf("TREE UNBALANCED RIGHT\n");
        return current->weightRight;
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

}

long long valueOf(char string[]){
  return strtoll(string+2, NULL, 10);
}
