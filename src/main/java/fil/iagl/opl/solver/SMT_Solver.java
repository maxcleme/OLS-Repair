package fil.iagl.opl.solver;

import java.io.File;

import org.smtlib.SMT;

import fil.iagl.opl.repair.Fixer;
import fr.inria.lille.commons.synthesis.CodeGenesis;
import fr.inria.lille.commons.synthesis.smt.solver.SolverFactory;
import fr.inria.lille.repair.common.config.Config;
import spoon.Launcher;

public class SMT_Solver extends SMT {

  public static CodeGenesis patch;

  public static Boolean useBlackBox = Boolean.TRUE;

  public static void main(String[] args) {
    Config.INSTANCE.setSolverPath("C:/Users/RMS/Downloads/z3-4.3.2-x64-win/z3-4.3.2-x64-win/bin/z3.exe");
    SolverFactory.setSolver(Config.INSTANCE.getSolver(), Config.INSTANCE.getSolverPath());

    String projectPath = "C:/workspace/IntroClassJava_smt/dataset/median";
    // String projectPath = "C:/workspace/IntroClassJava_smt/dataset/smallest";
    File projectDir = new File(projectPath);

    String[] spoonArgsModeling = {
      "-i",
      projectPath,
      "-p",
      "fil.iagl.opl.instrument.ConstructModel",
      "--with-imports",
      "-x"
    };

    String[] spoonArgsApplyingPatch = {
      "-i",
      projectPath + "/src/main/java",
      "-o",
      projectPath + "/src/main/java",
      "-p",
      "fil.iagl.opl.repair.ApplyingPatch",
      "--with-imports",
      "-x"
    };

    try {
      Launcher.main(spoonArgsModeling);
      if (SMT_Solver.patch.isSuccessful()) {
        Fixer.cleanFolder(projectDir);
        Launcher.main(spoonArgsApplyingPatch);
        Fixer.generateReport(projectDir);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
