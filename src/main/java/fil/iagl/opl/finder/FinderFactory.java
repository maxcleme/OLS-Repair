package fil.iagl.opl.finder;

import java.util.Arrays;
import java.util.Collection;

import spoon.reflect.code.CtExpression;

public class FinderFactory {

  private static final Collection<Finder> finders = Arrays.asList(
    new DirectPositionFinder(),
    new UndirectPositionFinder());

  private FinderFactory() {
  }

  public static Finder getPositionFinder(CtExpression<?> expr) {
    return finders.stream().filter(finder -> finder.match(expr)).findFirst().orElseThrow(() -> new RuntimeException("No finder found"));
  }
}
