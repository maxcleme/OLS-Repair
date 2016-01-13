package fil.iagl.opl.finder;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;

public interface Finder {
  boolean match(CtExpression<?> expr);

  CtInvocation<?> getFrom(CtExpression<?> expr);
}
