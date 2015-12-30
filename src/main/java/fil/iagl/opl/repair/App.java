package fil.iagl.opl.repair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Hello world!
 *
 */
public class App {

  private static final String JUNIT_JAR_PATH = "C:/Users/RMS/.m2/repository/junit/junit/4.11/junit-4.11.jar";
  private static final String ASPECTJ_PATH = "C:/Users/RMS/.m2/repository/org/aspectj/aspectjrt/1.8.7/aspectjrt-1.8.7.jar";

  private static final String INTROCLASS_DATASET_DIRECTORY_PATH = "D:/workspace/IntroClassJava"; // A mettre dans ce path la
                                                                                                 // obligatoirement

  private static URLClassLoader classLoader;

  private static URLClassLoader testClassLoader;

  private static File backupFolder;

  private static int globalTotalTest = 0;
  private static int globalTotalSucceed = 0;
  private static int projectWhiteBoxTotalTest = 0;
  private static int projectWhiteTotalSucceed = 0;

  public static void main(final String[] args) throws IOException {
    final File introClassfile = new File(App.INTROCLASS_DATASET_DIRECTORY_PATH);
    final File introClassfilePatched = new File(App.INTROCLASS_DATASET_DIRECTORY_PATH + "_patched");

    // if (introClassfilePatched.exists())
    // FileUtils.forceDelete(introClassfilePatched);
    //
    // FileUtils.copyDirectory(introClassfile, introClassfilePatched);
    //
    // backupFolder = Files.createTempDirectory("backup_opl3_").toFile();

    Arrays.stream(new File(App.INTROCLASS_DATASET_DIRECTORY_PATH + File.separator + "dataset").listFiles()).filter(file -> file.isDirectory()).forEach(file -> {
      Arrays.stream(file.listFiles()).filter(subDir -> subDir.isDirectory()).forEach(subDir -> {
        App.runTest(subDir);
        // try { // TO BE REMOVE WITH SYSTEM.EXIT(0);
        // FileUtils.forceDelete(backupFolder);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        System.exit(0);
      });
    });

    FileUtils.forceDelete(backupFolder);
    System.out.printf("################# RESULT ################\n\tTest\t\t : %.2f%%\tSuccess :%d\tFailure : %d\n", ((float) App.globalTotalSucceed / App.globalTotalTest) * 100,
      App.globalTotalSucceed,
      (App.globalTotalTest - App.globalTotalSucceed));

  }

  private static void runTest(final File folder) {
    Arrays.stream(folder.listFiles())
      .filter(file -> file.isDirectory())
      .forEach(
        dir -> {
          File sourceFolder = new File(dir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "java");
          String pomLocation = dir.getAbsolutePath() + File.separatorChar + "pom.xml";
          try {
            MavenRunner.cleanCompileTest(pomLocation);
            MainPOC.hijack(sourceFolder.getAbsolutePath(), pomLocation);
            MavenRunner.cleanCompileTest(pomLocation);
          } catch (Exception e) {
            e.printStackTrace();
          }

          // Collection<File> classFiles = FileUtils.listFiles(file, new String[] {"class"}, true);
          // classFiles.forEach(classFile -> {
          // classFile.delete();
          // });
          //
          //// final String folderPath = file.getAbsolutePath();
          //// System.out.printf("Running test of %s\n", folderPath.substring(StringUtils.ordinalIndexOf(folderPath, File.separator,
          //// (folderPath.split(StringEscapeUtils.escapeJava(File.separator)).length - 1) - 2)));
          //// File sourceFolder = new File(folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java");
          //// File testFolder = new File(folderPath + File.separator + "src" + File.separator + "test" + File.separator + "java");
          ////
          //// Arrays.stream(sourceFolder.listFiles()).forEach(packageDir -> {
          //// App.loadClass(packageDir.getAbsolutePath(), false, false);
          //// });
          ////
          //// Arrays.stream(testFolder.listFiles()).forEach(packageDir -> {
          //// App.loadClass(packageDir.getAbsolutePath(), true, true);
          //// });
          ////
          //// Collection<File> classFiles = FileUtils.listFiles(file, new String[] {"class"}, true);
          //// classFiles.forEach(classFile -> {
          //// classFile.delete();
          //// });
          //
          // try {
          // MainPOC.hijack(sourceFolder.getAbsolutePath());
          // System.out.println("____");
          // } catch (Exception e) {
          // e.printStackTrace();
          // }

          // Arrays.stream(sourceFolder.listFiles()).forEach(packageDir -> {
          // App.loadClass(packageDir.getAbsolutePath(), false, false);
          // });
          //
          // Arrays.stream(testFolder.listFiles()).forEach(packageDir -> {
          // App.loadClass(packageDir.getAbsolutePath(), true, true);
          // });

          // App.loadClass(folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "aop",
          // false, false);
          // App.loadClass(folderPath + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator +
          // "introclassJava", true, true);

          // classFiles = FileUtils.listFiles(file, new String[] {"class"}, true);
          // classFiles.forEach(classFile -> {
          // classFile.delete();
          // });

          // float successRate = ((float) App.projectWhiteTotalSucceed / App.projectWhiteBoxTotalTest);
          // if (successRate < 1) {
          //
          // System.out.printf("Trying to improve Whitebox...\n");
          // System.out.printf("%.2f%%...\n", successRate * 100);
          // // while (successRate < 1) {
          // App.projectWhiteBoxTotalTest = 0;
          // App.projectWhiteTotalSucceed = 0;
          //
          // classFiles = FileUtils.listFiles(file, new String[] {"class"}, true);
          // classFiles.forEach(classFile -> {
          // classFile.delete();
          // });
          //
          // try {
          // // backup(folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator);
          // MainPOC.hijack(folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator);
          //
          // // RERUN
          // App.loadClass(folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator +
          // "introclassJava", false, false);
          // App.loadClass(folderPath + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator +
          // "introclassJava", true, false);
          //
          // if (((float) App.projectWhiteTotalSucceed / App.projectWhiteBoxTotalTest) > successRate) {
          // System.out.printf("%.2f%%\n", ((float) App.projectWhiteTotalSucceed / App.projectWhiteBoxTotalTest) * 100);
          // successRate = ((float) App.projectWhiteTotalSucceed / App.projectWhiteBoxTotalTest);
          // }
          //
          // // restore(folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator);
          // } catch (Exception e) {
          // System.out.println("Exception occured during transformation");
          // throw new RuntimeException(e);
          // }
          //
          // // }
          // } else {
          // System.out.println("No need to improve");
          // }

          App.projectWhiteBoxTotalTest = 0;
          App.projectWhiteTotalSucceed = 0;

        });

  }

  private static void restore(String string) throws IOException {
    FileUtils.cleanDirectory(new File(string));
    FileUtils.copyDirectory(backupFolder, new File(string));
  }

  private static void backup(String string) throws IOException {
    FileUtils.cleanDirectory(backupFolder);
    FileUtils.copyDirectory(new File(string), backupFolder);
  }

  private static void loadClass(final String folderPath, final boolean runTest, final boolean display) {
    final File sourceDir = new File(folderPath);
    Arrays.stream(sourceDir.listFiles()).filter(sourceFile -> sourceFile.getAbsolutePath().endsWith(".java")).forEach(sourceFile -> {
      try {
        App.compile(sourceFile, runTest, display);
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private static void compile(final File sourceFile, final boolean runTest, final boolean display)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
    // System.out.printf("\tCompiling\t : %s\n",
    // sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separator)).substring(1));

    // Compile source file.
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    compiler.run(null, null, null, "-source", "8", "-cp",
      App.JUNIT_JAR_PATH + File.pathSeparatorChar + App.ASPECTJ_PATH
        + File.pathSeparatorChar
        + new File(sourceFile.getAbsolutePath().substring(0, StringUtils.ordinalIndexOf(sourceFile.getAbsolutePath(), File.separator, 8)) + File.separator + "main"
          + File.separator + "java" + File.separator).getAbsolutePath(),
      sourceFile.getPath());

    if (runTest) {
      App.testClassLoader = URLClassLoader.newInstance(
        new URL[] {new File(sourceFile.getAbsolutePath().substring(0, StringUtils.ordinalIndexOf(sourceFile.getAbsolutePath(), File.separator, 10))).toURI().toURL()},
        App.classLoader);

      final Class<?> cls = Class.forName(
        "introclassJava." + sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separator)).substring(1).replace(".java", ""), true,
        App.testClassLoader);
      final JUnitCore coreRunner = new JUnitCore();
      final Result r = coreRunner.run(cls);

      final int totalTestForThisClass = r.getRunCount() - r.getIgnoreCount();
      final int totalSucceedForThisClass = totalTestForThisClass - r.getFailureCount();
      final float successRate = (((float) totalSucceedForThisClass) / totalTestForThisClass) * 100;

      if (display)
        System.out.printf("%s\tTest\t\t : %.2f%%\tSuccess :%d\tFailure : %d\n",
          sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separator)).substring(1).replace(".java", ""), successRate, totalSucceedForThisClass,
          totalTestForThisClass - totalSucceedForThisClass);

      App.globalTotalTest += totalTestForThisClass;
      App.globalTotalSucceed += totalSucceedForThisClass;

      if (sourceFile.getAbsolutePath().contains("Whitebox")) {
        App.projectWhiteBoxTotalTest += totalTestForThisClass;
        App.projectWhiteTotalSucceed += totalSucceedForThisClass;
      }

      App.testClassLoader.close();
    } else {
      App.classLoader = URLClassLoader.newInstance(new URL[] {new File(sourceFile.getAbsolutePath()
        .substring(0, StringUtils.ordinalIndexOf(sourceFile.getAbsolutePath(), File.separator, 10))).toURI().toURL()});
    }
  }
}
