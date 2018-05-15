package joc;
public class Hero {
private int xp = 0;
private int hp;
private int level;
private int move = 0;
private int lin;
private int col;
private char tip;
private int runde;
private int dmgOverTime;
private int rundeDmgOt;
private boolean viu = true;

public Hero() {
}

public Hero(final int lin, final int col, final char tip) {
 this.hp = newHp(0, tip);
 this.lin = lin;
 this.col = col;
 this.tip = tip;
 this.level = 0;
 this.runde = 0;
 this.xp = 0;
 this.dmgOverTime = 0;
 this.viu = true;
}

public final void setXp(final int xpx) {
 this.xp = xpx;
}

public final int getXp() {
 return xp;
}

public final void setHp0(final int lvl, final char tipErou) {
 this.hp = newHp(lvl, tipErou);
}

public final void setHp(final int hitp) {
 this.hp = hitp;
}

public final int getHp() {
 return hp;
}

public final void setLevel(final int lvl) {
 this.level = lvl;
}

public final int getLevel() {
 return level;
}

public final void setLinie(final int linie) {
 this.lin = linie;
}

public final int getLinie() {
 return lin;
}

public final void setColoana(final int coloana) {
 this.col = coloana;
}

public final int getColoana() {
 return col;
}

public final void setRunde(final int r) {
 this.runde = r;
}

public final int getRunde() {
 return runde;
}

public final void setMove(final int m) {
 this.move = m;
}

public final int getMove() {
 return move;
}

public final char getTip() {
 return tip;
}

public final boolean getStare() {
 return viu;
}

public final void setStare(final boolean st) {
 this.viu = st;
}

public final void setDmgOverTime(final int dmg) {
 this.dmgOverTime = dmg;
}

public final int getDmgOverTime() {
 return dmgOverTime;
}

public final void setRundeDmgOverTime(final int nr) {
 this.rundeDmgOt = nr;
}

public final int getRundeDmgOverTime() {
 return rundeDmgOt;
}

public final int newXp(final int xpx, final int lvlwin, final int lvllos) {
final int xp0 = 200;
final int multiply = 40;
final int zer = 0;
int newXp;
newXp = xpx + Math.max(zer, xp0 - (lvlwin - lvllos) * multiply);
return newXp;
}

public final int newHp(final int lvl, final char c) {

if (c == 'R') {
 final int hpInitial = 600;
 final int multiplyLvl = 40;
 return (hpInitial + multiplyLvl * lvl);
}
if (c == 'K') {
 final int hpInitial = 900;
 final int multiplyLvl = 80;
 return (hpInitial + multiplyLvl * lvl);
}
if (c == 'W') {
 final int hpInitial = 400;
 final int multiplyLvl = 30;
 return (hpInitial + multiplyLvl * lvl);
}
if (c == 'P') {
 final int hpInitial = 500;
 final int multiplyLvl = 50;
 return (hpInitial + multiplyLvl * lvl);
}
return 0;
}

public float unu(final Hero atacant, final Hero victima, final char land) {
 return 0;
}

public float doi(final Hero atacant, final Hero victima, final char land) {
 return 0;
}

public float trei(final Hero atacand, final char land) {
 return 0;
}

public float patru(final Hero atacand, final char land) {
 return 0;
}
}
