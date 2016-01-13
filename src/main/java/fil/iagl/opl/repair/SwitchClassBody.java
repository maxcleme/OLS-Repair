package fil.iagl.opl.repair;

import java.util.List;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.visitor.filter.TypeFilter;

public class SwitchClassBody extends AbstractProcessor<CtClass<?>> {

  @Override
  public boolean isToBeProcessed(CtClass<?> candidate) {
    return candidate.hasModifier(ModifierKind.PUBLIC) && !candidate.getSimpleName().matches("[a-z]*");
  }

  @Override
  public void process(CtClass<?> clazz) {
    CtPackage solutionPackage = getFactory().Package().get("introclassJava");
    List<CtClass<?>> allPossibleSolution = solutionPackage.getElements(new TypeFilter<CtClass<?>>(CtClass.class));
    CtClass<?> tryingSolution = allPossibleSolution.stream().filter(solution -> solution.hasModifier(ModifierKind.PUBLIC) && solution.getSimpleName().matches("[a-z]*")).findAny()
      .get();

    // System.out.println(clazz);
    // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    clazz.setMethods(tryingSolution.getMethods());
    clazz.setMethods(tryingSolution.getMethods());
    clazz.getAllFields().clear();
    clazz.getAllFields().addAll(tryingSolution.getAllFields());

    // System.out.println(clazz);
  }

}
