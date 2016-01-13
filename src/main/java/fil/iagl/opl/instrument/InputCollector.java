package fil.iagl.opl.instrument;

import java.util.ArrayList;
import java.util.List;

import fr.inria.lille.commons.trace.Specification;

public class InputCollector {

  private static final List<Specification<Integer>> specs = new ArrayList<>();

  private InputCollector() {
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
