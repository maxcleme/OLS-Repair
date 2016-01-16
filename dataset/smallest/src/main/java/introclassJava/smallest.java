package introclassJava;

import java.util.Scanner;

public class smallest {
  public Scanner scanner;

  public String output = "";

  public static void main(String[] args) throws Exception {
    smallest mainClass = new smallest();
    if ((args.length) > 0) {
      mainClass.scanner = new Scanner(args[0]);
    } else {
      mainClass.scanner = new Scanner(System.in);
    }
    mainClass.exec();
  }

  public void exec() throws Exception {
    int[] input = new int[4];
    System.out.println("Please enter 4 numbers separated by spaces > ");
    input[0] = scanner.nextInt();
    input[1] = scanner.nextInt();
    input[2] = scanner.nextInt();
    input[3] = scanner.nextInt();
    System.out.println(((smallest(input)) + " is the smallest"));
  }

  public int smallest(int[] param) {
    throw new AssertionError("It fails");
  }
}
