package fil.iagl.opl.dataset.digits;

import java.util.ArrayList;
import java.util.List;

import fil.iagl.opl.dataset.Transformer;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

public class DigitsTestTransformer extends AbstractProcessor<CtClass<?>> implements Transformer {

  @Override
  public void process(CtClass<?> clazz) {
    for (CtMethod<?> method : clazz.getAllMethods()) {
      List<CtStatement> statements = method.getBody().getStatements();
      List<CtStatement> newStatements = new ArrayList<>();
      newStatements.add(statements.get(0));
      newStatements.add(getFactory().Code().createCodeSnippetStatement(
        "String expected = \"" + statements.get(1).toString().substring(statements.get(1).toString().indexOf(">") + 1,
          statements.get(1).toString().indexOf("That's all, have a nice day!")) + "\""));
      newStatements.add(getFactory().Code().createCodeSnippetStatement(
        "String input = " + statements.get(2).toString().substring(statements.get(2).toString().indexOf("\""),
          statements.get(2).toString().length() - 1)));
      newStatements.add(getFactory().Code().createCodeSnippetStatement("org.junit.Assert.assertEquals(expected, mainClass.digits(input))"));
      method.getBody().setStatements(newStatements);
      System.out.println(method);
    }
  }

  @Override
  public boolean match(String fileName) {
    return fileName.contains("digits");
  }

}
