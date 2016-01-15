package fil.iagl.opl.repair;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Hello world!
 *
 */
public class App {

  public static final String JUNIT_JAR_PATH = "C:/Users/RMS/.m2/repository/junit/junit/4.11/junit-4.11.jar";

  private static final String INTROCLASS_DATASET_DIRECTORY_PATH = "C:/workspace/IntroClassJava"; // A mettre dans ce path la
                                                                                                 // obligatoirement
  private static final String GITHUB_PROJECTS_PATH = "C:/workspace/Solutions";

  public static void main(final String[] args) throws IOException, SAXException, ParserConfigurationException {

    System.setOut(new PrintStream((new File("test_t_out.log"))));

    final File introClassfile = new File(App.INTROCLASS_DATASET_DIRECTORY_PATH);
    final File introClassfilePatched = new File(App.INTROCLASS_DATASET_DIRECTORY_PATH + "_patched");

    // if (introClassfilePatched.exists()) {
    // System.out.println("Deleting older temporary folder...");
    // FileUtils.forceDelete(introClassfilePatched);
    // }
    // System.out.println("Creating temporary folder...");
    // FileUtils.copyDirectory(introClassfile, introClassfilePatched);

    Arrays.stream(new File(introClassfile.getAbsolutePath() + "_transformed" + File.separatorChar + "dataset").listFiles()).filter(file -> file.isDirectory())
      .forEach(file -> {
        Arrays.stream(file.listFiles()).filter(subDir -> subDir.isDirectory()).forEach(subDir -> {
          App.fix(subDir);
        });
      });

  }

  private static void fix(final File folder) {
    Arrays.stream(folder.listFiles())
      .filter(file -> file.isDirectory())
      .forEach(
        dir -> {
          try {
            // Fixer.transform(dir, App.GITHUB_PROJECTS_PATH);
            Fixer.generateReport(dir);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });

  }

}
