package fil.iagl.opl.dataset.syllables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import fil.iagl.opl.dataset.Transformer;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;

public class SyllablesMainTransformer extends AbstractProcessor<CtClass<?>> implements Transformer {

  @Override
  public void process(CtClass<?> clazz) {
    for (CtMethod<?> method : clazz.getAllMethods()) {
      if (method.getSignature().contains("exec")) {
        modifyExecAndCreate(method, clazz);
      } else if (method.getSignature().contains("main")) {
        modifyMain(method);
      }
    }
  }

  private void modifyMain(CtMethod<?> method) {
    method.getBody().getStatements().remove(method.getBody().getStatements().size() - 1);
    method.getBody().getStatements().removeIf(statement -> statement.toString().contains("output"));
  }

  private void modifyExecAndCreate(CtMethod<?> method, CtClass<?> target) {
    List<CtStatement> outputStatements = method.getBody().getStatements().stream().filter(statement -> statement.toString().contains("output")).collect(Collectors.toList());
    List<CtStatement> newMethodStatements = new ArrayList<>(method.getBody().getStatements());
    removeFirstOutput(newMethodStatements);
    AtomicInteger idx = new AtomicInteger(0);
    newMethodStatements = newMethodStatements.stream().map(statement -> {
      if (statement instanceof CtIf && ((CtIf) (statement)).getCondition().toString().contains("true")) {
        ((CtIf) (statement)).getThenStatement().replace(getFactory().Code().createCodeSnippetStatement("return Integer.parseInt(output)"));
      }
      if (statement.toString().contains("scanner.nextLine()")) {
        return getFactory().Code().createCodeSnippetStatement(
          statement.toString().replace("scanner.nextLine()", "param"));
      }
      return statement;
    }).collect(Collectors.toList()); // replace : if ( true ) return by return output
    newMethodStatements.add(getFactory().Code().createCodeSnippetStatement("return Integer.parseInt(output)"));
    createMethod(newMethodStatements, target);

    List<CtStatement> newExecStatements = new ArrayList<>();
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(\"Please enter a string > \")"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("String input = scanner.nextLine()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(\"The number of syllables is \" + syllables(input))"));
    method.getBody().setStatements(newExecStatements);
  }

  private void removeFirstOutput(List<CtStatement> newMethodStatements) {
    ArrayList<CtStatement> toBeDelete = new ArrayList<CtStatement>();
    toBeDelete.add(newMethodStatements.stream().filter(statement -> statement.toString().contains("output")).findFirst().orElse(null));
    newMethodStatements.removeAll(toBeDelete);
  }

  private void createMethod(List<CtStatement> newMethodStatements, CtClass<?> target) {
    CtParameter<String> param = getFactory().Core().createParameter();
    param.setType(getFactory().Type().STRING);
    param.setSimpleName("param");
    CtMethod<Integer> syllablesMethod = getFactory().Method().create(target, Sets.newHashSet(ModifierKind.PUBLIC), getFactory().Type().INTEGER_PRIMITIVE, "syllables",
      Arrays.asList(param), Sets.newHashSet());
    CtBlock<Integer> body = getFactory().Core().createBlock();
    body.setStatements(newMethodStatements);
    syllablesMethod.setBody(body);
  }

  @Override
  public boolean match(String fileName) {
    return fileName.contains("syllables");
  }
}
