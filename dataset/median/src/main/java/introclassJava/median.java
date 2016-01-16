package introclassJava;

import java.util.Scanner;

public class median {
  public Scanner scanner;

  public String output = "";

  public static void main(String[] args) throws Exception {
    median mainClass = new median();
    if ((args.length) > 0) {
      mainClass.scanner = new Scanner(args[0]);
    } else {
      mainClass.scanner = new Scanner(System.in);
    }
    mainClass.exec();
  }

  public void exec() throws Exception {
    int[] input = new int[3];
    System.out.println("Please enter 3 numbers separated by spaces > ");
    input[0] = scanner.nextInt();
    input[1] = scanner.nextInt();
    input[2] = scanner.nextInt();
    System.out.println(((median(input)) + " is the median"));
  }

  public int median(int[] param) {
    throw new AssertionError("It fails");
  }
}
