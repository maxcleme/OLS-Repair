package fil.iagl.opl.repair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import spoon.Launcher;

public class MainPOC {

  public static void hijack(String sourceFolderPath, String pomLocation) throws Exception {

    String[] spoonArgs = {
      "-i",
      sourceFolderPath + File.pathSeparatorChar + "src" + File.separator + "main" + File.separator + "java" + File.separator + "aop",
      "-o",
      sourceFolderPath,
      "--with-imports",
      "-x"
    };
    Launcher.main(spoonArgs);

    injectDependencies(pomLocation);
  }

  private static void injectDependencies(String pomLocation) throws IOException, XmlPullParserException {
    Reader r = new FileReader(pomLocation);
    Model m = new MavenXpp3Reader().read(r);

    Dependency aspectjrt = new Dependency();
    aspectjrt.setGroupId("org.aspectj");
    aspectjrt.setArtifactId("aspectjrt");
    aspectjrt.setVersion("1.8.7");

    Plugin aspectJPlugin = new Plugin();
    aspectJPlugin.setGroupId("org.codehaus.mojo");
    aspectJPlugin.setArtifactId("aspectj-maven-plugin");
    aspectJPlugin.setVersion("1.8");

    Xpp3Dom configuration = new Xpp3Dom("configuration");

    Xpp3Dom complianceLevel = new Xpp3Dom("complianceLevel");
    complianceLevel.setValue("1.7");
    Xpp3Dom source = new Xpp3Dom("source");
    source.setValue("1.7");
    Xpp3Dom target = new Xpp3Dom("target");
    target.setValue("1.7");

    configuration.addChild(complianceLevel);
    configuration.addChild(source);
    configuration.addChild(target);

    PluginExecution execution = new PluginExecution();
    execution.setGoals(Arrays.asList("compile", "test-compile"));

    aspectJPlugin.setConfiguration(configuration);
    aspectJPlugin.addExecution(execution);

    m.setBuild(new Build());
    m.getBuild().addPlugin(aspectJPlugin);
    m.addDependency(aspectjrt);

    Writer w = new FileWriter(pomLocation);
    new MavenXpp3Writer().write(w, m);
  }

}
