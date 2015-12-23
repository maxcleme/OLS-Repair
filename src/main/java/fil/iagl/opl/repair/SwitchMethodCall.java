package fil.iagl.opl.repair;

import java.util.Scanner;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;

public class SwitchMethodCall extends AbstractProcessor<CtInvocation<Object>> {

  @Override
  public void process(CtInvocation<Object> element) {
    if (element.getTarget() != null && element.getTarget().getType().getQualifiedName().equals(Scanner.class.getName())) {
      element.getExecutable().setSimpleName("nextLine");
    }

  }
}
