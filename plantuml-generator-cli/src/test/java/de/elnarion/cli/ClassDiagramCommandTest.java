package de.elnarion.cli;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassDiagramCommandTest {
private String prefix = "../";

    @Test
    public void test_generate_class_diagram_text_1() {
        CliMain cmd = new CliMain();
        CommandLine commandLine = new CommandLine(cmd);

        int exitCode = commandLine.execute(
                "class",
                "--output-filename=test_generate_class_diagram_text_1.txt",
                "--scan-packages=de.elnarion.test.domain.t0001",
                "--hide-classes=de.elnarion.test.domain.ChildB",
                "--source-file-path=" + prefix + "plantuml-generator-util/src/test/java/de/elnarion/test/domain/t0001");

        assertEquals(0, exitCode);
        assertTrue(TestUtils.is_plantuml_content_equal(
                "generated-docs/test_generate_class_diagram_text_1.txt",
                prefix + "plantuml-generator-util/src/test/resources/class/0001_general_diagram.txt"));

    }

    @Test
    public void test_generate_class_diagram_text_2() {
        CliMain cmd = new CliMain();
        CommandLine commandLine = new CommandLine(cmd);

        int exitCode = commandLine.execute(
                "class",
                "--output-filename=test_generate_class_diagram_text_2.txt",
                "--scan-packages=de.elnarion.test.domain.t0002",
                "--source-file-path=" + prefix + "plantuml-generator-util/src/test/java/de/elnarion/test/domain/t0002");

        assertEquals(0, exitCode);
        assertTrue(TestUtils.is_plantuml_content_equal(
                "generated-docs/test_generate_class_diagram_text_2.txt",
                prefix + "plantuml-generator-util/src/test/resources/class/0002_class_types.txt"));
    }
}
