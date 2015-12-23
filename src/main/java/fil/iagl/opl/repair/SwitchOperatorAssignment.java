package fil.iagl.opl.repair;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtOperatorAssignment;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class SwitchOperatorAssignment extends AbstractProcessor<CtOperatorAssignment<?, ?>> {

  @Override
  public void process(CtOperatorAssignment<?, ?> element) {
    if (element.equals(element.getParent(CtMethod.class).getElements(new TypeFilter<>(CtOperatorAssignment.class)).get(2))) {
      element.setKind(BinaryOperatorKind.MOD);
    }

  }

}
