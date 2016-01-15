package fil.iagl.opl;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.smtlib.SMT;

import fil.iagl.opl.utils.Utils;
import fr.inria.lille.commons.synthesis.CodeGenesis;
import fr.inria.lille.commons.synthesis.smt.solver.SolverFactory;
import fr.inria.lille.repair.common.config.Config;
import spoon.Launcher;

public class SMT_Solver extends SMT {

  public static final String JUNIT_JAR_PATH = "C:/Users/RMS/.m2/repository/junit/junit/4.11/junit-4.11.jar";

  public static CodeGenesis patch;

  public static Boolean useBlackBox = Boolean.FALSE;

  public static Boolean overrideFile = Boolean.FALSE;

  public static int[] constantsArray = {-1, 0, 1};

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
      "fil.iagl.opl.model.ConstructModel",
      "--with-imports",
      "-x"
    };

    try {
      Launcher.main(spoonArgsModeling);
      if (SMT_Solver.patch.isSuccessful()) {
        if (!overrideFile) {
          String patchedProjectPath = projectPath + "_patched";
          File patchedProjectDir = new File(patchedProjectPath);
          if (patchedProjectDir.exists()) {
            FileUtils.forceDelete(patchedProjectDir);
          }
          FileUtils.copyDirectory(projectDir, patchedProjectDir);
          projectPath = patchedProjectPath;
          projectDir = patchedProjectDir;
        }
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
        Utils.cleanFolder(projectDir);
        Launcher.main(spoonArgsApplyingPatch);
        Utils.generateReport(projectDir);
        System.out.println("Constant used : " + Arrays.toString(SMT_Solver.constantsArray));
        System.out.println("Patch written in : " + projectPath);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
