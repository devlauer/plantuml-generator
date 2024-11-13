package de.elnarion.cli;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DynamicCompileUtils {
    // get system compiler:
    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    public static synchronized void compile(List<String> sourceFiles, List<String> classpathURLs) {
        if (compiler == null) {
            throw new IllegalStateException("No Java compiler available.");
        }

        System.out.println("source file list " + sourceFiles + "...");

        for (String sourceFile : sourceFiles) {
            File source = new File(sourceFile);
            //  get the parent directory of the source file as the class output directory
            String classOutputFolder = source.getParent();

            List<String> arguments = new ArrayList<>();

            if (classpathURLs!=null && classpathURLs.size() > 0){
                // add classpath
                arguments.add("-cp");
                arguments.add(String.join(File.pathSeparator, classpathURLs));
            }
            arguments.add("-d");
            arguments.add(classOutputFolder);
            arguments.addAll(sourceFiles);  // add all source files

            System.out.println("Compiling " + sourceFile + "...");
            int compilationResult = compiler.run(null, System.out, System.err, arguments.toArray(new String[0]));
            if (compilationResult == 0) {
                System.out.println("Compilation of " + sourceFile + " was successful.");
            } else {
                System.out.println("Compilation of " + sourceFile + " failed.");
            }
        }
    }


}
