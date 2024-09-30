package de.elnarion.cli;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

public  class  TestUtils {
    static boolean is_plantuml_content_equal(String A, String B) {

        // get output file content
        String output = "";
        try {
            output = IOUtils.toString(new FileInputStream(A), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        String desired_output = "";
        // plantuml-generator-util/src/test/resources/sequence/0002_basic_super_class_sequence_diagram_with_hide_super_class.txt
        try {
            desired_output = IOUtils.toString(new FileInputStream(B), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return output.replaceAll("\\s+", "").equals(desired_output.replaceAll("\\s+", ""));
    }
}
