package fil.iagl.opl.repair;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtOperatorAssignment;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;

public class SwitchOperatorAssignment extends AbstractProcessor<CtClass<?>> {

  private List<BinaryOperatorKind> possibleOperator = Arrays.asList(BinaryOperatorKind.PLUS, BinaryOperatorKind.MINUS, BinaryOperatorKind.MUL, BinaryOperatorKind.DIV,
    BinaryOperatorKind.MOD, BinaryOperatorKind.SL, BinaryOperatorKind.SR, BinaryOperatorKind.BITOR, BinaryOperatorKind.BITAND, BinaryOperatorKind.BITXOR);
  private boolean hasBeenDone = false;

  @Override
  public boolean isToBeProcessed(CtClass<?> candidate) {
    return !candidate.getQualifiedName().endsWith("Test");
  }

  @Override
  public void process(CtClass<?> clazz) {

    Random r = new Random();

    List<CtOperatorAssignment<?, ?>> assigments = clazz.getElements(new TypeFilter<CtOperatorAssignment<?, ?>>(CtOperatorAssignment.class)).stream()
      .filter(assignment -> !assignment.getAssigned().getType().getActualClass().equals(getFactory().Type().STRING.getActualClass())).collect(Collectors.toList());
    if (!assigments.isEmpty() && !hasBeenDone) {
      CtOperatorAssignment<?, ?> element = assigments.get(r.nextInt(assigments.size()));
      element.setKind(possibleOperator.get(r.nextInt(possibleOperator.size())));
      hasBeenDone = true;
    }

  }

}
