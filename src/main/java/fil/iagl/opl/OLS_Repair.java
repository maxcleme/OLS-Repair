package fil.iagl.opl;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.smtlib.SMT;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

import fil.iagl.opl.utils.Utils;
import fr.inria.lille.commons.synthesis.CodeGenesis;
import fr.inria.lille.commons.synthesis.smt.solver.SolverFactory;
import fr.inria.lille.repair.common.config.Config;
import spoon.Launcher;

public class OLS_Repair extends SMT {
  public static String PROJECT_PATH;
  public static String JUNIT_JAR_PATH;
  public static String Z3_PATH;
  public static Boolean USE_BLACK_BOX;
  public static Boolean OVERRIDE;
  public static Collection<Integer> CONSTANTS_ARRAY;

  public static CodeGenesis patch;

  public static void main(String[] args) {
    handleArgs(args);
    Config.INSTANCE.setSolverPath(Z3_PATH);
    SolverFactory.setSolver(Config.INSTANCE.getSolver(), Config.INSTANCE.getSolverPath());

    File projectDir = new File(PROJECT_PATH);

    String[] spoonArgsModeling = {
      "-i",
      PROJECT_PATH,
      "-p",
      "fil.iagl.opl.model.ConstructModel",
      "--with-imports",
      "-x"
    };

    try {
      Launcher.main(spoonArgsModeling);
      if (OLS_Repair.patch.isSuccessful()) {
        if (!OVERRIDE) {
          String patchedProjectPath = PROJECT_PATH + "_patched";
          File patchedProjectDir = new File(patchedProjectPath);
          if (patchedProjectDir.exists()) {
            FileUtils.forceDelete(patchedProjectDir);
          }
          FileUtils.copyDirectory(projectDir, patchedProjectDir);
          PROJECT_PATH = patchedProjectPath;
          projectDir = patchedProjectDir;
        }
        String[] spoonArgsApplyingPatch = {
          "-i",
          PROJECT_PATH + "/src/main/java",
          "-o",
          PROJECT_PATH + "/src/main/java",
          "-p",
          "fil.iagl.opl.repair.ApplyingPatch",
          "--with-imports",
          "-x"
        };
        Utils.cleanFolder(projectDir);
        Launcher.main(spoonArgsApplyingPatch);
        Utils.generateReport(projectDir);
        System.out.println("Constant used : " + OLS_Repair.CONSTANTS_ARRAY);
        System.out.println("Patch written in : " + PROJECT_PATH);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void handleArgs(String[] args) {
    CmdLineParser parse = new CmdLineParser();

    Option<String> sourcePath = parse.addStringOption('s', "source-path");
    Option<String> junitPath = parse.addStringOption('j', "junit-path");
    Option<String> z3Path = parse.addStringOption('z', "z3-path");
    Option<Integer> constants = parse.addIntegerOption('c', "add-constant");
    Option<Boolean> override = parse.addBooleanOption('o', "override");
    Option<Boolean> useBlackbox = parse.addBooleanOption('u', "use-blackbox");

    try {
      parse.parse(args);
    } catch (CmdLineParser.OptionException e) {
      System.err.println(e.getMessage());
      printUsage();
      System.exit(2);
    }

    PROJECT_PATH = parse.getOptionValue(sourcePath);
    JUNIT_JAR_PATH = parse.getOptionValue(junitPath);
    Z3_PATH = parse.getOptionValue(z3Path);
    OVERRIDE = parse.getOptionValue(override, false);
    USE_BLACK_BOX = parse.getOptionValue(useBlackbox, false);
    CONSTANTS_ARRAY = parse.getOptionValues(constants);

    if (PROJECT_PATH == null || JUNIT_JAR_PATH == null || Z3_PATH == null) {
      printUsage();
      System.exit(2);
    }

  }

  private static void printUsage() {
    System.err.println("Usage: OLS_Repair\n"
      + " -s, --source-path path_buggy_program\n"
      + " -j, --junit-path path_junit_jar\n"
      + " -z, --z3-path path_z3_executable\n"
      + " [-c, --constant one_constant_to_add]*\n"
      + " [-o, --override]\n"
      + " [-u, --use-blackbox]");
  }

}
