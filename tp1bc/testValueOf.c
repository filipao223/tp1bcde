#include <stdio.h>
#include <stdlib.h>

int valueOf(char string[]){
  int i, value=0;
  for(i=0;string[i]!='\0'; i++){
    value+=string[i];
  }
  return value;
}

int main(){
  char str1[20] = "TP111112";
  char str2[20] = "TP111111";
  printf("String '%s' tem valor de %i e string '%s' tem valor de %i\n", str1, valueOf(str1), str2, valueOf(str2));
  return 0;
}
