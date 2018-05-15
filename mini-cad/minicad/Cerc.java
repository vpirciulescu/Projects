package minicad;

import java.util.LinkedList;
import java.util.Queue;
import java.awt.Point;


public final class Cerc extends Shape implements Visitable {

  private int x;
  private int y;
  private int raza;
  private int colint;
  private int colext;
  private static Cerc cerc = new Cerc();
  private static final int SHIFT = 24;
  private static final int LIM = 7;
  private static final int BASE = 16;
  private static final int GET1 = 3;
  private static final int GET2 = 4;
  private static final int GET3 = 5;
  private static final int GET4 = 6;
  private static final int GET5 = 10;

  private Cerc() {

  }

   public void setUp(final String info) {

     String[] date;
     date = info.split(" ");
     this.x = Integer.parseInt(date[1]);
     this.y = Integer.parseInt(date[2]);
     this.raza = Integer.parseInt(date[GET1]);
     this.colext = (Integer.parseInt(date[GET3]) << SHIFT)
             | (Integer.valueOf(date[GET2].substring(1, LIM), BASE));
     this.colint = (Integer.parseInt(date[LIM]) << SHIFT)
             | (Integer.valueOf(date[GET4].substring(1, LIM), BASE));
   }

   public static Cerc getInstance() {
     return cerc;
   }

   public static void drawCircle(final int xc, final int yc, final int cul) {

      if ((xc < FactoryPattern.image.getWidth()) && (yc < FactoryPattern.image.getHeight())
          && (xc >= 0) && (yc >= 0)) {
          FactoryPattern.image.setRGB(xc, yc, cul);

      }
   }

   public static void contur(final int a, final int b, final int r, final int cul) {

     int p;
     p = 0;
     int q;
     int d;
     q = r;
     d = GET1 - 2 * r;

     while (q >= p) {

       drawCircle(a + p, b + q, cul);
       drawCircle(a - p, b + q, cul);
       drawCircle(a + p, b - q, cul);
       drawCircle(a - p, b - q, cul);

       drawCircle(a + q, b + p, cul);
       drawCircle(a - q, b + p, cul);
       drawCircle(a + q, b - p, cul);
       drawCircle(a - q, b - p, cul);

       p++;

       if (d <= 0) {
         d = d + GET2 * p + GET4;
       } else {
          q--;
          d = d + GET2 * (p - q) + GET5;
        }
       drawCircle(a + p, b + q, cul);
       drawCircle(a - p, b + q, cul);
       drawCircle(a + p, b - q, cul);
       drawCircle(a - p, b - q, cul);

       drawCircle(a + q, b + p, cul);
       drawCircle(a - q, b + p, cul);
       drawCircle(a + q, b - p, cul);
       drawCircle(a - q, b - p, cul);

   }
  }

   public static boolean valid(final int x, final int y, final int c) {

   if ((x >= FactoryPattern.image.getWidth())
    || (y >= FactoryPattern.image.getHeight()) || (x < 0) || (y < 0)) {

        return false;
       }
   if (FactoryPattern.image.getRGB(x, y) == c) {
      return false;
   }
       return true;
  }

   public static void fill(final int xc, final int yc, final int cint, final int cext) {

      Queue<Point> queue = new LinkedList<Point>();
       int lin = FactoryPattern.image.getHeight();
       int col = FactoryPattern.image.getWidth();
       int culoare;
       boolean[][] painted = new boolean[col][lin];
       queue.add(new Point(xc, yc));

       while (!queue.isEmpty()) {
         Point p = queue.remove();
         culoare = FactoryPattern.image.getRGB(p.x, p.y);

         if (p.y <= FactoryPattern.image.getHeight()  && p.x <= FactoryPattern.image.getWidth()) {
          if ((culoare != cext) && (!painted[p.x][p.y])) {

             FactoryPattern.image.setRGB(p.x, p.y, cint);
             painted[p.x][p.y] = true;

             if (valid(p.x - 1, p.y, cext)) {
                queue.add(new Point(p.x - 1, p.y));
              }
             if (valid(p.x + 1, p.y, cext)) {
                queue.add(new Point(p.x + 1, p.y));
              }
             if (valid(p.x, p.y - 1, cext)) {
                queue.add(new Point(p.x, p.y - 1));
           }
          if (valid(p.x, p.y + 1, cext)) {
                queue.add(new Point(p.x, p.y + 1));
              }
           }
         }
       }
  }

     public void draw() {

        contur(this.x, this.y, this.raza, this.colext);

        fill(this.x, this.y, this.colint, this.colext);
     }

   public void accept(final Visitor v) {
      v.visit(this);
   }
}
