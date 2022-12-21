package de.elnarion.util.plantuml.generator.coding;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;


/**
 * The Class CodingTest.
 */
class CodingTest {

	/** The classes. */
	private final JavaClasses classes = new ClassFileImporter().importPackages("de.elnarion.util.plantuml");
	
    /**
     * Test classes should not access standard streams defined by hand.
     */
    @Test
    void testClassesShouldNotAccessStandardStreamsDefinedByHand() {
        noClasses().should(ACCESS_STANDARD_STREAMS).check(classes);
    }

    /**
     * Test classes should not access standard streams from library.
     */
    @Test
    void testClassesShouldNotAccessStandardStreamsFromLibrary() {
        NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.check(classes);
    }

    /**
     * Test classes should not throw generic exceptions.
     */
    @Test
    void testClassesShouldNotThrowGenericExceptions() {
        NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.check(classes);
    }

    /**
     * Test classes should not use java util logging.
     */
    @Test
    void testClassesShouldNotUseJavaUtilLogging() {
        NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING.check(classes);
    }

    /**
     * Test classes should not use jodatime.
     */
    @Test
    void testClassesShouldNotUseJodatime() {
        NO_CLASSES_SHOULD_USE_JODATIME.check(classes);
    }

    /**
     * Test classes should not use field injection.
     */
    @Test
    void testClassesShouldNotUseFieldInjection() {
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classes);
    }

}
