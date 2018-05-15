package minicad;

public final class Fundal extends Shape implements Visitable {

    private int inaltime;
    private int latime;
    private int pixel;
    private static final int SHIFT = 24;
    private static final int LIM = 7;
    private static final int BASE = 16;
    private static final int GET1 = 3;
    private static final int GET2 = 4;

    private static Fundal fundal = new Fundal();

    private Fundal() {

    }

   public void setUp(final String info) {

      String[] date;
      date = info.split(" ");
      this.inaltime = Integer.parseInt(date[1]);
      this.latime = Integer.parseInt(date[2]);
      this.pixel = (Integer.parseInt(date[GET2]) << SHIFT)
             | (Integer.valueOf(date[GET1].substring(1, LIM), BASE));
   }

   public int getH() {
      return this.inaltime;
   }

   public int getL() {
      return this.latime;
   }

   public static Fundal getInstance() {
      return fundal;
   }

   public void draw() {

      for (int i = 0; i < this.latime; i++) {
       for (int j = 0; j < this.inaltime; j++) {
         FactoryPattern.image.setRGB(i, j, this.pixel);
        }
      }
   }

  public void accept(final Visitor v) {
      v.visit(this);
   }
}
