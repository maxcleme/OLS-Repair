package fil.iagl.opl.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fil.iagl.opl.OLS_Repair;
import fil.iagl.opl.finder.FinderFactory;
import fr.inria.lille.commons.synthesis.CodeGenesis;
import fr.inria.lille.commons.synthesis.ConstraintBasedSynthesis;
import fr.inria.lille.commons.trace.Specification;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class ConstructModel extends AbstractProcessor<CtMethod<?>> {

  @Override
  public boolean isToBeProcessed(CtMethod<?> candidate) {
    return candidate.getAnnotation(org.junit.Test.class) != null && candidate.getParent(CtClass.class) != null
      && (OLS_Repair.USE_BLACK_BOX || !candidate.getParent(CtClass.class).getSimpleName().contains("Blackbox"));
  }

  @Override
  public void process(CtMethod<?> method) {
    List<CtInvocation<?>> invocations = method.getElements(new TypeFilter<>(CtInvocation.class));
    for (CtInvocation<?> invocation : invocations) {
      if (invocation.getTarget().getType().getQualifiedName().equals("org.junit.Assert")) {
        int nbArgs = invocation.getArguments().size();
        CtInvocation<?> position = FinderFactory.getPositionFinder(invocation.getArguments().get(nbArgs - 1)).getFrom(invocation.getArguments().get(nbArgs - 1));
        if (position == null) {
          System.out.println("Cannot find faulty position from this assert");
          System.exit(0);
        } else {
          Model.addFailingMethods(position.getExecutable().getDeclaration());
          if (Model.getFailingMethods().size() > 1) {
            System.out.println("Cannot synthesis patch for multiple position");
            System.exit(0);
          }
          Map<String, Object> testCollect = new HashMap<>();
          List<CtExpression<?>> args = position.getArguments();
          for (int i = 0; i < args.size(); i++) {
            if (args.get(i) instanceof CtVariableAccess) {
              CtVariableAccess<?> access = (CtVariableAccess<?>) args.get(i);
              if (access.getVariable().getType().equals(getFactory().Type().STRING)) {
                CtExpression<?> expr = access.getVariable().getDeclaration().getDefaultExpression();
                int j = 0;
                for (char c : expr.toString().replace("\"", "").toCharArray()) {
                  testCollect.put("((int)(" + position.getExecutable().getDeclaration().getParameters().get(i).getSimpleName() + ".charAt(" + j + ")))", (int) c);
                  j++;
                }
              } else if (access.getVariable().getType().equals(getFactory().Type().createArrayReference(getFactory().Type().INTEGER_PRIMITIVE))) {
                CtExpression<?> expr = access.getVariable().getDeclaration().getDefaultExpression();
                int j = 0;
                for (String s : expr.toString().replace("new int[]", "").replace("{", "").replace("}", "").split(",")) {
                  testCollect.put(position.getExecutable().getDeclaration().getParameters().get(i).getSimpleName() + "[" + j + "]", Integer.parseInt(s.trim()));
                  j++;
                }
              } else {
                System.out.println("Cannot handle defined type.");
                System.exit(0);
              }
            }
          }
          if (invocation.getArguments().get(nbArgs - 2) instanceof CtVariableAccess) {
            CtVariableAccess<?> expect = (CtVariableAccess<?>) invocation.getArguments().get(nbArgs - 2);
            if (expect.getVariable().getType().equals(getFactory().Type().CHARACTER_PRIMITIVE)) {
              Model
                .addSpecification(
                  new Specification<Integer>(testCollect, (int) expect.getVariable().getDeclaration().getDefaultExpression().toString().replace("'", "").charAt(0)));
            } else if (expect.getVariable().getType().equals(getFactory().Type().INTEGER_PRIMITIVE)) {
              Model
                .addSpecification(
                  new Specification<Integer>(testCollect, Integer.parseInt(expect.getVariable().getDeclaration().getDefaultExpression().toString())));
            } else {
              System.out.println("Cannot handle defined type.");
              System.exit(0);
            }
          }
        }
      }
    }
  }

  @Override
  public void processingDone() {
    Map<String, Integer> intConstants = new HashMap<>();
    for (int constant : OLS_Repair.CONSTANTS_ARRAY) {
      intConstants.put("" + constant, constant);
    }

    ConstraintBasedSynthesis synthesis = new ConstraintBasedSynthesis(intConstants);
    CodeGenesis genesis = synthesis.codesSynthesisedFrom(
      (Integer.class), Model.getSpecs());
    OLS_Repair.patch = genesis;

  }

}
