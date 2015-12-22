package fil.iagl.opl.repair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Hello world!
 *
 */
public class App {

    private static final String   JUNIT_JAR_PATH                    = "C:/Users/mclement/.m2/repository/junit/junit/4.11/junit-4.11.jar";

    private static final String   INTROCLASS_DATASET_DIRECTORY_PATH = "C:/workspace/IntroClassJava/dataset";                             // A mettre dans ce path la obligatoirement

    private static URLClassLoader classLoader;

    private static URLClassLoader testClassLoader;

    private static int            totalTest                         = 0;

    private static int            totalSucceed                      = 0;

    public static void main( final String[] args ) {
        final File introClassfile = new File( App.INTROCLASS_DATASET_DIRECTORY_PATH );
        Arrays.stream( introClassfile.listFiles() ).filter( file -> file.isDirectory() ).forEach( file -> {
            Arrays.stream( file.listFiles() ).filter( subDir -> subDir.isDirectory() ).forEach( subDir -> {
                App.runTest( subDir );
            } );
        } );

        System.out.printf( "################# RESULT ################\n\tTest\t\t : %.2f%%\tSuccess :%d\tFailure : %d\n", ( (float) App.totalSucceed / App.totalTest ) * 100, App.totalSucceed,
                ( App.totalTest - App.totalSucceed ) );

    }

    private static void runTest( final File folder ) {
        Arrays.stream( folder.listFiles() )
                .filter( file -> file.isDirectory() )
                .forEach(
                        file -> {
                            final String folderPath = file.getAbsolutePath();
                            System.out.printf( "Running test of %s\n", folderPath.substring( StringUtils.ordinalIndexOf( folderPath, File.separator,
                                    ( folderPath.split( StringEscapeUtils.escapeJava( File.separator ) ).length - 1 ) - 2 ) ) );
                            App.loadClass( folderPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "introclassJava", false );
                            App.loadClass( folderPath + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator + "introclassJava", true );
                        } );

    }

    private static void loadClass( final String folderPath , final boolean runTest ) {
        final File sourceDir = new File( folderPath );
        Arrays.stream( sourceDir.listFiles() ).filter( sourceFile -> sourceFile.getAbsolutePath().endsWith( ".java" ) ).forEach( sourceFile -> {
            try {
                App.compile( sourceFile, runTest );
            } catch ( final Exception e ) {
                throw new RuntimeException( e );
            }
        } );

    }

    private static void compile( final File sourceFile , final boolean runTest ) throws ClassNotFoundException , InstantiationException , IllegalAccessException , IOException {
        // System.out.printf("\tCompiling\t : %s\n",
        // sourceFile.getAbsolutePath().substring(sourceFile.getAbsolutePath().lastIndexOf(File.separator)).substring(1));

        // Compile source file.
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        compiler.run( null, null, null, "-source", "8", "-cp",
                App.JUNIT_JAR_PATH
                        + File.pathSeparatorChar
                        + new File( sourceFile.getAbsolutePath().substring( 0, StringUtils.ordinalIndexOf( sourceFile.getAbsolutePath(), File.separator, 8 ) ) + File.separator + "main"
                                + File.separator + "java" + File.separator ).getAbsolutePath(), sourceFile.getPath() );

        if ( runTest ) {
            App.testClassLoader = URLClassLoader.newInstance(
                    new URL[]{ new File( sourceFile.getAbsolutePath().substring( 0, StringUtils.ordinalIndexOf( sourceFile.getAbsolutePath(), File.separator, 10 ) ) ).toURI().toURL() },
                    App.classLoader );

            final Class< ? > cls = Class.forName(
                    "introclassJava." + sourceFile.getAbsolutePath().substring( sourceFile.getAbsolutePath().lastIndexOf( File.separator ) ).substring( 1 ).replace( ".java", "" ), true,
                    App.testClassLoader );
            final JUnitCore coreRunner = new JUnitCore();
            final Result r = coreRunner.run( cls );

            final int totalTestForThisClass = r.getRunCount() - r.getIgnoreCount();
            final int totalSucceedForThisClass = totalTestForThisClass - r.getFailureCount();
            final float successRate = ( ( (float) totalSucceedForThisClass ) / totalTestForThisClass ) * 100;

            System.out.printf( "\tTest\t\t : %.2f%%\tSuccess :%d\tFailure : %d\n", successRate, totalSucceedForThisClass, totalTestForThisClass - totalSucceedForThisClass );

            App.totalTest += totalTestForThisClass;
            App.totalSucceed += totalSucceedForThisClass;

            App.testClassLoader.close();
        } else {
            App.classLoader = URLClassLoader.newInstance( new URL[]{ new File( sourceFile.getAbsolutePath()
                    .substring( 0, StringUtils.ordinalIndexOf( sourceFile.getAbsolutePath(), File.separator, 10 ) ) ).toURI().toURL() } );
        }
    }
}
