package fil.iagl.opl.repair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.invoker.PrintStreamHandler;

public class MavenRunner {

  private static final String MAVEN_HOME_PATH = "C:/Users/RMS/Downloads/apache-maven-3.3.3-bin/apache-maven-3.3.3";

  public static void cleanCompileTest(String pomLocation) throws MavenInvocationException, FileNotFoundException {
    InvocationRequest request = new DefaultInvocationRequest();
    request.setPomFile(new File(pomLocation));
    request.setGoals(Arrays.asList("clean", "compile", "test"));

    Invoker invoker = new DefaultInvoker();
    invoker.setMavenHome(new File(MAVEN_HOME_PATH));
    invoker.setOutputHandler(new PrintStreamHandler(new PrintStream(new File("mvn.log")), true));
    invoker.execute(request);
  }

}
