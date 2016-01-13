package fil.iagl.opl.finder;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtVariableAccess;

public class UndirectPositionFinder implements Finder {

  @Override
  public boolean match(CtExpression<?> expr) {
    return expr instanceof CtVariableAccess<?>;
  }

  @Override
  public CtInvocation<?> getFrom(CtExpression<?> expr) {
    return null;
  }

}
