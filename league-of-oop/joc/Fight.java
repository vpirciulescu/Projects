package joc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Fight {

 public static final int CT = 3;
 public static final int LVP = 250;
 public static final int LC = 50;
 final int nb = 200;
 int n;
 int m;
 int persj;
 int rd;
 char[][] teren = new char[nb][nb];
 char[][] mut = new char[nb][nb];
 char mutare;
 Hero[] eroi = new Hero[nb];
 final int zero = 0;

 public Fight(final String inputFile) throws IOException {

 Scanner input = new Scanner(new File(inputFile));
 final int indexUnu = 0;

 String line;
 n = input.nextInt();
 m = input.nextInt();
 m += m;
 line = input.nextLine();
 for (int i = 0; i < n; i++) {

 line = input.nextLine();
 for (int j = 0; j < line.length(); j++) {
  teren[i][j] = line.charAt(j);
 }
}

 persj = input.nextInt();
 line = input.nextLine();

 for (int i = 0; i < persj; i++) {
 String[] spl = new String[CT];
 line = input.nextLine();
 String rgx;
 rgx = " ";
 spl = line.split(rgx);

 if (line.charAt(indexUnu) == 'R') {
  eroi[i] = new Rogue(Integer.parseInt(spl[1]), Integer.parseInt(spl[2]), 'R');
  eroi[i].setHp0(zero, 'R');
 } else if (line.charAt(indexUnu) == 'K') {
  eroi[i] = new Knight(Integer.parseInt(spl[1]), Integer.parseInt(spl[2]), 'K');
  eroi[i].setHp0(zero, 'K');
 } else if (line.charAt(indexUnu) == 'P') {
  eroi[i] = new Knight(Integer.parseInt(spl[1]), Integer.parseInt(spl[2]), 'P');
  eroi[i].setHp0(zero, 'P');
 } else if (line.charAt(indexUnu) == 'W') {
  eroi[i] = new Knight(Integer.parseInt(spl[1]), Integer.parseInt(spl[2]), 'W');
  eroi[i].setHp0(zero, 'W');
 }
}
  rd = input.nextInt();
  line = input.nextLine();
  for (int i = 0; i < rd; i++) {

   line = input.nextLine();

   for (int j = 0; j < line.length(); j++) {
    mut[i][j] = line.charAt(j);
   }

 }
 input.close();
}

public final void start() {

  final boolean dead = false;

  for (int i = 0; i < rd; i++) {
   for (int j = 0; j < persj; j++) {

    if (mut[i][j] == 'U' && (eroi[j].getMove() == 0)) {
     eroi[j].setLinie(eroi[j].getLinie() - 1);
    } else if (mut[i][j] == 'D' && (eroi[j].getMove() == 0)) {
     eroi[j].setLinie(eroi[j].getLinie() + 1);
    } else if (mut[i][j] == 'L' && (eroi[j].getMove() == 0)) {
     eroi[j].setColoana(eroi[j].getColoana() - 1);
    } else if (mut[i][j] == 'R' && (eroi[j].getMove() == 0)) {
     eroi[j].setColoana(eroi[j].getColoana() + 1);
    }
  }

  for (int k = 0; k < persj; k++) {
   if (eroi[k].getMove() > 0) {
    eroi[k].setMove(eroi[k].getMove() - 1);
   }
  }

  for (int k = 0; k < persj; k++) {
   if (eroi[k].getRundeDmgOverTime() > 0) {
    eroi[k].setHp(eroi[k].getHp() - eroi[k].getDmgOverTime());
    eroi[k].setRundeDmgOverTime(eroi[k].getRundeDmgOverTime() - 1);
    if (eroi[k].getHp() <= 0) {
     eroi[k].setStare(dead);
    }
   }
  }

  int damageUnu = 0;
  int damageDoi = 0;
  int l;
  int c;
  int hpnou = 0;
  int xpnou = 0;

  Pyromancer py = new Pyromancer();
  Knight kn = new Knight();
  Rogue rg = new Rogue();
  Wizard wz = new Wizard();

 for (int k = 0; k < persj - 1; k++) {
  for (int j = k + 1; j < persj; j++) {

   if ((eroi[k].getLinie() == eroi[j].getLinie()) && (eroi[k].getColoana() == eroi[j].getColoana())
      && (eroi[k].getStare()) && (eroi[j].getStare())) {

   if ((eroi[k].getStare()) && (eroi[j].getStare())) {
    l = eroi[k].getLinie();
    c = eroi[k].getColoana();

  if (eroi[j].getTip() == 'W') {
 damageDoi = (int) (wz.unu(eroi[j], eroi[k], teren[l][c]) + wz.doi(eroi[j], eroi[k], teren[l][c]));
 }

 if (eroi[k].getTip() == 'R') {
  damageUnu = (int) (rg.unu(eroi[k], eroi[j], teren[l][c]) + rg.doi(eroi[k], eroi[j], teren[l][c]));
 } else if (eroi[k].getTip() == 'K') {
  damageUnu = (int) (kn.unu(eroi[k], eroi[j], teren[l][c]) + kn.doi(eroi[k], eroi[j], teren[l][c]));
    } else if (eroi[k].getTip() == 'W') {
  damageUnu = (int) (wz.unu(eroi[k], eroi[j], teren[l][c]) + wz.doi(eroi[k], eroi[j], teren[l][c]));
    } else if (eroi[k].getTip() == 'P') {
  damageUnu = (int) (py.unu(eroi[k], eroi[j], teren[l][c]) + py.doi(eroi[k], eroi[j], teren[l][c]));
    }

    if (eroi[j].getTip() == 'R') {
  damageDoi = (int) (rg.unu(eroi[j], eroi[k], teren[l][c]) + rg.doi(eroi[j], eroi[k], teren[l][c]));
    } else if (eroi[j].getTip() == 'K') {
  damageDoi = (int) (kn.unu(eroi[j], eroi[k], teren[l][c]) + kn.doi(eroi[j], eroi[k], teren[l][c]));
   // } else if (eroi[j].getTip() == 'W') {
//damageDoi = (int) (wz.unu(eroi[j], eroi[k], teren[l][c]) + wz.doi(eroi[j], eroi[k], teren[l][c]));
    } else if (eroi[j].getTip() == 'P') {
  damageDoi = (int) (py.unu(eroi[j], eroi[k], teren[l][c]) + py.doi(eroi[j], eroi[k], teren[l][c]));
    }

    eroi[j].setRunde(eroi[j].getRunde() + 1);
    eroi[k].setRunde(eroi[k].getRunde() + 1);

   hpnou = eroi[j].getHp() - damageUnu;
   eroi[j].setHp(hpnou);
   hpnou = eroi[k].getHp() - damageDoi;
   eroi[k].setHp(hpnou);

   int levelNou;
   if (eroi[j].getHp() <= 0) {
    eroi[j].setStare(dead);
    if (eroi[k].getHp() > 0) {
     xpnou = eroi[k].newXp(eroi[k].getXp(), eroi[k].getLevel(), eroi[j].getLevel());
     eroi[k].setXp(xpnou);
     levelNou = (xpnou - LVP) / LC + 1;
     if (levelNou > eroi[k].getLevel()) {
      eroi[k].setLevel(levelNou);
      eroi[k].setHp0(eroi[k].getLevel(), eroi[k].getTip());
     }
    }
   }

   if (eroi[k].getHp() <= 0) {
    eroi[k].setStare(dead);
    if (eroi[j].getHp() > 0) {
     xpnou = eroi[j].newXp(eroi[j].getXp(), eroi[j].getLevel(), eroi[k].getLevel());
     eroi[j].setXp(xpnou);
     levelNou = (xpnou - LVP) / LC + 1;
     if (levelNou > eroi[j].getLevel()) {
      eroi[j].setLevel(levelNou);
      eroi[j].setHp0(eroi[j].getLevel(), eroi[j].getTip());
     }
    }
   }
  }
 }
}
}
}
}

 public final void end(final String outputFile) throws IOException {

  File fout = new File(outputFile);
  FileOutputStream fos = new FileOutputStream(fout);
  BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

 for (int i = 0; i < persj; i++) {
   if (!eroi[i].getStare()) {
    bw.write(eroi[i].getTip() + " dead");
   } else {
     bw.write(eroi[i].getTip() + " " + eroi[i].getLevel() + " " + eroi[i].getXp() + " "
 + eroi[i].getHp() + " " + eroi[i].getLinie() + " " + eroi[i].getColoana());
    }
    bw.newLine();
 }
 bw.close();
}

}
