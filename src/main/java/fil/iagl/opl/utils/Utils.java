package fil.iagl.opl.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.xml.sax.SAXException;

import fil.iagl.opl.OLS_Repair;

public class Utils {

  private static URLClassLoader classLoader;

  private static URLClassLoader testClassLoader;

  public static void generateReport(File projectDir) throws SAXException, IOException, ParserConfigurationException {
    File sourceFolder = new File(projectDir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "java");
    runAllTest(sourceFolder, true);
  }

  private static void compile(final File sourceFile, String... classpaths)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

    // Compile source file.
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    compiler.run(null, null, null, "-source", "8", "-cp",
      OLS_Repair.JUNIT_JAR_PATH
        + File.pathSeparatorChar
        + Arrays.stream(classpaths).collect(Collectors.joining(File.pathSeparator)),
      sourceFile.getPath());

  }

  private static boolean runAllTest(File sourceFolder, boolean withBlackbox) throws SAXException, IOException, ParserConfigurationException {
    String srcMainJavaPath = sourceFolder.getAbsolutePath();
    String srcTestJavaPath = sourceFolder.getAbsolutePath() + File.separatorChar + ".." + File.separatorChar + ".."
      + File.separatorChar + "test" + File.separatorChar + "java";

    // Compile every .java inside src/main/java
    FileUtils.listFiles(new File(srcMainJavaPath), new String[] {"java"}, true).forEach(sourceFile -> {
      try {
        Utils.compile(sourceFile, srcMainJavaPath, srcTestJavaPath);
        Utils.classLoader = URLClassLoader.newInstance(new URL[] {new File(srcMainJavaPath).toURI().toURL()});
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    });
    // Compile every .java src/test/java
    FileUtils.listFiles(new File(srcTestJavaPath), new String[] {"java"}, true).forEach(sourceFile -> {
      try {
        Utils.compile(sourceFile, srcMainJavaPath, srcTestJavaPath);
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Run test inside src/test/java
    boolean allPassed = true;
    for (File testFile : FileUtils.listFiles(new File(srcTestJavaPath), new String[] {"java"}, true)) {
      try {
        if (withBlackbox || !testFile.getAbsolutePath().contains("Blackbox")) {
          allPassed &= Utils.runTest(testFile, srcTestJavaPath);
        }
      } catch (ClassNotFoundException e) {
        allPassed = false;
      }
    }
    return allPassed;
  }

  public static void cleanFolder(File sourceFolder) {
    Collection<File> filesToBeDeleted = FileUtils.listFiles(sourceFolder, new String[] {"class"}, true);
    filesToBeDeleted.forEach(classFile -> {
      classFile.delete();
    });
  }

  private static boolean runTest(File sourceFile, String srcTestJavaPath) throws ClassNotFoundException, MalformedURLException {
    Utils.testClassLoader = URLClassLoader.newInstance(
      new URL[] {new File(srcTestJavaPath).toURI().toURL()},
      Utils.classLoader);

    final Class<?> cls = Class.forName(
      StringUtils.difference(srcTestJavaPath, sourceFile.getAbsolutePath()).substring(1).replace(".java", "").replace(File.separatorChar, '.'),
      true,
      Utils.testClassLoader);
    final JUnitCore coreRunner = new JUnitCore();
    final Result r = coreRunner.run(cls);

    final float totalTestForThisClass = r.getRunCount() - r.getIgnoreCount();
    final float totalSucceedForThisClass = totalTestForThisClass - r.getFailureCount();
    final float successRate = totalSucceedForThisClass / totalTestForThisClass;

    System.out.printf("%s %.2f\t%d/%d \n",
      sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separatorChar + "introclassJava")),
      successRate * 100, (int) totalSucceedForThisClass, (int) totalTestForThisClass);
    return successRate == 1;
  }
}
