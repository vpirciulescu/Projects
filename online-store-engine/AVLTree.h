#ifndef AVLTREE_H_
#define AVLTREE_H_

#include <stdlib.h>

#define MAX(a, b) (((a) >= (b))?(a):(b))

typedef struct node{
	void* elem;
	void* info;
	struct node *pt;
	struct node *lt;
	struct node *rt;
	struct node* next;
	struct node* prev;
	struct node* end;
	long height;
}TreeNode;

typedef struct TTree{
	TreeNode *root;
	TreeNode *nil;
	void* (*createElement)(void*);
	void (*destroyElement)(void*);
	void* (*createInfo)(void*);
	void (*destroyInfo)(void*);
	int (*compare)(void*, void*);
	long size;
}TTree;



TTree* createTree(void* (*createElement)(void*), void (*destroyElement)(void*),
		void* (*createInfo)(void*), void (*destroyInfo)(void*),
		int compare(void*, void*)){
	/* 
	 * TODO: 
	 * 1. Allocate tree
     * 2. Allocate sentinel
     * 3. Set sentinel
     * 4. Set all other fields
	 */

	TTree *tree = (TTree*)malloc(sizeof(TTree));

	TreeNode *nod = (TreeNode*)malloc(sizeof(TreeNode));
	tree->nil = nod;
	tree->nil->height = 0;
	tree->nil->elem = tree->nil->info = NULL;
	tree->nil->pt = tree->nil;
	tree->nil->lt = tree->nil;
	tree->nil->rt = tree->nil;
	tree->nil->next = tree->nil;
	tree->nil->prev = tree->nil;
	tree->nil->end = tree->nil;

	tree->root = tree->nil;	
	
	tree->createElement = createElement;
	tree->destroyElement = destroyElement;
	tree->createInfo = createInfo;
	tree->destroyInfo = destroyInfo; 
	tree->compare = compare;
	tree->size = 0;

	return tree;

}



TreeNode* createTreeNode(TTree *tree, void* value, void* info){
	/* 
	 * TODO: 
	 * 1. Allocate new node
     * 2. Set it's fields using appropriate functions
     * 3. Set all links
     * 4. Set height for the new node
	 */

	TreeNode *newNode = (TreeNode*)malloc(sizeof(TreeNode));

	newNode->pt = tree->nil;
	newNode->lt = newNode->rt = tree->nil;
	newNode->next = tree->nil;
	newNode->prev = tree->nil;
	newNode->end = tree->nil;
	newNode->height = 1;
	newNode->elem = (tree->createElement)((void*)value);
	newNode->info = (tree->createInfo)((void*)info);
	newNode->end = tree->nil;
	
	return newNode;
}

void destroyTreeNode(TTree *tree, TreeNode* node){
	/* 
	 * TODO: 
	 * 1. Destroy fields
     * 2. De-allocate node
	 */
	
	tree->destroyElement(node->elem);
	tree->destroyInfo(node->info);
	free(node);
}

int isEmpty(TTree* tree){

	if(tree->size == 0)
		return 1;

	return 0;

}

TreeNode* search(TTree* tree, TreeNode* x, void* elem){

	// TODO: Search sub-tree rooted at node x
    // Hint: Elements are compared via compare function for this node

	TreeNode *caut_intens;
	caut_intens = x;

	while(caut_intens != tree->nil) {
		
		if((tree->compare)((void*)caut_intens->elem, (void*)elem) == 0) {
			return caut_intens;
		}

		if((tree->compare)((void*)caut_intens->elem, (void*)elem) == -1)
			caut_intens = caut_intens->rt;
		
		else caut_intens = caut_intens->lt;
	}
	
}

TreeNode* minimum(TTree*tree, TreeNode* x){
	
	if(x->lt == tree->nil) {
		return x;
	}	

	minimum(tree, x->lt); 

}

TreeNode* maximum(TTree* tree, TreeNode* x){

	if(x->rt == tree->nil)
		return x;

	maximum(tree, x->rt); 

}

TreeNode* successor(TTree* tree, TreeNode* x){

	if(x == tree->nil)
		return tree->nil;

	if(x->rt != tree->nil)
		return minimum(tree, x->rt);
	

	TreeNode *nod = x->pt;

	while(nod != tree->nil && x == nod->rt) {

		x = nod;
		nod = nod->pt;
	}

	return nod;	
	
}

TreeNode* predecessor(TTree* tree, TreeNode* x){
	
	if (x == tree->nil)
		return tree->nil;

	if(x->lt != tree->nil)
		return maximum(tree, x->lt);

	TreeNode *nod = x->pt;

	while(nod != tree->nil && x == nod->lt){
	
		x = nod;
		nod = nod->pt;
	}

	return nod;	
	
}

void parcurgere_SRD(TTree *tree, TreeNode *nod) {

	if (nod == tree->nil)
		return;
	parcurgere_SRD(tree, nod->lt);
	nod->prev = predecessor(tree, nod);
	TreeNode *q = nod;

	
	if(nod->end == tree->nil) {
		nod->end = nod;
		nod->next = successor(tree, nod);
	}
		else nod->end->next = successor(tree, nod);
	
	parcurgere_SRD(tree, nod->rt);
	
}

void avlRotateLeft(TTree* tree, TreeNode* x){
	TreeNode *y = x->rt;
	x->rt = y->lt;
	if(y->lt != tree->nil)
		y->lt->pt = x; // the parent of the y left subtree is x
	y->pt = x->pt; // update parent for y

	if(x->pt == tree->nil)
		tree->root = y;
	else if(x->pt->lt == x)
		x->pt->lt = y;
	else
		x->pt->rt = y;

	y->lt = x; // x is to the left of y
	x->pt = y; // x parent is y

	// Update heights
	y->height = MAX(y->lt->height, y->rt->height) + 1;
	x->height = MAX(x->lt->height, x->rt->height) + 1;

}

void avlRotateRight(TTree* tree, TreeNode* y){
	TreeNode *x = y->lt;
	y->lt = x->rt;
	if(x->rt != tree->nil)
		x->rt->pt = y;

	x->pt = y->pt;

	if(y->pt == tree->nil)
		tree->root = x;
	else if(y->pt->lt == y)
		y->pt->lt = x;
	else
		y->pt->rt = x;

	x->rt = y;
	y->pt = x;

	// Update heights
	y->height = MAX(y->lt->height, y->rt->height) + 1;
	x->height = MAX(x->lt->height, x->rt->height) + 1;
}

/* Get AVL balance factor for node x */
int avlGetBalance(TTree* tree, TreeNode *x){

	if (x == tree->nil)
		return 0;

	if(x->lt == tree->nil && x->rt == tree->nil)
		return 0;

	return x->lt->height - x->rt->height;

}


void avlFixUp(TTree* tree, TreeNode* y){
   	/* 
	 * TODO: 
	 * 1. Iterate up-wards to nil
     * 2. Update hight and get balance
     * 3. Test for each case
	 */
TreeNode *t;

	while( y != tree->nil)  {
		
		//UPDATE HEIGHT
	 	y->height = MAX(y->lt->height, y->rt->height) +1;

		if (avlGetBalance(tree,y) > 1  && avlGetBalance(tree, y->lt) >= 0) { //LEFT-LEFT CASE CASE			
			avlRotateRight(tree, y);
			
			}
			
		if (avlGetBalance(tree, y) > 1 && avlGetBalance(tree, y->lt) < 0){ // LEFT-RIGHT CASE		
			avlRotateLeft(tree,y->lt);
			avlRotateRight(tree,y);
			
			}
		if (avlGetBalance(tree, y) < -1 && avlGetBalance(tree, y->rt) <= 0)	//RIGH-RIGHT CASE
		{
			avlRotateLeft(tree, y);
		
		}
		if (avlGetBalance(tree, y) < -1 && avlGetBalance(tree, y->rt) > 0)	//RIGHT-LEFT CASE
		{	
			avlRotateRight(tree, y->rt);
			avlRotateLeft(tree,y);
		}
			
		
	t = y;
	y = y->pt;
	}
	
	tree->root = t;
}

void insert(TTree* tree, void* elem, void* info) {
	/* 
	 * TODO: 
	 * 1. Create new node
     * 2. Iterate to down-wards to nil 
     *    (duplicates are added to the list for the search node)
     * 3. Update tree 
     * 4. Update linked list
     * 5. Update size of tree and call fix-up
	 */

	TreeNode *nod = createTreeNode(tree, (void*)elem, (void*)info);	

	if (isEmpty(tree)) {

	tree->root = nod;
	tree->size = 1;

	return;

	}
 
	TTree *parc = tree;
	int deja_existent = 0;	
	
	//Parcurg arborele pentru a insera noul nod sau pentru a-l adauga la lista daca acesta exista deja
	
	while (parc->root != tree->nil)
	{
		if ((tree->compare)((void*)parc->root->elem, (void*)elem) == 0) {  //il adaug la lista

			deja_existent = 1;

		//In cazul in care am mai multe duplicate parcurg lista incepand de la nodul curent
		//Astfel inserarea fiecarui duplicat se face la end;
 
				TreeNode *v = parc->root;
				parc->root->end = nod;

				if(v->next != tree->nil) {
				while((tree->compare)((void*)v->next->elem, (void*)nod->elem) == 0) 
					v = v->next;
				
							
				nod->next = v->next;
				nod->prev = v;
				v->next->prev = nod;
				v->next = nod;
				break;
				
				}
				
				//Il inserez la finalul arborelui si al listei
				if(v->next == tree->nil) {
					v->next = nod;	
					nod->prev = v;
					nod->next = tree->nil;
				}
				
				break;
			
		}
	
	//Inserez nodul in arbore
if(deja_existent == 0) {

		if ((tree->compare)((void*)parc->root->elem, (void*) elem) == 1) //incep sa parcurg arborele
		{
			if (parc->root->lt == tree->nil) //trebuie sa stiu parintele
				break;
			else parc->root = parc->root->lt;
		}
		else
		{
			if (parc->root->rt == tree->nil)
				break;
			else parc->root = parc->root->rt;
		}
	}}


	if(deja_existent == 0) {

		nod->pt = parc->root;		

		if ((tree->compare)((void*)parc->root->elem, (void*) elem) == 1) //nodul devine fiu stang
		{	
			parc->root->lt = nod;
			tree->size++;
		}
		if ((tree->compare)((void*)parc->root->elem, (void*)elem) == -1) //nodul devine fiu drept
		{
			parc->root->rt = nod;
			tree->size++;
		}

		//Fix-up si refacere link-uri;
		avlFixUp(tree, nod);
		parcurgere_SRD(tree, tree->root);
	}


	if (deja_existent == 1) {

	TreeNode *t;

	while(parc->root != tree->nil) {
		
		t = parc->root;
		parc->root = parc->root->pt;
	}

	parc->root = t;
	}

}

void refacere_linkuri(TTree *tree, TreeNode *nod) {

	//printr-o parcurgere SRD  a arborelui refac linkurile 

	if (nod == tree->nil)
		return;

	refacere_linkuri(tree, nod->lt);
	nod->prev = predecessor(tree, nod);
	TreeNode *q = nod;
	nod->end->next = successor(tree, nod);
	refacere_linkuri(tree, nod->rt);
	
	
}

void delete(TTree* tree, void* elem){

	/* 
	 * TODO: 
	 * 1. Search for the node
     * 2. If duplicate delete from the list and exit
     * 3. Otherwise perform tree deletion and update linked list at the end
     * 4. Update size and call fix-up
     * 
     * Hints:
     * 1. If you MUST delete a node from the tree than it has no duplicates!
     *
     * 2. Changing the links with the splice out node is the way to GO,
     *    exchaning node fields does not work in this case,
          you might have duplicates for the splice-out node!!!! 
	 */

  TreeNode* z; // node to be deleted
  TreeNode* y; // Node that is spliced out
  TreeNode* x; // The child of the sliced out node

   // Find the node in the tree if it exists
  if( (z = search(tree, tree->root, elem)) == tree->nil)
    return;

if(z->next != tree->nil) {
	
  if((tree->compare)(z->next->elem, z->elem) == 0) {

	if(z->next->next == tree->nil) {

		z->end = z;
		z->next = tree->nil;
		return;
		}
	
	

	if((tree->compare)((void*)z->next->next->elem, (void*)z->elem) != 0) {
		
		z->next->prev = z;
		z->next = z->next->next;
		z->end = z;
	}

	else {
		TreeNode *d = z;
		while ((tree->compare)((void*)d->next->elem, (void*)z->elem) == 0) {
			d = d->next;
		}

		z->end = d->prev;
		d->next->prev = d->prev;
		d->prev->next = d->next;
	}
	return;
}

}
  /*
   * Note:
   * Case 1: The node has no children - can be directly spliced out
   * Case 2: The node has one child - can be directly spliced out
   * Case 3: The node has two children - the successor is spliced out instead
   */

 // Are we in cases 1,2 or in case 3
 y = ( (z->lt == tree->nil) || (z->rt == tree->nil) ) ? z : successor(tree, z);

 // Set x to the non-NULL child of y (if possible)
 x = (y->lt == tree->nil) ? y->rt : y->lt;


 // Set the new parent for the child
 if(x != tree->nil)
   x->pt = y->pt;

 if(y->pt == tree->nil) // If y is the root
   tree->root = x;
 else if( y == y->pt->lt) // If y is a left child
   y->pt->lt = x;
 else
   y->pt->rt = x;

   /*
    * If we are in case 3 all the links are made
    * for z, we can just copy the key
    */
  if (y != z) {
    z->elem = y->elem;
    z->info = z->info;
}

  TreeNode* aux = y;

  y->end = tree->nil;

  y = y->pt;
  // Free the spliced out node
  free(aux);

  // Re-balance

	if ( y != tree->nil)
		avlFixUp(tree, y);
  // Decrease size	

	tree->size--;

	refacere_linkuri(tree, tree->root);
	
}

void destroyTree(TTree* tree){
	// TODO:

	// Hint: Can you use the list?

	TreeNode *parcurg = tree->root;
	TreeNode *aux;

	while(parcurg != tree->nil) {

		aux = parcurg;
		parcurg = parcurg->next;
		
		destroyTreeNode(tree, aux);
		
	}

}


#endif /* AVLTREE_H_ */
