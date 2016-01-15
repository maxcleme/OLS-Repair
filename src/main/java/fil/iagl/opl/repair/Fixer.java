package fil.iagl.opl.repair;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.xml.sax.SAXException;

import spoon.Launcher;

public class Fixer {

  private static URLClassLoader classLoader;

  private static URLClassLoader testClassLoader;

  public static void transform(File projectDir, String githubProjectsPath) throws SAXException, IOException, ParserConfigurationException, MavenInvocationException {
    File sourceFolder = new File(projectDir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "java");

    System.out.println("Analysing " + projectDir.getAbsolutePath() + "...");

    boolean allPassed = runAllTest(sourceFolder, true);
    if (allPassed) {
      System.out.println("No need to be fixed");
    } else {
      System.out.println("Trying to fix it...");
      while (!allPassed) {
        try {
          Fixer.cleanFolder(sourceFolder);
          Fixer.switchClassBody(sourceFolder, githubProjectsPath);
          allPassed = runAllTest(sourceFolder, false);
        } catch (Exception e) {
          System.out.println("Exception occured during fix, trying another solution...");
        }
      }
      System.out.println("Fix found ! ");
    }

  }

  public static void generateReport(File projectDir) throws SAXException, IOException, ParserConfigurationException {
    File sourceFolder = new File(projectDir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "java");
    runAllTest(sourceFolder, true);
  }

  private static void switchClassBody(File sourceFolder, String githubProjectsPath) throws Exception {
    File githubProjectsDir = new File(githubProjectsPath);
    File[] solutionsProject = githubProjectsDir.listFiles();

    Random r = new Random();
    File choosenSolution = solutionsProject[r.nextInt(solutionsProject.length)];
    String[] spoonArgs = {
      "-i",
      sourceFolder.getAbsolutePath() + File.pathSeparatorChar + choosenSolution.getAbsolutePath() + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar
        + "java",
      "-o",
      sourceFolder.getAbsolutePath(),
      "-p",
      "fil.iagl.opl.repair.SwitchClassBody",
      "--with-imports",
      "-x"
    };
    Launcher.main(spoonArgs);

  }

  private static void compile(final File sourceFile, String... classpaths)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

    // Compile source file.
    final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    compiler.run(null, null, null, "-source", "8", "-cp",
      App.JUNIT_JAR_PATH
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
        Fixer.compile(sourceFile, srcMainJavaPath, srcTestJavaPath);
        Fixer.classLoader = URLClassLoader.newInstance(new URL[] {new File(srcMainJavaPath).toURI().toURL()});
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    });
    // Compile every .java src/test/java
    FileUtils.listFiles(new File(srcTestJavaPath), new String[] {"java"}, true).forEach(sourceFile -> {
      try {
        Fixer.compile(sourceFile, srcMainJavaPath, srcTestJavaPath);
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    });

    // Run test inside src/test/java
    boolean allPassed = true;
    for (File testFile : FileUtils.listFiles(new File(srcTestJavaPath), new String[] {"java"}, true)) {
      try {
        if (withBlackbox || !testFile.getAbsolutePath().contains("Blackbox")) {
          allPassed &= Fixer.runTest(testFile, srcTestJavaPath);
        }
      } catch (ClassNotFoundException e) {
        allPassed = false;
      }
    }
    return allPassed;
  }

  public static void cleanFolder(File sourceFolder) {
    Collection<File> filesToBeDeleted = FileUtils.listFiles(sourceFolder, new String[] {"class"}, true);
    filesToBeDeleted.addAll(FileUtils.listFiles(sourceFolder, new RegexFileFilter("[a-z]*.java"), TrueFileFilter.INSTANCE));
    filesToBeDeleted.forEach(classFile -> {
      classFile.delete();
    });
  }

  private static boolean runTest(File sourceFile, String srcTestJavaPath) throws ClassNotFoundException, MalformedURLException {
    Fixer.testClassLoader = URLClassLoader.newInstance(
      new URL[] {new File(srcTestJavaPath).toURI().toURL()},
      Fixer.classLoader);

    final Class<?> cls = Class.forName(
      StringUtils.difference(srcTestJavaPath, sourceFile.getAbsolutePath()).substring(1).replace(".java", "").replace(File.separatorChar, '.'),
      true,
      Fixer.testClassLoader);
    final JUnitCore coreRunner = new JUnitCore();
    final Result r = coreRunner.run(cls);

    final float totalTestForThisClass = r.getRunCount() - r.getIgnoreCount();
    final float totalSucceedForThisClass = totalTestForThisClass - r.getFailureCount();
    final float successRate = totalSucceedForThisClass / totalTestForThisClass;

    System.out.printf("%s %.2f\n", sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separatorChar + "introclassJava")), successRate * 100);
    return successRate == 1;
  }
}
