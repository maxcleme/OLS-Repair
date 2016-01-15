package fil.iagl.opl.dataset.digits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import fil.iagl.opl.dataset.Transformer;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;

public class DigitsMainTransformer extends AbstractProcessor<CtClass<?>> implements Transformer {

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
    List<CtStatement> newMethodStatements = new ArrayList<>(method.getBody().getStatements());
    removeFirstAndLastOutput(newMethodStatements);
    newMethodStatements = newMethodStatements.stream().map(statement -> {
      statement.getElements(new TypeFilter<>(CtIf.class)).stream().filter(ctif -> ctif.getCondition().toString().contains("true")).forEach(ctif -> {
        ctif.replace(getFactory().Code().createCodeSnippetStatement("return output.replace(\" \", \"\").replace(\"\\n\", \"\")"));
        System.out.println(ctif);
      });
      statement.getElements(new TypeFilter<>(CtInvocation.class)).stream().filter(invoc -> invoc.toString().contains("String.format") && invoc.getArguments().size() > 1)
        .forEach(invoc -> {
        invoc.replace(getFactory().Code().createCodeSnippetStatement("new String(\"\")"));
        System.out.println(invoc);
      });
      if (statement instanceof CtIf && ((CtIf) (statement)).getCondition().toString().contains("true")) {
        return getFactory().Code().createCodeSnippetStatement("return output.replace(\" \", \"\").replace(\"\\n\", \"\")");
      }
      return statement;
    }).collect(Collectors.toList()); // replace : if ( true ) return by return output
    // newMethodStatements.add(getFactory().Code().createCodeSnippetStatement("return output"));
    newMethodStatements = newMethodStatements.stream().map(statement -> {
      if (statement.toString().contains("scanner.findInLine(\".\").charAt(0)")) {
        return getFactory().Code().createCodeSnippetStatement(
          statement.toString().replace("scanner.findInLine(\".\").charAt(0)", "param.charAt(idx++)").replace("NullPointerException", "IndexOutOfBoundsException"));
      }
      if (statement.toString().contains("scanner.nextInt()")) {
        return getFactory().Code().createCodeSnippetStatement(
          statement.toString().replace("scanner.nextInt()", "Integer.parseInt(param)"));
      }
      if (statement.toString().contains("scanner.nextLong()")) {
        return getFactory().Code().createCodeSnippetStatement(
          statement.toString().replace("scanner.nextLong()", "Long.parseLong(param)"));
      }
      return statement;
    }).collect(Collectors.toList());
    createMethod(newMethodStatements, target);

    List<CtStatement> newExecStatements = new ArrayList<>();
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("String input"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(\"Enter an integer > \")"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("input = scanner.nextLine()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(digits(input) + \" That's all, have a nice day!\")"));
    method.getBody().setStatements(newExecStatements);
  }

  private void removeFirstAndLastOutput(List<CtStatement> newMethodStatements) {
    ArrayList<CtStatement> toBeDelete = new ArrayList<CtStatement>();
    ArrayList<CtStatement> copy = new ArrayList<>(newMethodStatements);
    toBeDelete.add(copy.stream().filter(statement -> statement.toString().contains("output")).findFirst().orElse(null));
    Collections.reverse(copy);
    toBeDelete.add(copy.stream().filter(statement -> statement.toString().contains("output")).findFirst().orElse(null));
    newMethodStatements.removeAll(toBeDelete);
  }

  private void createMethod(List<CtStatement> newMethodStatements, CtClass<?> target) {
    CtParameter<String> param = getFactory().Core().createParameter();
    param.setType(getFactory().Type().STRING);
    param.setSimpleName("param");
    CtMethod<String> digitsMethod = getFactory().Method().create(target, Sets.newHashSet(ModifierKind.PUBLIC), getFactory().Type().STRING, "digits",
      Arrays.asList(param), Sets.newHashSet());
    CtBlock<String> body = getFactory().Core().createBlock();
    body.setStatements(newMethodStatements);
    digitsMethod.setBody(body);
  }

  @Override
  public boolean match(String fileName) {
    return fileName.contains("digits");
  }

  public static void main(String[] args) throws Exception {
    String[] spoonArgsTransformMain = {
      "-i",
      "C:/workspace/IntroClassJava/dataset/digits/1b31fa5c50f7725ce468ebf24282f2d080a83aed87e4ee35522ae7710c8e0136bc263cc460b8ec7bf2c3519cb59af4a138e114d36541515b2609ab56ad2b8ee9/004",
      "-p",
      "fil.iagl.opl.dataset.digits.DigitsMainTransformer",
      "--with-imports",
      "-x"
    };

    Launcher.main(spoonArgsTransformMain);
  }

}
