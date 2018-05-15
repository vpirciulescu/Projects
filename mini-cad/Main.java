import minicad.FactoryPattern;

import java.io.IOException;

public final class Main {

private Main() {

}

public static void main(final String[] args) throws IOException {

 FactoryPattern pic = new FactoryPattern();

 pic.deseneaza(args[0]);

}
}
