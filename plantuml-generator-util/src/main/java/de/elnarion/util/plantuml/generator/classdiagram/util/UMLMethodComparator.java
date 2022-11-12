package de.elnarion.util.plantuml.generator.classdiagram.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import de.elnarion.util.plantuml.generator.classdiagram.internal.UMLMethod;

/**
 * The Class UMLMethodComparator is used to sort {@link UMLMethod} objects.
 */
public class UMLMethodComparator implements Comparator<UMLMethod> {

	@Override
	public int compare(UMLMethod o1, UMLMethod o2) {
		// compare method names
		int result = o1.getName().compareTo(o2.getName());
		if (result == 0) {
			// if names are equal compare parameter count
			Integer size1 = o1.getParametersAndTypes().size();
			Integer size2 = o2.getParametersAndTypes().size();
			result = size1.compareTo(size2);
			if (result == 0 && size1 > 0) {
				// if parameter count is equal compare parameter types
				Collection<String> parameterTypes1 = o1.getParametersAndTypes().values();
				Collection<String> parameterTypes2 = o2.getParametersAndTypes().values();
				Iterator<String> it1 = parameterTypes1.iterator();
				Iterator<String> it2 = parameterTypes2.iterator();
				while (it1.hasNext() && it2.hasNext()) {
					result = it1.next().compareTo(it2.next());
					if (result != 0) {
						return result;
					}
				}
			}
			// if names and parameter count and parameter types are equal compare classifier
			if (result == 0) {
				result = o1.getClassifierType().compareTo(o2.getClassifierType());
			}
		}
		return result;
	}
}
