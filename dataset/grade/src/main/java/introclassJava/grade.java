package introclassJava;

import java.util.Scanner;

public class grade {
  public Scanner scanner;

  public String output = "";

  public static void main(String[] args) throws Exception {
    grade mainClass = new grade();
    if ((args.length) > 0) {
      mainClass.scanner = new Scanner(args[0]);
    } else {
      mainClass.scanner = new Scanner(System.in);
    }
    mainClass.exec();
  }

  public void exec() throws Exception {
    int[] input = new int[5];
    System.out.println("Enter thresholds for A, B, C, D in that order, decreasing percentages > ");
    input[0] = scanner.nextInt();
    input[1] = scanner.nextInt();
    input[2] = scanner.nextInt();
    input[3] = scanner.nextInt();
    System.out.println("Thank you. Now enter student score (percent) > ");
    input[4] = scanner.nextInt();
    System.out.println(grade(input));
  }

  public String grade(int[] param) {
    throw new AssertionError("It fails");
  }
}
