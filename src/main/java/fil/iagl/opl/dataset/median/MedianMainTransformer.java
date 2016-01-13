package fil.iagl.opl.dataset.median;

import fil.iagl.opl.dataset.Transformer;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

public class MedianMainTransformer extends AbstractProcessor<CtClass<?>> implements Transformer {

  @Override
  public void process(CtClass<?> clazz) {

  }

  @Override
  public boolean match(String fileName) {
    return fileName.contains("median");
  }

}
