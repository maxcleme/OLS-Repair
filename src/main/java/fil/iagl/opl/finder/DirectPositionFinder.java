package fil.iagl.opl.finder;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;

public class DirectPositionFinder implements Finder {
  @Override
  public boolean match(CtExpression<?> expr) {
    return expr instanceof CtInvocation;
  }

  @Override
  public CtInvocation<?> getFrom(CtExpression<?> expr) {
    return ((CtInvocation<?>) (expr));
  }
}
