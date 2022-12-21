package de.elnarion.util.plantuml.generator.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "de.elnarion.util.plantuml")
class ArchitectureTest {

	// internal class diagram classes should only be accessed from main class diagram package or from inside internal itself 
	@ArchTest
	static final ArchRule internalClassDiagramRule = classes().that().resideInAPackage("de.elnarion.util.plantuml.generator.classdiagram.internal").should().onlyBeAccessed()
			.byAnyPackage("de.elnarion.util.plantuml.generator.classdiagram","de.elnarion.util.plantuml.generator.classdiagram.internal");

	// config class diagram classes should only be accessed from main class diagram package or from internal or from inside config itself 
	@ArchTest
	static final ArchRule configClassDiagramRule = classes().that().resideInAPackage("de.elnarion.util.plantuml.generator.classdiagram.config").should().onlyBeAccessed()
			.byAnyPackage("de.elnarion.util.plantuml.generator.classdiagram","de.elnarion.util.plantuml.generator.classdiagram.internal","de.elnarion.util.plantuml.generator.classdiagram.config");

	// main class diagram package should only be accessed from inside itself
	@ArchTest
	static final ArchRule mainClassDiagramRule = classes().that().resideInAPackage("de.elnarion.util.plantuml.generator.classdiagram").should().onlyBeAccessed()
			.byAnyPackage("de.elnarion.util.plantuml.generator.classdiagram");
	
	// internal sequence diagram classes should only be accessed from main sequence diagram package or from inside internal itself 
	@ArchTest
	static final ArchRule internalSequenceDiagramRule = classes().that().resideInAPackage("de.elnarion.util.plantuml.generator.sequencediagram.internal").should().onlyBeAccessed()
	.byAnyPackage("de.elnarion.util.plantuml.generator.sequencediagram","de.elnarion.util.plantuml.generator.sequencediagram.internal");

	// config sequence diagram classes should only be accessed from main sequence diagram package or from internal or from inside config itself 
	@ArchTest
	static final ArchRule configSequenceDiagramRule = classes().that().resideInAPackage("de.elnarion.util.plantuml.generator.sequencediagram.config").should().onlyBeAccessed()
			.byAnyPackage("de.elnarion.util.plantuml.generator.sequencediagram","de.elnarion.util.plantuml.generator.sequencediagram.internal","de.elnarion.util.plantuml.generator.sequencediagram.config");
	
	// sequence diagram exceptions should only be accessed from main sequence diagram package  
	@ArchTest
	static final ArchRule utilSequenceDiagramRule = classes().that().resideInAPackage("de.elnarion.util.plantuml.generator.sequencediagram.exception").should().onlyBeAccessed()
			.byAnyPackage("de.elnarion.util.plantuml.generator.sequencediagram");
	
	// main sequence diagram package should only be accessed from inside itself
	@ArchTest
	static final ArchRule mainSequenceDiagramRule = classes().that().resideInAPackage("de.elnarion.util.plantuml.generator.sequencediagram").should().onlyBeAccessed()
			.byAnyPackage("de.elnarion.util.plantuml.generator.sequencediagram");
	
	// no cycles between packages
	@ArchTest
	static final ArchRule noCyclesRule = slices().matching("de.elnarion.util.plantuml.(*)..").should().beFreeOfCycles();


}
