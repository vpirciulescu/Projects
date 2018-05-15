package minicad;

public interface Visitor {

   void visit(Fundal f);
   void visit(Linie l);
   void visit(Patrat p);
   void visit(Dreptunghi d);
   void visit(Cerc c);
   void visit(Triunghi t);
   void visit(Romb r);
   void visit(Poligon pol);
}
