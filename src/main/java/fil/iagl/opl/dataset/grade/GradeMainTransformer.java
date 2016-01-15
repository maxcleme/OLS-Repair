package fil.iagl.opl.dataset.grade;

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
import spoon.reflect.visitor.filter.TypeFilter;

public class GradeMainTransformer extends AbstractProcessor<CtClass<?>> implements Transformer {

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
      statement.getElements(new TypeFilter<>(CtIf.class)).stream().filter(ctif -> ctif.getCondition().toString().contains("true")).forEach(ctif -> {
        ctif.replace(getFactory().Code().createCodeSnippetStatement("return output"));
      });
      if (statement instanceof CtIf && ((CtIf) (statement)).getCondition().toString().contains("true")) {
        return getFactory().Code().createCodeSnippetStatement("return output");
      }
      if (statement.toString().contains("scanner.nextFloat()")) {
        return getFactory().Code().createCodeSnippetStatement(statement.toString().replace("scanner.nextFloat()", "param[" + idx.getAndIncrement() + "]"));
      }
      return statement;
    }).collect(Collectors.toList()); // replace : if ( true ) return by return output
    createMethod(newMethodStatements, target);

    List<CtStatement> newExecStatements = new ArrayList<>();
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("int[] input = new int[5]"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(\"Enter thresholds for A, B, C, D in that order, decreasing percentages > \")"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("input[0] = scanner.nextInt()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("input[1] = scanner.nextInt()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("input[2] = scanner.nextInt()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("input[3] = scanner.nextInt()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(\"Thank you. Now enter student score (percent) > \")"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("input[4] = scanner.nextInt()"));
    newExecStatements.add(getFactory().Code().createCodeSnippetStatement("System.out.println(grade(input))"));
    method.getBody().setStatements(newExecStatements);
  }

  private void removeFirstOutput(List<CtStatement> newMethodStatements) {
    ArrayList<CtStatement> toBeDelete = new ArrayList<CtStatement>();
    toBeDelete.add(newMethodStatements.stream().filter(statement -> statement.toString().contains("output")).findFirst().orElse(null));
    newMethodStatements.removeAll(toBeDelete);
  }

  private void createMethod(List<CtStatement> newMethodStatements, CtClass<?> target) {
    CtParameter<Integer[]> param = getFactory().Core().createParameter();
    param.setType(getFactory().Type().createArrayReference(getFactory().Type().INTEGER_PRIMITIVE));
    param.setSimpleName("param");
    CtMethod<String> gradeMethod = getFactory().Method().create(target, Sets.newHashSet(ModifierKind.PUBLIC), getFactory().Type().STRING, "grade",
      Arrays.asList(param), Sets.newHashSet());
    CtBlock<String> body = getFactory().Core().createBlock();
    body.setStatements(newMethodStatements);
    gradeMethod.setBody(body);
  }

  @Override
  public boolean match(String fileName) {
    return fileName.contains("grade");
  }

}
