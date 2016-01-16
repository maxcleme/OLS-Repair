package introclassJava;

import java.util.Scanner;

public class syllables {
  public Scanner scanner;

  public String output = "";

  public static void main(String[] args) throws Exception {
    syllables mainClass = new syllables();
    if ((args.length) > 0) {
      mainClass.scanner = new Scanner(args[0]);
    } else {
      mainClass.scanner = new Scanner(System.in);
    }
    mainClass.exec();
  }

  public static int str_size = 20;

  public void exec() throws Exception {
    System.out.println("Please enter a string > ");
    String input = scanner.nextLine();
    System.out.println("The number of syllables is " + syllables(input));
  }

  public int syllables(String param) {
    throw new AssertionError("It fails");
  }
}
