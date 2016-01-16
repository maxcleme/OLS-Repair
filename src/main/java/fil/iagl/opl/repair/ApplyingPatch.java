package fil.iagl.opl.repair;

import fil.iagl.opl.OLS_Repair;
import fil.iagl.opl.model.Model;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtExecutable;

public class ApplyingPatch extends AbstractProcessor<CtExecutable<?>> {
  @Override
  public boolean isToBeProcessed(CtExecutable<?> candidate) {
    return candidate.equals(Model.getFailingMethods().stream().findAny().get());
  }

  @Override
  public void process(CtExecutable<?> element) {
    element.getBody().getStatements().clear();
    element.getBody().getStatements().add(getFactory().Code().createCodeSnippetStatement("return " + OLS_Repair.patch.returnStatement()));
    System.out.println(element);
  }

}
