#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include"bmp_header.h"
#include<string.h>

typedef struct {
	unsigned char b, g, r;
	} mat;

#pragma pack(1)
typedef struct {
	unsigned short linie, coloana;
	unsigned char r, g, b;
	} compressed;
#pragma pack()

mat **alocare(int height, int width)
{
	int i, j;
	mat **img;
	
	img = (mat**)calloc(height, sizeof(mat *));
	
	for (i = 0; i < height; i++)
		img[i] = (mat *)calloc(width, sizeof(mat));

return img;
}

//APLICARE FILTRU
mat **filtru(int height, int width, int F[3][3], mat **img_aux)
{
	int i, j, R, G, B, x, y;
	mat **img;
	img = alocare(height, width);	

	for ( i = 1; i <= height; i++)
		for ( j = 1; j <= width; j++) {
			R = 0;
			G = 0;
			B = 0;
			for ( x = -1; x <= 1; x++)
				for ( y = -1; y <= 1; y++) {
					R += img_aux[i+x][j+y].r * F[1+x][1+y];
					G += img_aux[i+x][j+y].g * F[1+x][1+y];
					B += img_aux[i+x][j+y].b * F[1+x][1+y];
				}		
			if ( R > 255 )
				img[i-1][j-1].r = 255;
				else 
					if ( R < 0 )
						img[i-1][j-1].r = 0;
						else 
							img[i-1][j-1].r = R;

			if ( G > 255 )
				img[i-1][j-1].g = 255;
				else 
					if ( G < 0 )
						img[i-1][j-1].g = 0;
						else 
							img[i-1][j-1].g = G;

			if ( B > 255 )
				img[i-1][j-1].b = 255;
				else 
					if ( B < 0 )
						img[i-1][j-1].b = 0;
						else 
							img[i-1][j-1].b = B;
			}

	return img;
}



int main()
{
	//char input_filename[] = "santa.bmp";
	char output_filename[]="";
	char output_filename3[] = "compressed.bin";
	mat **img, **img_aux;
	struct bmp_fileheader header;
	struct bmp_infoheader infoh;
	int  i, j, a, x, y, R, G, B, stop;
	int s, zona, threshold = 25;
	unsigned char padding;
	padding = 0;
	compressed *C;
	int **A;	
	char nume_imagine[25];
	char bin[25];
	int nr, n;	
	char black[]="_black_white.bmp";
	char *p;
	char output_F1[25]="";
	char output_F2[25]="";
	char output_F3[25]="";
	char decompressed[]="decompressed.bmp";
	char output[] = "Venice.bmp";
	
//CITIRE DATE 
FILE *IN = fopen("input.txt", "rt");
	
	fgets(nume_imagine, sizeof(nume_imagine), IN);
	n = strlen(nume_imagine);
	fseek(IN, 0, SEEK_SET);
	fgets(nume_imagine, n , IN);

	fscanf(IN,"%d", &nr);
	int nr_cifre = 0;
	int aux;
	aux = nr;
	while ( aux > 0 ) {
		nr_cifre++;
		aux = aux/10;
		}

	n = strlen(nume_imagine) + 2 + nr_cifre;
	
	printf("sizeof nr: %d %d\n", sizeof(nr), sizeof(int));
	//printf("%d\n", n);
		
	fseek(IN, n, SEEK_SET);
	fgets(bin, sizeof(bin), IN);
	int m;
	m = strlen(bin);
	fseek(IN, 0, SEEK_SET);	
	fseek(IN, n, SEEK_SET);
	fgets(bin, m, IN);
	printf("%s %d\n", nume_imagine, nr);
	printf("Fisier binar: %s ", bin);
	

	fclose(IN);

	FILE *in = fopen(nume_imagine, "rb");

// CITIRE IMAGINE

	fread(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), in);
	fread(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), in);

	img = alocare(infoh.height, infoh.width);
	img_aux = alocare(infoh.height+2, infoh.width+2);

	fseek(in, header.imageDataOffset, SEEK_SET);

	for ( i = 0; i < infoh.height; i++) 	
		for ( j = 0; j < infoh.width; j++) {
			fread(&img[i][j], sizeof(unsigned char), sizeof(mat), in);
			}
		//fseek(in, SEEK_CUR, header.imageDataOffset);
		


	//fclose(in);

//TASK 3 
/*	zona = 0;
	
	C = (compressed**)calloc(infoh.height, sizeof(compressed *));
	
	for (i = 0; i < infoh.height; i++)
		C[i] = (compressed *)calloc(infoh.width, sizeof(compressed));

	
	A = malloc((infoh.height+2)*sizeof(int *));
	
	for (i = 0; i <= infoh.height+1; i++)
		A[i] = calloc((infoh.width+2), sizeof(int));
	
	for ( i = 0; i < infoh.height+2; i++)
		for ( j = 0; j < infoh.width+2; j++)
			A[i][j] = 0;

FILE *out3 =  fopen(output_filename3, "wb");
	
	fwrite(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), out3);
	fwrite(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), out3);
	fseek(out3, header.imageDataOffset, SEEK_SET);
	 
for ( i = 0; i < infoh.height; i++)
		for ( j = 0; j < infoh.width; j++){
			C[i][j].linie = i+1;
			C[i][j].coloana = j+1;
		}

	for ( i = 0; i < infoh.height; i++)
		for ( j = 0; j < infoh.width; j++) {
			if ( A[i+1][j+1] == 0 ) {
				zona ++;
				A[i+1][j+1] = zona;
				printf("VALOARE BUNA %d %d %d \n", i, j, zona);
				R = img[i][j].r;
				G = img[i][j].g;
				B = img[i][j].b; 
				for ( x = 0; x < infoh.height; x++) {
					for ( y = 0; y < infoh.width; y++) {
						a = abs(R - img[x][y].r)+abs(G - img[x][y].g)+abs(B - img[x][y].b);
						if ( A[x+1][y+1] == 0 && a <= threshold && 
						(A[x+1][y] == zona || A[x][y+1] == zona  ||
						 A[x+2][y+1] == zona || A[x+1][y+2] == zona)) {
							A[x+1][y+1] = zona;
							C[x][y].r = R;
							C[x][y].g = G; 
							C[x][y].b = B;
						}
					}
					
				}
			}
		}
	for ( i = 0; i < infoh.height; i++) 	
		for ( j = 0; j < infoh.width; j++) {
			if ( A [i+1][j+1] != A[i][j+1] || A[i+1][j+1] != A[i+1][j] ||
			 A[i+1][j+1] != A[i+2][j+1] || A[i+1][j+1] != A[i+1][j+2] )
			 fwrite(&C[i][j] , sizeof(unsigned char), sizeof(compressed), out3);		

		} 
*/

// BLACK AND WHITE
	p =  strtok(nume_imagine, ".");
	strcat(output_filename, p);
	strcat(output_filename, black);
	printf("%s\n", output_filename);
	
	FILE *out =  fopen(output_filename, "wb");
	
	fwrite(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), out);
	fwrite(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), out);
	fseek(out, header.imageDataOffset, SEEK_SET );

	for ( i = 0; i < infoh.height; i++)
		for ( j = 0; j < infoh.width; j++)
		{	s = img[i][j].r+img[i][j].g+img[i][j].b;
			img[i][j].r = s/3;
		 	img[i][j].g = s/3;
		 	img[i][j].b = s/3;}

	for ( i = 0; i < infoh.height; i++) 	
		for ( j = 0; j < infoh.width; j++) 
			fwrite(&img[i][j], sizeof(unsigned char), sizeof(mat), out);		

fclose(out);
//INITIALIZARE FILTRE

	int Funu[3][3]={{-1,-1,-1}, {-1,8,-1}, {-1,-1,-1}};
	int Fdoi[3][3]={{0,1,0}, {1,-4,1}, {0,1,0}};
	int Ftrei[3][3]={{1,0,-1}, {0,0,0}, {-1,0,1}};

//COPIERE IMAGINE RASTURNATA 

	img_aux = alocare(infoh.height+2, infoh.width+2);

	for ( i = 0; i <= infoh.width+1; i++) {
		img_aux[0][i].r = 0;
		img_aux[0][i].g = 0;
		img_aux[0][i].b = 0;
		img_aux[infoh.height+1][i].r = 0;
		img_aux[infoh.height+1][i].g = 0;
		img_aux[infoh.height+1][i].b = 0;
	}


	for ( i = 1; i <= infoh.height; i++) {
		img_aux[i][0].r = 0;
		img_aux[i][0].g = 0;
		img_aux[i][0].b = 0;
		img_aux[i][infoh.width+1].r = 0;
		img_aux[i][infoh.width+1].g = 0;
		img_aux[i][infoh.width+1].b = 0;
	}

			
	for ( i = 1; i <= infoh.height; i++)
		for ( j = 1; j <= infoh.width; j++) {
			img_aux[i][j].r = img[infoh.height-i][j-1].r;
			img_aux[i][j].g = img[infoh.height-i][j-1].g;
			img_aux[i][j].b = img[infoh.height-i][j-1].b;
		}


//APLICARE F1
	strcat(output_F1, p);
	strcat(output_F1, "_f1.bmp");
	printf("nume output F1: %s\n", output_F1);	

	FILE *out_f1 =  fopen(output_F1, "wb");
	fwrite(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), out_f1);
	fwrite(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), out_f1);
	fseek(out_f1, header.imageDataOffset, SEEK_SET );

	img = filtru (infoh.height, infoh.width, Funu, img_aux);
	
	for ( i = infoh.height-1; i >= 0; i--) 	
		for ( j = 0; j < infoh.width; j++) {
			fwrite(&img[i][j], sizeof(unsigned char), sizeof(mat), out_f1);		

		} 

//APLICARE F2

	strcat(output_F2, p);
	strcat(output_F2, "_f2.bmp");
	printf("nume output F2: %s\n", output_F2);	

	FILE *out_f2 =  fopen(output_F2, "wb");
	fwrite(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), out_f2);
	fwrite(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), out_f2);
	fseek(out_f2, header.imageDataOffset, SEEK_SET );

	img = filtru (infoh.height, infoh.width, Fdoi, img_aux);
	
	for ( i = infoh.height-1; i >= 0; i--) 	
		for ( j = 0; j < infoh.width; j++) {
			fwrite(&img[i][j], sizeof(unsigned char), sizeof(mat), out_f2);		

		} 

//APLICARE F3

	strcat(output_F3, p);
	strcat(output_F3, "_f3.bmp");
	printf("nume output F2: %s\n", output_F3);	

	FILE *out_f3 =  fopen(output_F3, "wb");
	fwrite(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), out_f3);
	fwrite(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), out_f3);
	fseek(out_f3, header.imageDataOffset, SEEK_SET );

	img = filtru (infoh.height, infoh.width, Ftrei, img_aux);
	
	for ( i = infoh.height-1; i >= 0; i--) 	
		for ( j = 0; j < infoh.width; j++) {
			fwrite(&img[i][j], sizeof(unsigned char), sizeof(mat), out_f3);		

		} 
//FREE IMG_AUX
	for ( i = 0; i < infoh.height+2; i++)
		free(img_aux[i]);
	free(img_aux);

	printf("Dimensiuni: %d %d\n", infoh.height, infoh.width);


//DECOMPRESS

	FILE *in_bin = fopen(bin, "rb");

	printf("nume output: %s\n", decompressed);
	
	//CITIRE FISIER BIN
	
	int size_file;

	fseek(in_bin, 0 , SEEK_END);
	size_file = ftell(in_bin);

	fseek(in_bin, 0, SEEK_SET);
	fread(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), in_bin);
	fread(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), in_bin);	
	fseek(in_bin, header.imageDataOffset, SEEK_SET);

	size_file = size_file - header.imageDataOffset;// -infoh.biSize;
	if (size_file % 7 == 0) size_file = size_file / 7;
	else size_file = size_file / 7 + size_file % 7;
	//printf("%d\n", size_file);
	//size_file = size_file / 7;
	printf("->%d\n", size_file);
	
	img = alocare(infoh.height, infoh.width);


	C = (compressed*)calloc(size_file, sizeof(compressed));
	fread(C, sizeof(compressed), size_file, in_bin);

	
	for ( i = 0; i < infoh.height; i++)
		for ( j = 0; j < infoh.width; j++) {
			img[i][j].r = 8;
			img[i][j].g = 8;
			img[i][j].b = 8;
		}

	for ( i = 0; i < size_file; i++){
		img[C[i].linie-1][C[i].coloana-1].r = C[i].r;
		img[C[i].linie-1][C[i].coloana-1].g = C[i].g;
		img[C[i].linie-1][C[i].coloana-1].b = C[i].b;
	}	


	for ( i = 1; i < infoh.height - 1; i++)
		for ( j = 1; j < infoh.width - 1; j++) {
			if (img[i][j].r == 8 && img[i][j].g == 8 && img[i][j].b == 8 ) {

				img[i][j].r = img[i-1][j].r;
				img[i][j].g = img[i-1][j].g;
				img[i][j].b = img[i-1][j].b;
				}		
		}

FILE *out_d =  fopen(decompressed, "wb");

fwrite(&header, sizeof(unsigned char), sizeof(struct bmp_fileheader), out_d);
fwrite(&infoh, sizeof(unsigned char), sizeof(struct bmp_infoheader), out_d);
fseek(out_d, header.imageDataOffset, SEEK_SET);

		for ( i = infoh.height-1; i >= 0; i--) 
			for ( j = 0; j < infoh.width; j++)			
				fwrite(&img[i][j], sizeof(unsigned char), sizeof(mat), out_d);

	free(C);
	fclose(in);
	fclose(out_d);
	fclose(in_bin);
	fclose(out_f1);
	fclose(out_f2);
	fclose(out_f3);
	//fclose(out3);
 

return 0;
}
