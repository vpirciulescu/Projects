package minicad;

import java.awt.Point;

public final class Poligon extends Shape implements Visitable {

   private Point[] puncte;
   private int nr;
   private int colext;
   private int colint;
   private static final int SHIFT = 24;
   private static final int LIM = 7;
   private static final int BASE = 16;
   private static final int GET1 = 3;

   private static Poligon poligon = new Poligon();

   private Poligon() {
   }

   public void setUp(final String info) {

      int xx;
      int yy;
      String[] date = info.split(" ");
      this.nr = Integer.parseInt(date[1]);
      this.puncte = new Point[this.nr + 1];
      int i;

      for (i = 1; i <= nr; i++) {

         //System.out.println(date[2*i] + " " + date[2*i+1]);

         xx = Integer.parseInt(date[2 * i]);
         yy = Integer.parseInt(date[2 * i + 1]);
         this.puncte[i] = new Point();
         this.puncte[i].setLocation(xx, yy);
      }
      i = 2 * i;
      this.colext = (Integer.parseInt(date[i + 1]) << SHIFT)
                  | (Integer.valueOf(date[i].substring(1, LIM), BASE));
      this.colint = (Integer.parseInt(date[i + GET1]) << SHIFT)
                  | (Integer.valueOf(date[i + 2].substring(1, LIM), BASE));

   }

   public static Poligon getInstance() {
      return poligon;
   }

   public void draw() {

      //contur
      int xi;
      int yi;
      int xf;
      int yf;
      int i;

      for (i = 1; i < this.nr; i++) {

         xi = this.puncte[i].x;
         yi = this.puncte[i].y;
         xf = this.puncte[i + 1].x;
         yf = this.puncte[i + 1].y;
         Linie.drawLinie(xi, yi, xf, yf, this.colext);

      }
      xf = this.puncte[1].x;
      yf = this.puncte[1].y;
      xi = this.puncte[i].x;
      yi = this.puncte[i].y;

      Linie.drawLinie(xi, yi, xf, yf, this.colext);
      //FILL
      int xg = 0;
      int yg = 0;
      for (i = 1; i <= this.nr; i++) {
         xg += this.puncte[i].x;
         yg += this.puncte[i].y;
      }
      xg /= this.nr;
      yg /= this.nr;
      Cerc.fill(xg, yg, this.colint, this.colext);

   }

   public void accept(final Visitor v) {
      v.visit(this);
   }
}
