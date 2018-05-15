/*
 ============================================================================
 Name        : Tema2.c
 Author      : Dan Novischi
 Version     :
 Copyright   : Your copyright notice
 Description : Dictionare
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "AVLTree.h"

/* Lungimea maxima a unui buffer */
#define BUFLEN 1024

/* Lungimea unui element din arbore */
#define ELEMENT_TREE_LENGTH 3

/* Range of models*/
typedef struct Range{
	int *index; // Array of models in the range
	int size; // Number of array elements
	int capacity; // Array capacity
}Range;

void* createStrElement(void* str){

	char *c = malloc(sizeof(char));
	strncpy(c, (char*)str, 3);
	return c;
}

void destroyStrElement(void* elem){
	free((char*)elem);
}


void* createPriceElement(void* price){
	
	long *p = malloc(sizeof(long));
	*p = *((long*)(price));
	return p;
}

void destroyPriceElement(void* price){
	free((long*)price);
}

void* createIndexInfo(void* index){
	int *i = malloc(sizeof(int));
	*i = *((int*)(index));
	return i;
}

void destroyIndexInfo(void* index){
	free((int*)index);
}

int compareStr(void* str1, void* str2){
	if(strcmp(((char*)str1),((char*)str2)) > 0)
		return 1;	
	if(strcmp(((char*)str1),((char*)str2)) < 0)
		return -1;
	return 0;
}

int comparePrice(void* price1, void* price2){
	if(*((long*)price1) < *((long*)price2))
		return -1;
	if(*((long*)price1) > *((long*)price2))
		return 1;
	return 0;
}

void buildTreesFromFile(char* fileName, TTree* modelTree, TTree* priceTree){
	 
//parcurg fisierul si inserez fiecare element in arborele corespunzator 

	if(fileName == NULL) 
		return;

	FILE *file = fopen(fileName, "r");	
	
	if(file == NULL)
		return;

	int poz = ftell(file);
	char *buf = (char*)malloc(BUFLEN+1);
	while(fgets(buf, BUFLEN, file) != NULL) {
		buf[strlen(buf)] = '\0';
		char *m = strtok(buf, ",");
		long p = atol(strtok(NULL,","));
		insert(modelTree, m, &poz);
		insert(priceTree, &p, &poz);
		poz = ftell(file);
	}

}

Range* modelGroupQuery(TTree* tree, char* q){

	//parcurg arborele cu ajutorul listei, verific cu strncmp daca modelul actual este cel cautat
	//in caz afirmativ ii retin indexul (pozitia in fisier), altfel parcurg in continuare;

	TreeNode *parcurg = minimum(tree, tree->root);
	Range *range;
	int n = strlen(q);
	range = (Range*)malloc(sizeof(Range));
	range->index = (int*)malloc(999*sizeof(int));
	range->capacity = 0;
	range->size = 0;
	
	while(parcurg != tree->nil) {
		
		if(strncmp(q, parcurg->elem, n) == 0){
			range->index[range->size] = *(int*)parcurg->info;
			range->size++;
		}
	parcurg = parcurg->next;
	}

	return range;
}

Range* modelRangeQuery(TTree* tree, char* q, char* p){
	
	//parcurg arborele cu ajutorul listei, verific cu strncmp daca modelul actual este cel cautat
	//in caz afirmativ ii retin indexul (pozitia in fisier), altfel parcurg in continuare;
	
	TreeNode *parcurg = minimum(tree, tree->root);
	Range *range;
	int n1 = strlen(q); 
	int n2 = strlen(p);
	range = (Range*)malloc(sizeof(Range));
	range->index = (int*)malloc(999*sizeof(int));
	range->capacity = 0;
	range->size = 0;
	
	while(parcurg != tree->nil) {
		
		if((strncmp(q, parcurg->elem, n1) <= 0) && (strncmp(p, parcurg->elem, n2) >= 0)){
			range->index[range->size] = *(int*)parcurg->info;
			range->size++;
		}
	parcurg = parcurg->next;
	}

	return range;


}
Range* priceRangeQuery(TTree* tree, long q, long p){

	//similar modelRangeQuery

	TreeNode *parcurg = minimum(tree, tree->root);
	Range *range;
	
	range = (Range*)malloc(sizeof(Range));
	range->index = (int*)malloc(999*sizeof(int));
	range->capacity = 0;
	range->size = 0;
	

	while(parcurg != tree->nil) {
		long price = *(long*)parcurg->elem;
		
		if(price > q && price < p){
			range->index[range->size] = *(int*)parcurg->info;
			range->size++;			
			
		}
	parcurg = parcurg->next;
	}

	return range;
}

Range* modelPriceRangeQuery(char* fileName, TTree* tree, char* m1, char* m2, long p1, long p2){
	//generez range-ul de indecsi pentru cheiele de cautare, dat fiind ca tree este dictionarul pentru modele
	//folosind range-ul auxiliar verific fiecare produs, daca acesta se afla in intervalul dat (p1,p2),
	//atunci ii retin indexul.

	FILE * file = fopen(fileName,"r");

	
	Range *range_aux = modelRangeQuery(tree, m1, m2);
	Range *range;
	
	range = (Range*)malloc(sizeof(Range));
	range->index = (int*)malloc(999*sizeof(int));
	range->capacity = 0;
	range->size = 0;

	char *buf = (char*) malloc(BUFLEN+1);

	for(int i = 0; i < range_aux->size;i++){
		fseek(file,range_aux->index[i],SEEK_SET);
		if(fgets(buf,BUFLEN,file) != NULL){
			char* model = strtok(buf,",");
			long price = atol(strtok(NULL,","));
			if (price > p1 && price < p2) {
				range->index[range->size] = range_aux->index[i];
				range->size++;		
			}
		}
	}
	
	free(buf);
	fclose(file);

	return range;

	

}


void printFile(char* fileName);
void inorderModelTreePrint(TTree* tree, TreeNode* node);
void inorderPriceTreePrint(TTree* tree, TreeNode* node);
void printRange(Range* range, char* fileName);


int main(void) {
	
	TTree* modelTree = createTree(createStrElement, destroyStrElement, createIndexInfo, destroyIndexInfo, compareStr);
	TTree* priceTree = createTree(createPriceElement, destroyPriceElement, createIndexInfo, destroyIndexInfo, comparePrice);

	buildTreesFromFile("input.csv", modelTree, priceTree);

	printf("Model Tree In Order:\n");
	inorderModelTreePrint(modelTree, modelTree->root);
	printf("\n\n");

	printf("Price Tree In Order:\n");
	inorderPriceTreePrint(priceTree, priceTree->root);
	printf("\n\n");

	printf("Group Model Search:\n");
	Range *range = modelGroupQuery(modelTree,"MG3");
	printRange(range,"input.csv");
	printf("\n\n");

	printf("Price Range Search:\n");
	Range *range2 = priceRangeQuery(priceTree,100,400);
	printRange(range2,"input.csv");
	printf("\n\n");

	printf("Model Range Search:\n");
	Range *range3 = modelRangeQuery(modelTree,"L2","M");
	printRange(range3,"input.csv");
	printf("\n\n");

	printf("Model Price Range Search:\n");
	Range *range4 = modelPriceRangeQuery("input.csv",modelTree,"L2","M", 300, 600);
	printRange(range4,"input.csv");

	free(range->index);
	free(range);
	free(range2->index);
	free(range2);
	free(range3->index);
	free(range3);
	free(range4->index);
	free(range4);
	destroyTree(priceTree);
	destroyTree(modelTree); 

	return 0;
}



void inorderModelTreePrint(TTree* tree, TreeNode* node){
	if(node != tree->nil){
		inorderModelTreePrint(tree, node->lt);
		TreeNode* begin = node;
		TreeNode* end = node->end->next;
		while(begin != end){
			printf("%d:%s  ",*((int*)begin->info),(char*)begin->elem);
			begin = begin->next;
		}
		inorderModelTreePrint(tree, node->rt);
	}
}

void inorderPriceTreePrint(TTree* tree, TreeNode* node){
	if(node != tree->nil){
		inorderPriceTreePrint(tree, node->lt);
		TreeNode* begin = node;
		TreeNode* end = node->end->next;
		while(begin != end){
			printf("%d:%ld  ",*((int*)begin->info),*((long*)begin->elem));
			begin = begin->next;
		}
		inorderPriceTreePrint(tree, node->rt);
	}
}

void printRange(Range* range, char* fileName){
	if(fileName == NULL) return;
	FILE * file = fopen(fileName,"r");
	if (file == NULL) return;

	char *buf = (char*) malloc(BUFLEN+1);

	for(int i = 0; i < range->size;i++){
		fseek(file,range->index[i],SEEK_SET);
		if(fgets(buf,BUFLEN,file) != NULL){
			char* model = strtok(buf,",");
			long price = atol(strtok(NULL,","));
			printf("%s:%ld  ", model, price);
		}
	}
	printf("\n");
	free(buf);
	fclose(file);
}

void printFile(char* fileName){
	printf("---------\n");
	if(fileName == NULL) return;
	FILE * file = fopen(fileName,"r");
	if (file == NULL)
		return;

	char *buf = (char*) malloc(BUFLEN+1);
	while(fgets(buf,BUFLEN,file) != NULL){
		buf[strlen(buf) - 1] = '\0';
		printf("%s",buf);
	}
	printf("\n");
	printf("---------\n");
	free(buf);
	fclose(file);
}
