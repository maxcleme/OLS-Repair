package fil.iagl.opl.repair;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.reference.SpoonClassNotFoundException;

public class SwitchMethodCall extends AbstractProcessor<CtClass<?>> {

  private Predicate<CtInvocation<?>> isToBeProceed = candidate -> {
    boolean foundInSpoonClassPath = true;
    try {
      candidate.getExecutable().getDeclaringType().getActualClass();
    } catch (SpoonClassNotFoundException e)

    {
      foundInSpoonClassPath = false;
    }
    return candidate.getTarget() != null && candidate.getArguments().isEmpty() && foundInSpoonClassPath;

  };
  private boolean hasBeenDone = false;

  @Override
  public boolean isToBeProcessed(CtClass<?> candidate) {
    return !candidate.getQualifiedName().endsWith("Test");
  }

  @Override
  public void process(CtClass<?> clazz) {

    List<CtInvocation<?>> invocations = clazz.getElements(new TypeFilter<CtInvocation<?>>(CtInvocation.class)).stream().filter(isToBeProceed).collect(Collectors.toList());
    if (!invocations.isEmpty() && !hasBeenDone) {
      Random r = new Random();
      CtInvocation<?> element = invocations.get(r.nextInt(invocations.size()));

      List<Method> availableMethod = Arrays.stream(element.getTarget().getType().getActualClass().getMethods())
        .filter(method -> method.getParameterTypes().length == 0 &&
          method.getReturnType().getName().equals(element.getType().getActualClass().getName()))
        .collect(Collectors.toList());
      element.getExecutable().setSimpleName(availableMethod.get(r.nextInt(availableMethod.size())).getName());
      hasBeenDone = true;
    }

  }
}
