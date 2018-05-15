package minicad;

public final class Romb extends Shape implements Visitable {

   private int x;
   private int y;
   private int diago;
   private int diagv;
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

   private static Romb romb = new Romb();

   private Romb() {
   }

   public void setUp(final String info) {

      String[] date;
      date = info.split(" ");
      this.x = Integer.parseInt(date[1]);
      this.y = Integer.parseInt(date[2]);
      this.diago = Integer.parseInt(date[GET1]);
      this.diagv = Integer.parseInt(date[GET2]);
      this.colext = (Integer.parseInt(date[GET4]) << SHIFT)
              | (Integer.valueOf(date[GET3].substring(1, LIM), BASE));
      this.colint = (Integer.parseInt(date[GET5]) << SHIFT)
              | (Integer.valueOf(date[LIM].substring(1, LIM), BASE));
   }

   public static Romb getInstance() {
      return romb;
   }

   public void draw() {

      int xr;
      int yr;
      yr = this.diagv / 2;
      xr = this.diago / 2;

      Linie.drawLinie(this.x, this.y + yr, this.x + xr, this.y, this.colext);
      Linie.drawLinie(this.x, this.y - yr, this.x + xr, this.y, this.colext);
      Linie.drawLinie(this.x, this.y - yr, this.x - xr, this.y, this.colext);
      Linie.drawLinie(this.x, this.y + yr, this.x - xr, this.y, this.colext);

      //FILL
      Cerc.fill(this.x, this.y, this.colint, this.colext);
   }

   public void accept(final Visitor v) {
      v.visit(this);
   }
}
