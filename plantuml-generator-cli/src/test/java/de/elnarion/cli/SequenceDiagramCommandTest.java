package de.elnarion.cli;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SequenceDiagramCommandTest {
    private String prefix = "../";

    @Test
    public void test_generate_sequence_diagram_text() {
        CliMain cmd = new CliMain();
        CommandLine commandLine = new CommandLine(cmd);

        int exitCode = commandLine.execute(
                "sequence",
                "--output-filename=test_generate_sequence_diagram_text.txt",
                "--start-class=de.elnarion.test.sequence.t0002.CallerClassA",
                "--start-method=testSomething",
                "--source-file-path=" + prefix + "/plantuml-generator-util/src/test/java/de/elnarion/test/sequence/t0002");

        assertEquals(0, exitCode);
        assertTrue(TestUtils.is_plantuml_content_equal(
                "generated-docs/test_generate_sequence_diagram_text.txt",
                prefix + "plantuml-generator-util/src/test/resources/sequence/0002_basic_super_class_sequence_diagram.txt"));
    }
}
