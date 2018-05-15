package joc;

public class Knight extends Hero {

 private static final int BASE_DMG_EXECUTE = 200;
 private static final int BASE_DMG_EXECUTE_PLUS = 30;
 private static final int BASE_DMG_SLAM = 100;
 private static final int BASE_DMG_SLAM_PLUS = 40;

 private static final int HP0_ROGUE = 600;
 private static final int HP_ROGUE_PLUS = 40;
 private static final int HP0_KNIGHT = 900;
 private static final int HP_KNIGHT_PLUS = 80;
 private static final int HP0_WIZARD = 400;
 private static final int HP_WIZARD_PLUS = 30;
 private static final int HP0_PYRO = 500;
 private static final int HP_PYRO_PLUS = 50;

 private static final float HP_LIMIT = 0.2f;
 private static final float HP_LIMIT_PLUS = 0.01f;
 private static final float HP_LIMIT_MAX = 0.4f;

 private static final float EXE_ROGUE_MODF = 1.15f;
 private static final float EXE_KNIGHT_MODF = 1.0f;
 private static final float EXE_PYRO_MODF = 1.1f;
 private static final float EXE_WIZARD_MODF = 0.8f;

 private static final float SLAM_ROGUE_MODF = 0.8f;
 private static final float SLAM_KNIGHT_MODF = 1.2f;
 private static final float SLAM_PYRO_MODF = 0.9f;
 private static final float SLAM_WIZARD_MODF = 1.05f;

 private static final float DMG_LAND_MODF = 1.15f;
 private static final int STOP = 1;

 public Knight() {
  super();
}

public Knight(final int lin, final int col, final char tip) {
 super(lin, col, tip);
}

 public final float trei(final Hero victima, final char land) {

  float damage;
  damage = BASE_DMG_EXECUTE + BASE_DMG_EXECUTE_PLUS * victima.getLevel();
  if (land == 'L') {
   damage = damage * DMG_LAND_MODF;
  }
  return Math.round(damage);
}

public final float patru(final Hero victima, final char land) {

  float damage;
  damage = BASE_DMG_SLAM + BASE_DMG_SLAM_PLUS * victima.getLevel();
  if (land == 'L') {
   damage = damage * DMG_LAND_MODF;
  }
  return Math.round(damage);
}

public final float unu(final Hero atacant, final Hero victima, final char land) {

 float damage;
 float hpVictima = 0;
 float hpDamage;

 if (victima.getTip() == 'R') {
  hpVictima = HP0_ROGUE + HP_ROGUE_PLUS * victima.getLevel();
 } else if (victima.getTip() == 'K') {
  hpVictima = HP0_KNIGHT + HP_KNIGHT_PLUS * victima.getLevel();
 } else if (victima.getTip() == 'W') {
  hpVictima = HP0_WIZARD + HP_WIZARD_PLUS * victima.getLevel();
 } else if (victima.getTip() == 'P') {
  hpVictima = HP0_PYRO + HP_PYRO_PLUS * victima.getLevel();
 }

 hpDamage = hpVictima * Math.min(HP_LIMIT_MAX, (HP_LIMIT + HP_LIMIT_PLUS * atacant.getLevel()));

 if (hpDamage > victima.getHp()) {
  damage = victima.getHp();
  return damage;
 }

 damage = BASE_DMG_EXECUTE + BASE_DMG_EXECUTE_PLUS * atacant.getLevel();
 if (land == 'L') {
  damage = damage * DMG_LAND_MODF;
 }

 damage = Math.round(damage);

 if (victima.getTip() == 'R') {
  damage = damage * EXE_ROGUE_MODF;
 } else if (victima.getTip() == 'K') {
  damage = damage * EXE_KNIGHT_MODF;
 } else if (victima.getTip() == 'P') {
  damage = damage * EXE_PYRO_MODF;
 } else if (victima.getTip() == 'W') {
  damage = damage * EXE_WIZARD_MODF;
 }
 return Math.round(damage);
}

public final float doi(final Hero atacant, final Hero victima, final char land) {

 float damage;

 damage = BASE_DMG_SLAM + BASE_DMG_SLAM_PLUS * atacant.getLevel();
 victima.setMove(STOP);

 if (land == 'L') {
  damage = damage * DMG_LAND_MODF;
 }

 damage = Math.round(damage);

 if (victima.getTip() == 'R') {
  damage = damage * SLAM_ROGUE_MODF;
 } else if (victima.getTip() == 'K') {
  damage = damage * SLAM_KNIGHT_MODF;
 } else if (victima.getTip() == 'P') {
  damage = damage * SLAM_PYRO_MODF;
 } else if (victima.getTip() == 'W') {
  damage = damage * SLAM_WIZARD_MODF;
 }

return Math.round(damage);
}
}
