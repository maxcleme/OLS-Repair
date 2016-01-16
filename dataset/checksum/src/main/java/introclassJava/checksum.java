package introclassJava;

import java.util.Scanner;

public class checksum {
  public Scanner scanner;

  public String output = "";

  public static void main(String[] args) throws Exception {
    checksum mainClass = new checksum();
    if ((args.length) > 0) {
      mainClass.scanner = new Scanner(args[0]);
    } else {
      mainClass.scanner = new Scanner(System.in);
    }
    mainClass.exec();
  }

  public void exec() throws Exception {
    String input;
    System.out.println("Enter an abitrarily long string, ending with carriage return > ");
    input = scanner.next();
    System.out.println("Check sum is " + checksum(input));
  }

  public char checksum(String input) {
    throw new AssertionError("It fails");
  }
}
