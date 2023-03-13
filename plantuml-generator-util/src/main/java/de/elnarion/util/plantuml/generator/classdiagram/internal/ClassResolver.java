package de.elnarion.util.plantuml.generator.classdiagram.internal;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Class ClassResolver.
 */
class ClassResolver {

	/** The destination class loader. */
	private final ClassLoader destinationClassLoader;

	/** The scanpackages. */
	private final List<String> scanpackages;

	/** The blacklist reg exp. */
	private final String blacklistRegExp;

	/** The whitelist reg exp. */
	private final String whitelistRegExp;

	/**
	 * Instantiates a new class resolver.
	 *
	 * @param paramDestinationClassloader the param destination classloader
	 * @param paramScanpackages           the param scanpackages
	 * @param paramBlacklistRegExp        the param blacklist reg exp
	 * @param paramWhitelistRegExp        the param whitelist reg exp
	 */
	public ClassResolver(ClassLoader paramDestinationClassloader, List<String> paramScanpackages,
						 String paramBlacklistRegExp, String paramWhitelistRegExp) {
		destinationClassLoader = paramDestinationClassloader;
		scanpackages = paramScanpackages;
		blacklistRegExp = paramBlacklistRegExp;
		whitelistRegExp = paramWhitelistRegExp;
	}

	/**
	 * Gets the all diagram classes.
	 *
	 * @return the all diagram classes
	 */
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
						scanpackages.toArray(new String[0]))
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
						scanpackages.toArray(new String[0]))
				.scan()) {
			final ClassInfoList allClasses = scanResult.getAllClasses();
			final ClassInfoList result = allClasses
					.filter(ci -> ci.getName().matches(whitelistRegExp));
			return new HashSet<>(result.loadClasses());
		}
	}

}
