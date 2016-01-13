package fil.iagl.opl.solver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.smtlib.SMT;

import fil.iagl.opl.instrument.InputCollector;
import fil.iagl.opl.repair.Fixer;
import fr.inria.lille.commons.synthesis.CodeGenesis;
import fr.inria.lille.commons.synthesis.ConstraintBasedSynthesis;
import fr.inria.lille.commons.synthesis.smt.solver.SolverFactory;
import fr.inria.lille.repair.common.config.Config;
import spoon.Launcher;

public class SMT_Solver extends SMT {

  public static void main(String[] args) {
    Config.INSTANCE.setSolverPath("C:/Users/RMS/Downloads/z3-4.3.2-x64-win/z3-4.3.2-x64-win/bin/z3.exe");
    SolverFactory.setSolver(Config.INSTANCE.getSolver(), Config.INSTANCE.getSolverPath());

    String projectPath = "C:/Users/RMS/Documents/workspace-sts-3.7.0.RELEASE/sample";
    File projectDir = new File(projectPath);

    String[] spoonArgsModeling = {
      "-i",
      projectPath,
      "-p",
      "fil.iagl.opl.instrument.ConstructModel",
      "--with-imports",
      "-x"
    };

    try {
      Launcher.main(spoonArgsModeling);
      Fixer.generateReport(projectDir);
    } catch (Exception e) {
      e.printStackTrace();
    }

    InputCollector.printSpec();

    Map<String, Integer> intConstants = new HashMap<>();
    intConstants.put("-1", -1);
    intConstants.put("0", 0);
    intConstants.put("1", 1);

    ConstraintBasedSynthesis synthesis = new ConstraintBasedSynthesis(intConstants);
    CodeGenesis genesis = synthesis.codesSynthesisedFrom(
      (Integer.class), InputCollector.getSpecs());
    System.out.println(genesis.returnStatement());
  }

}
