package minicad;

public class Imagine implements Visitor {

    public final void visit(final Fundal f) {
      f.draw();
    }
    public final void visit(final Linie l) {
      l.draw();
    }
    public final void visit(final Patrat p) {
      p.draw();
    }
    public final void visit(final Dreptunghi d) {
      d.draw();
    }
    public final void visit(final Cerc c) {
      c.draw();
    }
    public final void visit(final Triunghi t) {
      t.draw();
    }
    public final void visit(final Romb r) {
      r.draw();
    }
    public final void visit(final Poligon pol) {
      pol.draw();
    }
}
