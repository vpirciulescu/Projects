//PIRCIULESCU VALENTINA - 323 CD
package main;

import java.io.IOException;

import joc.Fight;

public final class Main {

private Main() {
}

public static void main(final String[] args) throws IOException {

 Fight fight = new Fight(args[0]);
 fight.start();
 fight.end(args[1]);
}
}
