package fil.iagl.opl.dataset.checksum;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import fil.iagl.opl.dataset.Transformer;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;

public class ChecksumMainTransformer extends AbstractProcessor<CtClass<?>> implements Transformer {
  @Override
  public void process(CtClass<?> clazz) {
    for (CtMethod<?> method : clazz.getAllMethods()) {
      if (method.getSignature().contains("exec")) {
        modifyExecAndCreate(method, clazz);
      } else if (method.getSignature().contains("main")) {
        modifyMain(method);
      }
    }
    System.out.println(clazz);
  }

  private void modifyMain(CtMethod<?> method) {
    method.getBody().getStatements().remove(method.getBody().getStatements().size() - 1);
    method.getBody().getStatements().removeIf(statement -> statement.toString().contains("output"));
  }

  private void modifyExecAndCreate(CtMethod<?> method, CtClass<?> target) {
    List<CtStatement> outputStatements = method.getBody().getStatements().stream().filter(statement -> statement.toString().contains("output")).collect(Collectors.toList());
    List<CtStatement> newMethodStatements = method.getBody().getStatements().stream().filter(statement -> !statement.toString().contains("output")).collect(Collectors.toList());
    newMethodStatements.add(0, getFactory().Code().createCodeSnippetStatement("int idx = 0"));
    newMethodStatements.remove(newMethodStatements.size() - 1); // remove : if ( true ) return
    newMethodStatements.add(getFactory().Code().createCodeSnippetStatement("return (char)" + outputStatements.get(outputStatements.size() - 1).toString()
      .substring(outputStatements.get(outputStatements.size() - 1).toString().indexOf(",") + 1, outputStatements.get(outputStatements.size() - 1).toString().length() - 1)));
    newMethodStatements = newMethodStatements.stream().map(statement -> {
      if (statement.toString().contains("scanner.findInLine(\".\").charAt(0)")) {
        return getFactory().Code().createCodeSnippetStatement(
          statement.toString().replace("scanner.findInLine(\".\").charAt(0)", "input.charAt(idx++)").replace("NullPointerException", "IndexOutOfBoundsException"));
      }
      return statement;
    }).collect(Collectors.toList());
    createMethod(newMethodStatements, target);

    List<CtStatement> newExecStatements = new ArrayList<>();
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("String input"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(\"Enter an abitrarily long string, ending with carriage return > \")"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("input = scanner.nextLine()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(\"Check sum is \" + checksum(input))"));
    method.getBody().setStatements(newExecStatements);
  }

  private void createMethod(List<CtStatement> newMethodStatements, CtClass<?> target) {
    CtParameter<String> param = getFactory().Core().createParameter();
    param.setType(getFactory().Type().STRING);
    param.setSimpleName("input");
    CtMethod<Character> checksumMethod = getFactory().Method().create(target, Sets.newHashSet(ModifierKind.PUBLIC), getFactory().Type().CHARACTER_PRIMITIVE, "checksum",
      Arrays.asList(param), Sets.newHashSet());
    CtBlock<Character> body = getFactory().Core().createBlock();
    body.setStatements(newMethodStatements);
    checksumMethod.setBody(body);
  }

  @Override
  public boolean match(String fileName) {
    return fileName.contains("checksum");
  }

}
