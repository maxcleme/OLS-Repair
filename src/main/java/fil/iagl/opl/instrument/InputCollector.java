package fil.iagl.opl.instrument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.inria.lille.commons.trace.Specification;
import spoon.reflect.declaration.CtExecutable;

public class InputCollector {

  private static final List<Specification<Integer>> specs = new ArrayList<>();

  private static final Set<CtExecutable<?>> failingMethods = new HashSet<>();

  private InputCollector() {
  }

  public static void addFailingMethods(CtExecutable<?> ctMethod) {
    failingMethods.add(ctMethod);
  }

  public static Set<CtExecutable<?>> getFailingMethods() {
    return failingMethods;
  }

  public static void addSpecification(Specification<Integer> spec) {
    specs.add(spec);
  }

  public static List<Specification<Integer>> getSpecs() {
    return specs;
  }

  public static void printSpec() {
    specs.forEach((spec) -> {
      spec.inputs().forEach((k, v) -> {
        System.out.println(k + " -> " + v);
      });
      System.out.println("output -> " + spec.output());
    });
  }

}
