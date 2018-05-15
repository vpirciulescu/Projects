package minicad;

public final class Dreptunghi extends Shape implements Visitable {

  private int x;
  private int y;
  private int h;
  private int l;
  private int colint;
  private int colext;
  private int a1;
  private int a2;
  private static final int SHIFT = 24;
  private static final int LIM = 7;
  private static final int BASE = 16;
  private static final int GET1 = 3;
  private static final int GET2 = 4;
  private static final int GET3 = 5;
  private static final int GET4 = 6;
  private static final int GET5 = 8;


 private static Dreptunghi dreptunghi = new Dreptunghi();

 private Dreptunghi() {
 }

 public void setUp(final String info) {

     String[] date;
     date = info.split(" ");

     this.x = Integer.parseInt(date[1]);
     this.y = Integer.parseInt(date[2]);
     this.h = Integer.parseInt(date[GET1]);
     this.l = Integer.parseInt(date[GET2]);
     this.a1 = Integer.parseInt(date[GET4]);
     this.a2 = Integer.parseInt(date[GET5]);
     this.colext = (a1 << SHIFT)
          | (Integer.valueOf(date[GET3].substring(1, LIM), BASE));
     this.colint = (a2 << SHIFT)
          | (Integer.valueOf(date[LIM].substring(1, LIM), BASE));
   }

 public static Dreptunghi getInstance() {
     return dreptunghi;
 }

 public void draw() {

   for (int i = 0; i < this.l; i++) {
     if (this.x + i < FactoryPattern.image.getWidth()) {
        FactoryPattern.image.setRGB(this.x + i, this.y, this.colext);

     if (this.y + this.h < FactoryPattern.image.getHeight()) {
        FactoryPattern.image.setRGB(this.x + i, this.y + this.h - 1, this.colext);
      }
     }
    }

   for (int i = 0; i < this.h; i++) {

    if (this.y + i  < FactoryPattern.image.getHeight()) {

     FactoryPattern.image.setRGB(this.x, this.y + i, this.colext);

     if (this.x + this.l < FactoryPattern.image.getWidth()) {
  FactoryPattern.image.setRGB(this.x + this.l - 1, this.y + i, this.colext);
   }
  }
 }

   for (int i = 1; i < this.h - 1; i++) {

      if (this.y + i < FactoryPattern.image.getHeight()) {
        for (int j = 1; j < this.l - 1; j++) {
         if (this.x + j < FactoryPattern.image.getWidth()) {
        FactoryPattern.image.setRGB(this.x + j, this.y + i, this.colint);
       }
      }
      }
     }
   }

    public void accept(final Visitor v) {
      v.visit(this);
   }
}
