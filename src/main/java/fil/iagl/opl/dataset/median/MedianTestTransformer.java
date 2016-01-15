package fil.iagl.opl.dataset.median;

import java.util.ArrayList;
import java.util.List;

import fil.iagl.opl.dataset.Transformer;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

public class MedianTestTransformer extends AbstractProcessor<CtClass<?>> implements Transformer {

  @Override
  public void process(CtClass<?> clazz) {
    for (CtMethod<?> method : clazz.getAllMethods()) {
      List<CtStatement> statements = method.getBody().getStatements();
      List<CtStatement> newStatements = new ArrayList<>();
      newStatements.add(statements.get(0));
      newStatements.add(getFactory().Code().createCodeSnippetStatement(
        "int expected = " + statements.get(1).toString().substring(statements.get(1).toString().indexOf(">") + 1, statements.get(1).toString().indexOf("is the median"))));
      newStatements.add(getFactory().Code().createCodeSnippetStatement(
        "int[] input = {" + statements.get(2).toString().substring(statements.get(2).toString().indexOf("\""),
          statements.get(2).toString().length() - 1).replace(' ', ',').replace("\"", "") + "}"));
      newStatements.add(getFactory().Code().createCodeSnippetStatement("org.junit.Assert.assertEquals(expected, mainClass.median(input))"));
      method.getBody().setStatements(newStatements);
    }
  }

  @Override
  public boolean match(String fileName) {
    return fileName.contains("median");
  }

}
