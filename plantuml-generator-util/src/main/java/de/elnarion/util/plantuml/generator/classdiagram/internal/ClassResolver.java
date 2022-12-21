package de.elnarion.util.plantuml.generator.classdiagram.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class ClassResolver {

	private ClassLoader destinationClassLoader;
	private List<String> scanpackages;
	private String blacklistRegExp;
	private String whitelistRegExp;
	
	public ClassResolver(ClassLoader paramDestinationClassloader, List<String> paramScanpackages,
			String paramBlacklistRegExp, String paramWhitelistRegExp) {
		destinationClassLoader = paramDestinationClassloader;
		scanpackages = paramScanpackages;
		blacklistRegExp = paramBlacklistRegExp;
		whitelistRegExp = paramWhitelistRegExp;
	}

	public Set<Class<?>> getAllDiagramClasses() {
		if (whitelistRegExp == null)
			return getAllClassesInScanPackages();
		else
			return getAllClassesFromWhiteList();
	}
	
	
	/**
	 * Gets the all classes which are contained in the scanned packages.
	 *
	 * @return Set&lt;Class&lt;?&gt;&gt; - all classes in scanned packages
	 */
	private Set<Class<?>> getAllClassesInScanPackages() {

		try (ScanResult scanResult = new ClassGraph().overrideClassLoaders(destinationClassLoader)
				.enableClassInfo()
				.acceptPackages(
						scanpackages.toArray(new String[scanpackages.size()]))
				.scan()) {
			final ClassInfoList allClasses = scanResult.getAllClasses();
			if (blacklistRegExp != null) {
				final ClassInfoList result = allClasses
						.filter(ci -> !ci.getName().matches(blacklistRegExp));
				return new HashSet<>(result.loadClasses());
			} else {
				return new HashSet<>(allClasses.loadClasses());
			}
		}
	}

	/**
	 * Reads all classes from classpath which match the given whitelist regular
	 * expression and are children of the given packages to scan.
	 *
	 * @return the all classes from white list
	 */
	private Set<Class<?>> getAllClassesFromWhiteList() {
		try (ScanResult scanResult = new ClassGraph().overrideClassLoaders(destinationClassLoader)
				.enableClassInfo()
				.acceptPackages(
						scanpackages.toArray(new String[scanpackages.size()]))
				.scan()) {
			final ClassInfoList allClasses = scanResult.getAllClasses();
			final ClassInfoList result = allClasses
					.filter(ci -> ci.getName().matches(whitelistRegExp));
			return new HashSet<>(result.loadClasses());
		}
	}

}
