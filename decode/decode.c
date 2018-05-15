#include<stdio.h>
#include<unistd.h>
#include<string.h>
#include<stdlib.h>

int main() {

	int c, i;
	char sir[5000];
	char *code;
	FILE *file;
	int size = 0;
	
	file = fopen("input.dat", "r");

	if (file) {
		i = 0;
		while ((c = getc(file)) != EOF) {
			
			sir[i] = c;
			i++;
		}
			
	}


	int *cd;
	cd = malloc (100 * sizeof(int));

	for (i = 415; i < strlen(sir + 415); i++) {
		if (sir[i] == 'a')
			printf("gasit");
		cd[(int)sir[i] - 97]++;
		if (sir[i] == '.')
			cd[26]++;
		if (sir[i] == ' ')
			cd[27]++;
		
	} 

	for (i = 0; i < 26; i++)
	
		printf("%c --> %d \n", i+97, cd[i]);

	printf(". --> %d \n", cd[i]);
	printf("  --> %d \n", cd[i + 1]);
	fclose(file);
	return (0);
}	
