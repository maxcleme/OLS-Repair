package fil.iagl.opl.repair;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MavenRunner {

  private static final String MAVEN_HOME_PATH = "D:/apache-maven-3.2.5";

  public static void cleanCompileTest(String pomLocation) throws MavenInvocationException {
    InvocationRequest request = new DefaultInvocationRequest();
    request.setPomFile(new File(pomLocation));
    request.setGoals(Arrays.asList("clean", "compile", "test"));

    Invoker invoker = new DefaultInvoker();
    invoker.setMavenHome(new File(MAVEN_HOME_PATH));
    invoker.execute(request);
  }

}
