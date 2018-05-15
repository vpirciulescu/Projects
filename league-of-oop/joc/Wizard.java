package joc;

public class Wizard extends Hero {

 private static final float DRAIN_PROCENT = 0.2f;
 private static final float DRAIN_PROCENT_PLUS = 0.05f;

 private static final int HP0_ROGUE = 600;
 private static final int HP_ROGUE_PLUS = 40;
 private static final int HP0_KNIGHT = 900;
 private static final int HP_KNIGHT_PLUS = 80;
 private static final int HP0_WIZARD = 400;
 private static final int HP_WIZARD_PLUS = 30;
 private static final int HP0_PYRO = 500;
 private static final int HP_PYRO_PLUS = 50;

 private static final float DRAIN_ROGUE_MODF = 0.8f;
 private static final float DRAIN_KNIGHT_MODF = 1.2f;
 private static final float DRAIN_PYRO_MODF = 0.9f;
 private static final float DRAIN_WIZARD_MODF = 1.05f;
 private static final float HP_MUL = 0.3f;

 private static final float DEF_ROGUE_MODF = 1.2f;
 private static final float DEF_KNIGHT_MODF = 1.4f;
 private static final float DEF_PYRO_MODF = 1.3f;

 private static final float DEF_PROCENT = 0.35f;
 private static final float DEF_PROCENT_PLUS = 0.02f;
 private static final float DEF_PROCENT_MAX = 0.7f;

 private static final float DMG_LAND_MODF = 1.1f;

public Wizard() {
 super();
}

public Wizard(final int lin, final int col, final char tip) {
 super(lin, col, tip);
}

public final float unu(final Hero atacant, final Hero victima, final char land) {

 float damage = 0;
 float procent;
 float opponentMaxHp = 0;
 float opponentCurrentHp;

 procent = DRAIN_PROCENT + DRAIN_PROCENT_PLUS * atacant.getLevel();

 if (victima.getTip() == 'R') {
  procent = procent * DRAIN_ROGUE_MODF;
 } else if (victima.getTip() == 'K') {
  procent = procent * DRAIN_KNIGHT_MODF;
 } else if (victima.getTip() == 'W') {
  procent = procent * DRAIN_WIZARD_MODF;
 } else if (victima.getTip() == 'P') {
  procent = procent * DRAIN_PYRO_MODF;
 }

 if (victima.getTip() == 'R') {
  opponentMaxHp = HP0_ROGUE + HP_ROGUE_PLUS * victima.getLevel();
 } else if (victima.getTip() == 'K') {
  opponentMaxHp = HP0_KNIGHT + HP_KNIGHT_PLUS * victima.getLevel();
 } else if (victima.getTip() == 'W') {
  opponentMaxHp = HP0_WIZARD + HP_WIZARD_PLUS * victima.getLevel();
 } else if (victima.getTip() == 'P') {
  opponentMaxHp = HP0_PYRO + HP_PYRO_PLUS * victima.getLevel();
 }

 opponentCurrentHp = victima.getHp();
 damage = procent * Math.min(HP_MUL * opponentMaxHp, opponentCurrentHp);

 if (land == 'D') {
  damage = damage * DMG_LAND_MODF;
 }

 return Math.round(damage);
}

public final float doi(final Hero atacant, final Hero victima, final char land) {

float damage = 0;
float procent;
float dmgAdv = 0;
Pyromancer pyr = new Pyromancer();
Knight kng = new Knight();
Rogue rog = new Rogue();

if (victima.getTip() == 'R') {
  dmgAdv += rog.trei(victima, land) + rog.patru(victima, land);
} else if (victima.getTip() == 'K') {
  dmgAdv += kng.trei(victima, land) + kng.patru(victima, land);
} else if (victima.getTip() == 'P') {
  dmgAdv += pyr.trei(victima, land) + pyr.patru(victima, land);
}

if (victima.getTip() == 'W') {
 return damage;
}
 dmgAdv = Math.round(dmgAdv);
 procent = Math.min(DEF_PROCENT_MAX, DEF_PROCENT + DEF_PROCENT_PLUS * atacant.getLevel());
 damage = dmgAdv * procent;

 if (land == 'D') {
  damage = damage * DMG_LAND_MODF;
 }

 if (victima.getTip() == 'R') {
  damage = damage * DEF_ROGUE_MODF;
 } else if (victima.getTip() == 'K') {
  damage = damage * DEF_KNIGHT_MODF;
 } else if (victima.getTip() == 'P') {
  damage = damage * DEF_PYRO_MODF;
 }
return Math.round(damage);
}
}
