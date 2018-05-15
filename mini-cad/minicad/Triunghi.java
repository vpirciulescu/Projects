package minicad;

public final class Triunghi extends Shape implements Visitable {

   private int x1;
   private int y1;
   private int x2;
   private int y2;
   private int x3;
   private int y3;
   private int colint;
   private int colext;
   private static final int SHIFT = 24;
   private static final int LIM = 7;
   private static final int BASE = 16;
   private static final int GET1 = 3;
   private static final int GET2 = 4;
   private static final int GET3 = 5;
   private static final int GET4 = 6;
   private static final int GET5 = 8;

   private static Triunghi triunghi = new Triunghi();

   private Triunghi() {

   }

   public void setUp(final String info) {

      String[] date;
      date = info.split(" ");
      this.x1 = Integer.parseInt(date[1]);
      this.y1 = Integer.parseInt(date[2]);
      this.x2 = Integer.parseInt(date[GET1]);
      this.y2 = Integer.parseInt(date[GET2]);
      this.x3 = Integer.parseInt(date[GET3]);
      this.y3 = Integer.parseInt(date[GET4]);
      this.colext = (Integer.parseInt(date[GET5]) << SHIFT)
              | (Integer.valueOf(date[LIM].substring(1, LIM), BASE));
      this.colint = (Integer.parseInt(date[GET5 + 2]) << SHIFT)
              | (Integer.valueOf(date[GET5 + 1].substring(1, LIM), BASE));

   }

   public static Triunghi getInstance() {
      return triunghi;
   }

   public void draw() {

      //contur
      Linie.drawLinie(this.x1, this.y1, this.x2, this.y2, this.colext);
      Linie.drawLinie(this.x2, this.y2, this.x3, this.y3, this.colext);
      Linie.drawLinie(this.x3, this.y3, this.x1, this.y1, this.colext);

      int xg;
      int yg;
      xg = (this.x1 + this.x2 + this.x3) / GET1;
      yg = (this.y1 + this.y2 + this.y3) / GET1;
      Cerc.fill(xg, yg, this.colint, this.colext);
   }

   public void accept(final Visitor v) {
      v.visit(this);
   }

}
