package fil.iagl.opl.repair;

import fil.iagl.opl.instrument.InputCollector;
import fil.iagl.opl.solver.SMT_Solver;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtExecutable;

public class ApplyingPatch extends AbstractProcessor<CtExecutable<?>> {
  @Override
  public boolean isToBeProcessed(CtExecutable<?> candidate) {
    return candidate.equals(InputCollector.getFailingMethods().stream().findAny().get());
  }

  @Override
  public void process(CtExecutable<?> element) {
    element.getBody().getStatements().clear();
    element.getBody().getStatements().add(getFactory().Code().createCodeSnippetStatement("return " + SMT_Solver.patch.returnStatement()));
  }

}
