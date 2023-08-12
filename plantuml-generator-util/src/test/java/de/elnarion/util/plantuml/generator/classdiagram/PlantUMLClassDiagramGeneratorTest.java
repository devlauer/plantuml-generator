package de.elnarion.util.plantuml.generator.classdiagram;

import de.elnarion.util.plantuml.generator.classdiagram.config.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfig;
import de.elnarion.util.plantuml.generator.classdiagram.config.PlantUMLClassDiagramConfigBuilder;
import de.elnarion.util.plantuml.generator.classdiagram.config.VisibilityType;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class PlantUMLClassDiagramGeneratorTest tests the
 * {@link PlantUMLClassDiagramGenerator}.
 */
class PlantUMLClassDiagramGeneratorTest {

    private final ClassLoader classLoader = this.getClass().getClassLoader();

    private static Stream<Arguments> provideBaseRenderingTests() {
        //@formatter:off
		return Stream.of(
				Arguments.of("de.elnarion.test.domain.t0002", "class/0002_class_types.txt"),
				Arguments.of("de.elnarion.test.domain.t0003", "class/0003_class_relationships.txt"),
				Arguments.of("de.elnarion.test.domain.t0005", "class/0005_class_methods.txt")
				);
		//@formatter:on
    }

    /**
     * Test generate diagram with a normal test case with different linked classes
     * and compares the result with the text of the file 0001_general_diagram.txt.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0001GenerateDiagram() throws IOException {
        // tag::hideclasses[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0001");
        List<String> hideClasses = new ArrayList<>(); // <1>
        hideClasses.add("de.elnarion.test.domain.ChildB");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages) // <2>
                .withHideClasses(hideClasses); // <3>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build()); // <4>
        String result = generator.generateDiagramText(); // <5>
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource("class/0001_general_diagram.txt")),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::hideclasses[]
    }

    @ParameterizedTest
    @MethodSource("provideBaseRenderingTests")
    void testBaseRendering(String paramScanpackage, String paramCompareTextFile)
            throws IOException {
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add(paramScanpackage);
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages);
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(paramCompareTextFile)),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
    }

    /**
     * Test 0004 test different field variations.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0004ClassFields() throws IOException {
        // tag::baseclassfieldtypes[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0004");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(".*TestReference",
                scanPackages);
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource("class/0004_class_fields.txt")),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // tag::baseclassfieldtypes[]
    }

    /**
     * Test 0006 test different packages as scan list.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0006DifferentPackages() throws IOException {
        // tag::multiplescanpackages[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0006.pck1"); // <1>
        scanPackages.add("de.elnarion.test.domain.t0006.pck2"); // <2>
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages); // <3>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource("class/0006_different_packages.txt")),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::multiplescanpackages[]
    }

    /**
     * Test 0007 test hide toggles.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0007HideParameters() throws IOException {
        // tag::hideclassesfieldsandmethods[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0007");
        List<String> hideClasses = new ArrayList<>();
        hideClasses.add("de.elnarion.test.domain.t0007.ClassB");
        hideClasses.add("de.elnarion.test.domain.t0007.ClassC");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withHideClasses(hideClasses).withHideFieldsParameter(true).withHideMethods(true); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource("class/0007_hide_parameters.txt")),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::hideclassesfieldsandmethods[]
    }

    /**
     * Test custom classloader.
     *
     * @throws Exception the exception
     */
    @Test
    void test0008Classloader() throws Exception {
        // tag::classloader[]
        String filename = "class/0008_classloader_test.txt";
        String testClassPath = "file:///" + System.getProperty("user.dir") + "/src/test/classes/";
        URL[] classesURLs = new URL[]{new URL(testClassPath)};
        URLClassLoader customClassLoader = new URLClassLoader(classesURLs); // <1>
        customClassLoader.loadClass("de.elnarion.maven.plugin.plantuml.generator.test.domain.ChildA");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.maven.plugin.plantuml.generator.test.domain");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withClassLoader(customClassLoader).withHideFieldsParameter(true).withHideMethods(true); // <2>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::classloader[]
    }

    /**
     * Test classes contained in a package in a jar.
     *
     * @throws Exception the exception
     */
    @Test
    void test0009JarPackage() throws Exception {
        // tag::jartest[]
        String filename = "class/0009_jar_test.txt";
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("org.apache.commons.io.monitor");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withHideFieldsParameter(true).withHideMethods(true);
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::jartest[]
    }

    /**
     * Test classes contained in a package in a jar.
     *
     * @throws Exception the exception
     */
    @Test
    void test0011JarPackageWithBlacklist() throws Exception {
        // tag::blacklistregexp[]
        String filename = "class/0011_jar_test_blacklist.txt";
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("org.apache.commons.io.monitor");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(".*FileEn.*", // <1>
                scanPackages).withHideFieldsParameter(true).withHideMethods(true);
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::blacklistregexp[]
    }

    /**
     * Test 0010 test parameterized aggregation type.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0010ParameterizedAggregationType() throws IOException {
        // tag::parameterizedaggregationtype[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0010");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withHideFieldsParameter(true).withHideMethods(true);
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource("class/0010_parameterized_aggregation_type.txt")),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::parameterizedaggregationtype[]
    }

    /**
     * Test classes contained in a package in a jar.
     *
     * @throws Exception the exception
     */
    @Test
    void test0012JarPackageWithWhitelist() throws Exception {
        // tag::whitelistregexp[]
        String filename = "class/0012_jar_whitelist.txt";
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(null,
                "org\\.apache\\.commons\\.io.*FileAl.*").withHideFieldsParameter(true).withHideMethods(true); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::whitelistregexp[]
    }

    @Test
    void test0012JarPackageWithWhitelistWithoutScanpackagesParameter() throws Exception {
        // tag::whitelistregexpwithoutscanpackagesparameter[]
        String filename = "class/0012_jar_whitelist.txt";
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(
                "org\\.apache\\.commons\\.io.*FileAl.*").withHideFieldsParameter(true).withHideMethods(true); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::whitelistregexpwithoutscanpackagesparameter[]
    }

    @Test
    void test0012JarPackageWithWhitelistWithScanpackagesParameterLimitedToBasePackage() throws Exception {
        // tag::whitelistregexpwithscanpackageslimitedtobasepackage[]
        String filename = "class/0012_jar_whitelist.txt";
        List<String> scanpackages = new ArrayList<>();
        scanpackages.add("org.apache.commons.io");
        PlantUMLClassDiagramConfigBuilder configBuilder = new PlantUMLClassDiagramConfigBuilder(scanpackages,
                "org\\.apache\\.commons\\.io.*FileAl.*").withHideFieldsParameter(true).withHideMethods(true); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(configBuilder.build());
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::whitelistregexpwithscanpackageslimitedtobasepackage[]
    }

    @Test
    void test0013MaxVisibilityFields() throws Exception {
        // tag::maxvisibilityfieldspublic[]
        String filename = "class/0013_max_visibility_fields_public.txt";
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0013");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMaximumFieldVisibility(VisibilityType.PUBLIC).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilityfieldspublic[]

        // tag::maxvisibilityfieldsprotected[]
        config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMaximumFieldVisibility(VisibilityType.PROTECTED).build(); // <1>
        generator = new PlantUMLClassDiagramGenerator(config);
        result = generator.generateDiagramText();
        filename = "class/0013_max_visibility_fields_protected.txt";
        expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilityfieldsprotected[]

        // tag::maxvisibilityfieldspackageprivate[]
        config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMaximumFieldVisibility(VisibilityType.PACKAGE_PRIVATE).build(); // <1>
        generator = new PlantUMLClassDiagramGenerator(config);
        result = generator.generateDiagramText();
        filename = "class/0013_max_visibility_fields_package_private.txt";
        expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilityfieldspackageprivate[]

        // tag::maxvisibilityfieldsprivate[]
        config = new PlantUMLClassDiagramConfigBuilder(scanPackages).withMaximumFieldVisibility(VisibilityType.PRIVATE)
                .build(); // <1>
        generator = new PlantUMLClassDiagramGenerator(config);
        result = generator.generateDiagramText();
        filename = "class/0013_max_visibility_fields_private.txt";
        expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilityfieldsprivate[]
    }

    @Test
    void test0014MaxVisibilityMethods() throws Exception {
        // tag::maxvisibilitymethodspublic[]
        String filename = "class/0014_max_visibility_methods_public.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0014.Testclass");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0014");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMaximumMethodVisibility(VisibilityType.PUBLIC).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilitymethodspublic[]

        // tag::maxvisibilitymethodsprotected[]
        config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMaximumMethodVisibility(VisibilityType.PROTECTED).build(); // <1>
        generator = new PlantUMLClassDiagramGenerator(config);
        result = generator.generateDiagramText();
        filename = "class/0014_max_visibility_methods_protected.txt";
        expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilitymethodsprotected[]

        // tag::maxvisibilitymethodspackageprivate[]
        config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMaximumMethodVisibility(VisibilityType.PACKAGE_PRIVATE).build(); // <1>
        generator = new PlantUMLClassDiagramGenerator(config);
        result = generator.generateDiagramText();
        filename = "class/0014_max_visibility_methods_package_private.txt";
        expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilitymethodspackageprivate[]

        // tag::maxvisibilitymethodsprivate[]
        config = new PlantUMLClassDiagramConfigBuilder(scanPackages).withMaximumMethodVisibility(VisibilityType.PRIVATE)
                .build(); // <1>
        generator = new PlantUMLClassDiagramGenerator(config);
        result = generator.generateDiagramText();
        filename = "class/0014_max_visibility_methods_private.txt";
        expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::maxvisibilitymethodsprivate[]
    }

    @Test
    void test0015RemoveMethods() throws Exception {
        // tag::removemethods[]
        String filename = "class/0015_remove_methods.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0015.Testclass1");
        classLoader.loadClass("de.elnarion.test.domain.t0015.Testclass2");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0015");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages).withRemoveMethods(true)
                .build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::removemethods[]
    }

    @Test
    void test0016RemoveFields() throws Exception {
        // tag::removefields[]
        String filename = "class/0016_remove_fields.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0016.Testclass1");
        classLoader.loadClass("de.elnarion.test.domain.t0016.Testclass2");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0016");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages).withRemoveFields(true)
                .build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::removefields[]
    }

    @Test
    void test0017BlacklistMethods() throws Exception {
        // tag::blacklistmethods[]
        String filename = "class/0017_blacklist_methods.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0017.Testclass1");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0017");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMethodBlacklistRegexp(".*doSomething1.*").build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::blacklistmethods[]
    }

    @Test
    void test0017BlacklistMethodsWithEmptyBlacklist() throws Exception {
        // tag::blacklistmethods[]
        String filename = "class/0017_blacklist_methods_empty_parameter.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0017.Testclass1");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0017");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withMethodBlacklistRegexp("").build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::blacklistmethods[]
    }

    @Test
    void test0018BlacklistFields() throws Exception {
        // tag::blacklistfields[]
        String filename = "class/0018_blacklist_fields.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0018.Testclass1");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0018");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withFieldBlacklistRegexp("test1").build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::blacklistfields[]
    }

    @Test
    void test0019IgnoreClassifierFields() throws Exception {
        // tag::ignoreclassifierfields[]
        String filename = "class/0019_ignore_classifier_fields.txt";
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0019");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .addFieldClassifierToIgnore(ClassifierType.STATIC).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::ignoreclassifierfields[]
    }

    @Test
    void test0019IgnoreMultipleClassifierFields() throws Exception {
        // tag::ignoremultipleclassifierfields[]
        String filename = "class/0019_ignore_classifier_fields.txt";
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0019");
        List<ClassifierType> classifiers = new ArrayList<>();
        classifiers.add(ClassifierType.STATIC);
        classifiers.add(ClassifierType.ABSTRACT);
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .addFieldClassifiersToIgnore(classifiers).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::ignoremultipleclassifierfields[]
    }

    @Test
    void test0020IgnoreClassifierMethods() throws Exception {
        // tag::ignoreclassifiermethods[]
        String filename = "class/0020_ignore_classifier_methods.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0020.Testclass");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0020");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .addMethodClassifierToIgnore(ClassifierType.STATIC).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::ignoreclassifiermethods[]
    }

    @Test
    void test0020IgnoreMultipleClassifierMethods() throws Exception {
        // tag::ignoremultipleclassifiermethods[]
        String filename = "class/0020_ignore_multiple_classifier_methods.txt";
        classLoader.loadClass("de.elnarion.test.domain.t0020.Testclass");
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0020");
        List<ClassifierType> classifiers = new ArrayList<>();
        classifiers.add(ClassifierType.STATIC);
        classifiers.add(ClassifierType.ABSTRACT);
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .addMethodClassifiersToIgnore(classifiers).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::ignoremultipleclassifiermethods[]
    }

    @Test
    void test0021JPAAnnotations() throws Exception {
        // tag::jpaannotations[]
        String filename = "class/0021_jpa_annotations.txt";
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0021");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages).withJPAAnnotations(true)
                .build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::jpaannotations[]
    }

    @Test
    void test0022PrivateFinalFields() throws Exception {
        // tag::combinedhideparametersfieldormethodvisibilityandblacklistmethod[]
        String filename = "class/0022_private_final_field.txt";
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0022");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(
                ".*\\$.*|com.xx.common.converter.BeanConverter|.*\\.metamodel\\..*", scanPackages)
                .withHideMethods(false).withHideFieldsParameter(false)
                .withMaximumFieldVisibility(VisibilityType.PRIVATE).withMaximumMethodVisibility(VisibilityType.PUBLIC)
                .withMethodBlacklistRegexp("(hashCode|equals)").build();
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::combinedhideparametersfieldormethodvisibilityandblacklistmethod[]
    }

    /**
     * Test generate diagram with a normal test case with different linked classes
     * and additional PlantUML configs and compares the result with the text of the
     * file 0023_additional-plant-uml-configs.txt.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0023GenerateDiagramWithAdditionalPlantUmlConfigs() throws IOException {
        // tag::additionalplantumlconfig[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0023");
        List<String> additionalPlantUmlConfigs = new ArrayList<>(); // <1>
        additionalPlantUmlConfigs.add("left to right direction");
        additionalPlantUmlConfigs.add("scale 2/3");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .addAdditionalPlantUmlConfigs(additionalPlantUmlConfigs).build(); // <2>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils
                .toString(Objects.requireNonNull(classLoader.getResource("class/0023_additional-plant-uml-configs.txt")), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::additionalplantumlconfig[]
    }

    /**
     * Test generate diagramm with different class relationships.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0024ClassRelationships() throws IOException {
        // tag::classrelationships[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0024");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages).withRemoveMethods(false)
                .build();
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource("class/0024_class_relationships.txt")),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::classrelationships[]
    }

    /**
     * Test generate diagram with shortened class names in classes, relationships,
     * fields, methods.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0025UseShortClassNames() throws IOException {
        // tag::useshortclassnames[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0025");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withUseShortClassNames(true).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(Objects.requireNonNull(classLoader.getResource("class/0025_use_short_classnames.txt")),
                StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::useshortclassnames[]
    }

    /**
     * Test generate diagram with shortened class names only in fields and methods
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0025UseShortClassNamesInFieldsAndMethods() throws IOException {
        // tag::useshortclassnamesinfieldsandmethods[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0025");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withUseShortClassNamesInFieldsAndMethods(true).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(
                Objects.requireNonNull(classLoader.getResource("class/0025_use_short_classnames_in_fields_and_methods.txt")), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::useshortclassnamesinfieldsandmethods[]
    }

    /**
     * Test generate diagram with different aggregate relationships
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void test0026AggregateRelationships() throws IOException {
        // tag::aggregaterelationships[]
        List<String> scanPackages = new ArrayList<>();
        scanPackages.add("de.elnarion.test.domain.t0026");
        PlantUMLClassDiagramConfig config = new PlantUMLClassDiagramConfigBuilder(scanPackages)
                .withUseShortClassNamesInFieldsAndMethods(true).build(); // <1>
        PlantUMLClassDiagramGenerator generator = new PlantUMLClassDiagramGenerator(config);
        String result = generator.generateDiagramText();
        String expectedDiagramText = IOUtils.toString(
                Objects.requireNonNull(classLoader.getResource("class/0026_different_aggregate_relationships.txt")), StandardCharsets.UTF_8);
        assertNotNull(result);
        assertNotNull(expectedDiagramText);
        assertEquals(expectedDiagramText.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
        // end::aggregaterelationships[]
    }

}
