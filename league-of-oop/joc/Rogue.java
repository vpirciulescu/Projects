package joc;

public class Rogue extends Hero {

 private static final int BASE_DMG = 200;
 private static final int BASE_DMG_PLUS = 20;

 private static final int PARA_DMG = 40;
 private static final int PARA_DMG_PLUS = 10;

 private static final int RUNDE_OVER = 3;
 private static final int RUNDE_OVER_PLUS = 6;

 private static final float BACK_ROGUE_MODF = 1.2f;
 private static final float BACK_KNIGHT_MODF = 0.9f;
 private static final float BACK_PYRO_MODF = 1.25f;
 private static final float BACK_WIZARD_MODF = 1.25f;

 private static final float PARA_ROGUE_MODF = 0.9f;
 private static final float PARA_KNIGHT_MODF = 0.8f;
 private static final float PARA_PYRO_MODF = 1.2f;
 private static final float PARA_WIZARD_MODF = 1.25f;

 private static final float DMG_LAND_MODF = 1.15f;
 private static final float DMG_SPECIAL = 1.5f;

public Rogue() {
 super();
}

public Rogue(final int lin, final int col, final char tip) {
 super(lin, col, tip);
}

public final float trei(final Hero victima, final char land) {

 float damage;
 damage = BASE_DMG + BASE_DMG_PLUS * victima.getLevel();

 if (land == 'W') {
  damage = damage * DMG_LAND_MODF;
 }
 damage = Math.round(damage);
 if ((victima.getRunde() % RUNDE_OVER == 0) && (land == 'W')) {
  damage = damage * DMG_SPECIAL;
 }
 return Math.round(damage);
}

public final float patru(final Hero victima, final char land) {

 float damage;
 damage = PARA_DMG + PARA_DMG_PLUS * victima.getLevel();
 if (land == 'W') {
  damage = damage * DMG_LAND_MODF;
 }
 return Math.round(damage);
}

public final float unu(final Hero atacant, final Hero victima, final char land) {

 float damage;

 damage = BASE_DMG + BASE_DMG_PLUS * atacant.getLevel();

 if ((atacant.getRunde() % RUNDE_OVER == 0) && (land == 'W')) {
  damage = damage * DMG_SPECIAL;
 }

 if (land == 'W') {
  damage = damage * DMG_LAND_MODF;
 }
 damage = Math.round(damage);

 if (victima.getTip() == 'R') {
  damage = damage * BACK_ROGUE_MODF;
 } else if (victima.getTip() == 'K') {
  damage = damage * BACK_KNIGHT_MODF;
 } else if (victima.getTip() == 'W') {
  damage = damage * BACK_WIZARD_MODF;
 } else if (victima.getTip() == 'P') {
  damage = damage * BACK_PYRO_MODF;
 }

return Math.round(damage);
}

public final float doi(final Hero atacant, final Hero victima, final char land) {

 float damage = 0;
 damage = PARA_DMG + PARA_DMG_PLUS * atacant.getLevel();

 if (land == 'W') {
  damage = damage * DMG_LAND_MODF;
  victima.setMove(RUNDE_OVER_PLUS);
  victima.setRundeDmgOverTime(RUNDE_OVER_PLUS);
 } else {
  victima.setMove(RUNDE_OVER);
  victima.setRundeDmgOverTime(RUNDE_OVER);
 }

 damage = Math.round(damage);

 if (victima.getTip() == 'R') {
  damage = damage * PARA_ROGUE_MODF;
 } else if (victima.getTip() == 'K') {
  damage = damage * PARA_KNIGHT_MODF;
 } else if (victima.getTip() == 'W') {
  damage = damage * PARA_WIZARD_MODF;
 } else if (victima.getTip() == 'P') {
  damage = damage * PARA_PYRO_MODF;
 }

 victima.setDmgOverTime((int) Math.round(damage));
 return Math.round(damage);
}
}
