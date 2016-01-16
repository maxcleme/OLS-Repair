package introclassJava;

import java.util.Scanner;

public class digits {
  public Scanner scanner;

  public String output = "";

  public static void main(String[] args) throws Exception {
    digits mainClass = new digits();
    if ((args.length) > 0) {
      mainClass.scanner = new Scanner(args[0]);
    } else {
      mainClass.scanner = new Scanner(System.in);
    }
    mainClass.exec();
  }

  public void exec() throws Exception {
    String input;
    System.out.println("Enter an integer > ");
    input = scanner.nextLine();
    System.out.println(digits(input) + " That's all, have a nice day!");
  }

  public String digits(String param) {
    throw new AssertionError("It fails");
  }
}
