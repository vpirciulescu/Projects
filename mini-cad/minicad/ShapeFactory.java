package minicad;

public class ShapeFactory {

   public ShapeFactory() {

   }
   public final Shape getShape(final String shape) {

      if (shape == null) {
         return null;
      }

      String[] spliting;
      spliting = shape.split(" ");
      String shapeType = spliting[0];

      if (shapeType.equalsIgnoreCase("CANVAS")) {
         Fundal f = Fundal.getInstance();
         f.setUp(shape);
         return f;

      } else if (shapeType.equalsIgnoreCase("SQUARE")) {
         Patrat p = Patrat.getInstance();
         p.setUp(shape);
         return p;

      } else if (shapeType.equalsIgnoreCase("LINE")) {
         Linie l = Linie.getInstance();
         l.setUp(shape);
         return l;
      } else if (shapeType.equalsIgnoreCase("RECTANGLE")) {
         Dreptunghi d = Dreptunghi.getInstance();
         d.setUp(shape);
         return d;
      } else if (shapeType.equalsIgnoreCase("CIRCLE")) {
         Cerc c = Cerc.getInstance();
         c.setUp(shape);
         return c;
      } else if (shapeType.equalsIgnoreCase("TRIANGLE")) {
         Triunghi t = Triunghi.getInstance();
         t.setUp(shape);
         return t;
      } else if (shapeType.equalsIgnoreCase("DIAMOND")) {
         Romb r = Romb.getInstance();
         r.setUp(shape);
         return r;
      } else if (shapeType.equalsIgnoreCase("POLYGON")) {
         Poligon pol = Poligon.getInstance();
         pol.setUp(shape);
         return pol;
      }
   return null;
   }
}
