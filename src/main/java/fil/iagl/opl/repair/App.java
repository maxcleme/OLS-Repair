package fil.iagl.opl.repair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Hello world!
 *
 */
public class App {

  private static final String JUNIT_JAR_PATH = "C:/Users/RMS/.m2/repository/junit/junit/4.11/junit-4.11.jar";
  private static final String INTROCLASS_DATASET_DIRECTORY_PATH = "C:/workspace/IntroClassJava/dataset";
  private static URLClassLoader classLoader;
  private static URLClassLoader testClassLoader;

  private static int totalTest = 0;
  private static int totalSucceed = 0;

  public static void main(String[] args) {
    File introClassfile = new File(INTROCLASS_DATASET_DIRECTORY_PATH);
    Arrays.stream(introClassfile.listFiles()).filter(file -> file.isDirectory()).forEach(file -> {
      Arrays.stream(file.listFiles()).filter(subDir -> subDir.isDirectory()).forEach(subDir -> {
        runTest(subDir);
      });
    });

    System.out.printf("################# RESULT ################\n\tTest\t\t : %.2f%%\tSuccess :%d\tFailure : %d\n", ((float) totalSucceed / totalTest) * 100, totalSucceed,
      (totalTest - totalSucceed));

  }

  private static void runTest(File folder) {
    Arrays.stream(folder.listFiles()).filter(file -> file.isDirectory()).forEach(file -> {
      String folderPath = file.getAbsolutePath();
      System.out.printf("Running test of %s\n",
        folderPath.substring(StringUtils.ordinalIndexOf(folderPath, File.separator, (folderPath.split(StringEscapeUtils.escapeJava(File.separator)).length - 1) - 2)));
      loadClass(folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "introclassJava", false);
      loadClass(folderPath + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator + "introclassJava", true);
    });

  }

  private static void loadClass(String folderPath, boolean runTest) {
    File sourceDir = new File(folderPath);
    Arrays.stream(sourceDir.listFiles()).filter(sourceFile -> sourceFile.getAbsolutePath().endsWith(".java")).forEach(sourceFile -> {
      try {
        compile(sourceFile, runTest);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

  }

  private static void compile(File sourceFile, boolean runTest) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
    // System.out.printf("\tCompiling\t : %s\n",
    // sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separator)).substring(1));

    // Compile source file.
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    compiler.run(null, null, null, "-source", "8", "-cp",
      JUNIT_JAR_PATH + File.pathSeparatorChar
        + new File(sourceFile.getAbsolutePath().substring(0, StringUtils.ordinalIndexOf(sourceFile.getAbsolutePath(), File.separator, 8)) + File.separator + "main" + File.separator
          + "java" + File.separator).getAbsolutePath(),
      sourceFile.getPath());

    if (runTest) {
      testClassLoader = URLClassLoader
        .newInstance(new URL[] {new File(sourceFile.getAbsolutePath().substring(0, StringUtils.ordinalIndexOf(sourceFile.getAbsolutePath(), File.separator, 10))).toURI().toURL()},
          classLoader);

      Class<?> cls = Class.forName(
        "introclassJava." + sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separator)).substring(1).replace(".java", ""), true,
        testClassLoader);
      JUnitCore coreRunner = new JUnitCore();
      Result r = coreRunner.run(cls);

      int totalTestForThisClass = r.getRunCount() - r.getIgnoreCount();
      int totalSucceedForThisClass = totalTestForThisClass - r.getFailureCount();
      float successRate = (((float) totalSucceedForThisClass) / totalTestForThisClass) * 100;

      System.out.printf("\tTest\t\t : %.2f%%\tSuccess :%d\tFailure : %d\n", successRate, totalSucceedForThisClass, totalTestForThisClass - totalSucceedForThisClass);

      App.totalTest += totalTestForThisClass;
      App.totalSucceed += totalSucceedForThisClass;

      testClassLoader.close();
    } else {
      classLoader = URLClassLoader
        .newInstance(new URL[] {new File(sourceFile.getAbsolutePath().substring(0, StringUtils.ordinalIndexOf(sourceFile.getAbsolutePath(), File.separator, 10))).toURI().toURL()});
    }
  }
}
