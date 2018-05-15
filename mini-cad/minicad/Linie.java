package minicad;

public final class Linie extends Shape implements Visitable {

    private int xi;
    private int yi;
    private int xf;
    private int yf;
    private int pixel;
    private static final int SHIFT = 24;
    private static final int LIM = 7;
    private static final int BASE = 16;
    private static final int GET1 = 3;
    private static final int GET2 = 4;
    private static final int GET3 = 5;
    private static final int GET4 = 6;

    private static Linie linie = new Linie();

    private Linie() {

    }

    public void setUp(final String info) {

      String[] date;
      date = info.split(" ");
      this.xi = Integer.parseInt(date[1]);
      this.yi = Integer.parseInt(date[2]);
      this.xf = Integer.parseInt(date[GET1]);
      this.yf = Integer.parseInt(date[GET2]);
      this.pixel = (Integer.parseInt(date[GET4]) << SHIFT)
           | (Integer.valueOf(date[GET3].substring(1, LIM), BASE));
    }

    public static Linie getInstance() {
       return linie;
    }

   public static void drawLinie(final int xi, final int yi, final int xf,
         final int yf, final int cul) {

     int x;
     int y;
     int deltax;
     int deltay;
     int s1;
     int s2;
     int aux;
     boolean swap = false;

     x = xi;
     y = yi;
     deltax = Math.abs(xf - xi);
     deltay = Math.abs(yf - yi);
     s1 = xf > xi ? 1 : -1;
     s2 = yf > yi ? 1 : -1;

     if (xi == xf) {
       s1 = 0;
     }
     if (yi == yf) {
        s2 = 0;
     }

     if (deltay > deltax) {
       aux = deltay;
       deltay = deltax;
       deltax = aux;
       swap = true;
      }

    int error = 2 * deltay - deltax;

    for (int i = 0; i < deltax + 1; i++) {
      if ((x < FactoryPattern.image.getWidth())
     && (y < FactoryPattern.image.getHeight()) && (x >= 0) && (y >= 0)) {
         FactoryPattern.image.setRGB(x, y, cul);
      }

      while (error > 0) {

        if (swap) {
         x = x + s1;
        } else {
         y = y + s2;
        }
         error = error - 2 * deltax;
      }

        if (swap) {
           y = y + s2;
        } else {
           x = x + s1;
        }

        error = error + 2 * deltay;
    }
   }

    public void draw() {

      drawLinie(this.xi, this.yi, this.xf, this.yf, this.pixel);
    }

    public void accept(final Visitor v) {
      v.visit(this);
    }
}
