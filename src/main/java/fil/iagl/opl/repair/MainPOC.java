package fil.iagl.opl.repair;

import java.io.File;

import spoon.Launcher;

public class MainPOC {

  public static void improve(String sourceFolderPath) throws Exception {

    String[] spoonArgs = {
      "-i",
      sourceFolderPath,
      "-o",
      sourceFolderPath,
      "-p", "fil.iagl.opl.repair.SwitchOperatorAssignment" + File.pathSeparator + "fil.iagl.opl.repair.SwitchMethodCall",
      "--with-imports"
    };

    Launcher.main(spoonArgs);

  }

  public static void main(String[] args) throws Exception {

    String[] spoonArgs = {
      "-i",
      "C:/workspace/IntroClassJava/dataset/checksum/08c7ea4ac39aa6a5ab206393bb4412de9d2c365ecdda9c1b391be963c1811014ed23d2722d7433b8e8a95305eee314d39da4950f31e01f9147f90af91a5c433a/006/src/main/java",
      "-p", "fil.iagl.opl.repair.SwitchOperatorAssignment" + File.pathSeparator + "fil.iagl.opl.repair.SwitchMethodCall",
      "--with-imports"
    };

    Launcher.main(spoonArgs);

  }

}
