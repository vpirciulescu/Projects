package minicad;

import java.awt.image.BufferedImage;
//import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.OutputStreamWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class FactoryPattern {

 public FactoryPattern() {

 }
 public static BufferedImage image;
 public static Scanner input;

 public final void deseneaza(final String inputFile) throws IOException {

  String filename = "drawing.png";
  Scanner input = new Scanner(new File(inputFile));
  File f = new File(filename);

  int n;
  n = input.nextInt();
  String s;
  s = input.nextLine();
  s = input.nextLine();

  int inaltime = 0;
  int latime = 0;
  String[] ss;
  ss = s.split(" ");
  inaltime = Integer.parseInt(ss[1]);
  latime = Integer.parseInt(ss[2]);

  image = new BufferedImage(latime, inaltime, BufferedImage.TYPE_INT_ARGB);
  ShapeFactory paint = new ShapeFactory();
  Shape sh = new Shape();
  Imagine img = new Imagine();
  sh = paint.getShape(s);
  sh.accept(img);

  for (int i = 0; i < n - 1; i++) {

   s = input.nextLine();
   sh = paint.getShape(s);
   sh.accept(img);
    }

ImageIO.write(image, "PNG", f);
input.close();

}
}
