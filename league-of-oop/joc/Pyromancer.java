package joc;

public class Pyromancer extends Hero {

 private static final int DMG_FIRE = 350;
 private static final int DMG_FIRE_PLUS = 50;

 private static final float DMG_ROGUE_MODF = 0.8f;
 private static final float DMG_KNIGHT_MODF = 1.2f;
 private static final float DMG_PYRO_MODF = 0.9f;
 private static final float DMG_WIZARD_MODF = 1.05f;
 private static final float DMG_LAND_MODF = 1.25f;

 private static final int BASE_DMG_IGNITE = 150;
 private static final int BASE_DMG_IGNITE_PLUS = 20;
 private static final int DMG_IGNITE = 50;
 private static final int DMG_IGNITE_PLUS = 30;
 private static final int DMG_RUNDE = 2;

 public Pyromancer() {
  super();
 }

 public Pyromancer(final int lin, final int col, final char tip) {
  super(lin, col, tip);
 }

 public final float trei(final Hero victima, final char land) {

 float dmg;

 dmg = DMG_FIRE + DMG_FIRE_PLUS * victima.getLevel();
 if (land == 'V') {
  dmg = dmg * DMG_LAND_MODF;
 }

 return Math.round(dmg);
}

public final float patru(final Hero victima, final char land) {

 float basedmg;
 float dmg;

 basedmg = BASE_DMG_IGNITE + BASE_DMG_IGNITE_PLUS * victima.getLevel();

 dmg = Math.round(basedmg);
 if (land == 'V') {
  dmg = dmg * DMG_LAND_MODF;
 }
 return Math.round(dmg);
}

public final float unu(final Hero atacant, final Hero victima, final char land) {

 float damage = 0;

 damage = DMG_FIRE + DMG_FIRE_PLUS * atacant.getLevel();

 if (land == 'V') {
  damage = damage * DMG_LAND_MODF;
 }
 damage = Math.round(damage);

 if (victima.getTip() == 'R') {
  damage = damage * DMG_ROGUE_MODF;
} else if (victima.getTip() == 'K') {
  damage = damage * DMG_KNIGHT_MODF;
} else if (victima.getTip() == 'P') {
  damage = damage * DMG_PYRO_MODF;
} else if (victima.getTip() == 'W') {
  damage = damage * DMG_WIZARD_MODF;
}

 return Math.round(damage);
}

public final float doi(final Hero atacant, final Hero victima, final char land) {

 float damage = 0;
 float basedmg = 0;
 float dmgr = 0;

 basedmg = BASE_DMG_IGNITE + BASE_DMG_IGNITE_PLUS * atacant.getLevel();
 dmgr = DMG_IGNITE + DMG_IGNITE_PLUS * atacant.getLevel();

 if (land == 'V') {
  basedmg = basedmg * DMG_LAND_MODF;
  dmgr = dmgr * DMG_LAND_MODF;
 }

 basedmg = Math.round(basedmg);
 dmgr = Math.round(dmgr);

 if (victima.getTip() == 'R') {
  basedmg = basedmg * DMG_ROGUE_MODF;
  dmgr = dmgr * DMG_ROGUE_MODF;
 } else if (victima.getTip() == 'K') {
   basedmg = basedmg * DMG_KNIGHT_MODF;
   dmgr = dmgr * DMG_KNIGHT_MODF;
 } else if (victima.getTip() == 'P') {
   basedmg = basedmg * DMG_PYRO_MODF;
   dmgr = dmgr * DMG_PYRO_MODF;
 } else if (victima.getTip() == 'W') {
   basedmg = basedmg * DMG_WIZARD_MODF;
   dmgr = dmgr * DMG_WIZARD_MODF;
 }

 //SET DAMAGE OVERT TIME
 dmgr = Math.round(dmgr);
 victima.setDmgOverTime((int) dmgr);
 victima.setRundeDmgOverTime(DMG_RUNDE);
 damage = Math.round(basedmg);

 return Math.round(damage);
}
}
