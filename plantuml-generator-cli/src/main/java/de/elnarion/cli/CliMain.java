package de.elnarion.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "plantuml-generator", mixinStandardHelpOptions = true,
        subcommands = {ClassDiagramCommand.class, SequenceDiagramCommand.class}
)
public class CliMain implements  Runnable {

    @Override
    public void run() {
        // print subcommands
        System.out.println("use ‘plantuml-generator <subcommand> [options]’ to generate diagrams");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CliMain()).execute(args);
        System.exit(exitCode);
    }
}
