package fil.iagl.opl.dataset;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import fil.iagl.opl.dataset.checksum.ChecksumMainTransformer;
import fil.iagl.opl.dataset.checksum.ChecksumTestTransformer;
import fil.iagl.opl.dataset.digits.DigitsMainTransformer;
import fil.iagl.opl.dataset.digits.DigitsTestTransformer;
import fil.iagl.opl.dataset.grade.GradeMainTransformer;
import fil.iagl.opl.dataset.grade.GradeTestTransformer;
import fil.iagl.opl.dataset.median.MedianMainTransformer;
import fil.iagl.opl.dataset.median.MedianTestTransformer;
import fil.iagl.opl.dataset.smallest.SmallestMainTransformer;
import fil.iagl.opl.dataset.smallest.SmallestTestTransformer;
import fil.iagl.opl.dataset.syllables.SyllablesMainTransformer;
import fil.iagl.opl.dataset.syllables.SyllablesTestTransformer;
import spoon.Launcher;

public class DatasetTransformer {

  private static final String INTROCLASS_DATASET_DIRECTORY_PATH = "C:/workspace/IntroClassJava";

  private static final List<Transformer> mainTransformers = Arrays.asList(new ChecksumMainTransformer(), new DigitsMainTransformer(), new GradeMainTransformer(),
    new MedianMainTransformer(), new SmallestMainTransformer(), new SyllablesMainTransformer());
  private static final List<Transformer> testTransformers = Arrays.asList(new ChecksumTestTransformer(), new DigitsTestTransformer(), new GradeTestTransformer(),
    new MedianTestTransformer(), new SmallestTestTransformer(), new SyllablesTestTransformer());

  public static void main(String[] args) throws IOException {
    final File introClassfile = new File(DatasetTransformer.INTROCLASS_DATASET_DIRECTORY_PATH);
    final File introClassfileTransformed = new File(DatasetTransformer.INTROCLASS_DATASET_DIRECTORY_PATH + "_transformed");

    if (introClassfileTransformed.exists()) {
      System.out.println("Deleting older temporary folder...");
      FileUtils.forceDelete(introClassfileTransformed);
    }
    System.out.println("Creating temporary folder...");
    FileUtils.copyDirectory(introClassfile, introClassfileTransformed);

    Arrays.stream(new File(introClassfileTransformed.getAbsolutePath() + File.separatorChar + "dataset").listFiles()).filter(file -> file.isDirectory()).forEach(file -> {
      Arrays.stream(file.listFiles()).filter(subDir -> subDir.isDirectory()).forEach(subDir -> {
        DatasetTransformer.transform(subDir);
      });
    });
  }

  public static void transform(File subDir) {
    Arrays.stream(subDir.listFiles())
      .filter(file -> file.isDirectory())
      .forEach(
        dir -> {
          try {
            Collection<File> filesToBeDeleted = FileUtils.listFiles(dir, new String[] {"class"}, true);
            filesToBeDeleted.forEach(classFile -> {
              classFile.delete();
            });
            File sourceFolder = new File(dir.getAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "java");
            File testFolder = new File(dir.getAbsolutePath() + File.separator + "src" + File.separator + "test" + File.separator + "java");
            Transformer mainTransformer = mainTransformers.stream().filter(transformer -> transformer.match(dir.getAbsolutePath())).findAny()
              .orElseThrow(() -> new RuntimeException("No transformer"));
            Transformer testTransformer = testTransformers.stream().filter(transformer -> transformer.match(dir.getAbsolutePath())).findAny()
              .orElseThrow(() -> new RuntimeException("No transformer"));

            String[] spoonArgsTransformMain = {
              "-i",
              sourceFolder.getAbsolutePath(),
              "-o",
              sourceFolder.getAbsolutePath(),
              "-p",
              mainTransformer.getClass().getCanonicalName(),
              "--with-imports",
              "-x"
            };
            String[] spoonArgsTransformTest = {
              "-i",
              testFolder.getAbsolutePath(),
              "-o",
              testFolder.getAbsolutePath(),
              "-p",
              testTransformer.getClass().getCanonicalName(),
              "--with-imports",
              "-x"
            };

            Launcher.main(spoonArgsTransformMain);
            Launcher.main(spoonArgsTransformTest);

          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
  }

}
