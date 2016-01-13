package fil.iagl.opl.instrument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fil.iagl.opl.finder.FinderFactory;
import fr.inria.lille.commons.trace.Specification;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class ConstructModel extends AbstractProcessor<CtMethod<?>> {

  @Override
  public boolean isToBeProcessed(CtMethod<?> candidate) {
    return candidate.getAnnotation(org.junit.Test.class) != null;
  }

  @Override
  public void process(CtMethod<?> method) {
    System.out.println(method);
    List<CtInvocation<?>> invocations = method.getElements(new TypeFilter<>(CtInvocation.class));
    for (CtInvocation<?> invocation : invocations) {
      if (invocation.getTarget().getType().getQualifiedName().equals("org.junit.Assert")) {
        int nbArgs = invocation.getArguments().size();
        CtInvocation<?> position = FinderFactory.getPositionFinder(invocation.getArguments().get(nbArgs - 1)).getFrom(invocation.getArguments().get(nbArgs - 1));
        if (position == null) {
          System.out.println("Cannot find faulty position from this assert");
        } else {
          Map<String, Object> testCollect = new HashMap<>();
          List<CtExpression<?>> args = position.getArguments();
          for (int i = 0; i < args.size(); i++) {
            testCollect.put(position.getExecutable().getDeclaration().getParameters().get(i).getSimpleName(), Integer.parseInt(args.get(i).toString()));
          }
          InputCollector.addSpecification(new Specification<Integer>(testCollect, Integer.parseInt(invocation.getArguments().get(nbArgs - 2).toString())));
        }
      }
    }
  }

}
