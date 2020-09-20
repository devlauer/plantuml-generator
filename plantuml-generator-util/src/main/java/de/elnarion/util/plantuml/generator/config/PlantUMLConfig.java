package de.elnarion.util.plantuml.generator.config;

import java.util.ArrayList;
import java.util.List;

import de.elnarion.util.plantuml.generator.classdiagram.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.VisibilityType;

public class PlantUMLConfig {

	private ClassLoader destinationClassloader = null;
	private List<String> scanPackages = new ArrayList<>();
	private String blacklistRegexp = null;
	private String whitelistRegexp = null;
	private List<String> hideClasses = null;
	private boolean hideFields = false;
	private boolean hideMethods = false;
	private boolean removeFields = false;
	private boolean removeMethods = false;
	private boolean addJPAAnnotations = false;
	private String fieldBlacklistRegexp = null;
	private String methodBlacklistRegexp = null;
	private VisibilityType maxVisibilityFields = VisibilityType.PRIVATE;
	private VisibilityType maxVisibilityMethods = VisibilityType.PRIVATE;
	private List<ClassifierType> fieldClassifierToIgnore = new ArrayList<>();
	private List<ClassifierType> methodClassifierToIgnore = new ArrayList<>();

	protected PlantUMLConfig() {
		// default constructor with protected visibility because of corresponding
		// builder
	}

	public List<ClassifierType> getMethodClassifierToIgnore() {
		return methodClassifierToIgnore;
	}

	public void setMethodClassifierToIgnore(List<ClassifierType> methodClassifierToIgnore) {
		this.methodClassifierToIgnore = methodClassifierToIgnore;
	}

	public List<ClassifierType> getFieldClassifierToIgnore() {
		return fieldClassifierToIgnore;
	}

	public void setFieldClassifierToIgnore(List<ClassifierType> fieldClassifierToIgnore) {
		this.fieldClassifierToIgnore = fieldClassifierToIgnore;
	}

	/**
	 * @return ClassLoader
	 */
	public ClassLoader getDestinationClassloader() {
		return destinationClassloader;
	}

	
	/** 
	 * @param destinationClassloader
	 */
	protected void setDestinationClassloader(ClassLoader destinationClassloader) {
		this.destinationClassloader = destinationClassloader;
	}

	
	/** 
	 * @return List<String>
	 */
	public List<String> getScanPackages() {
		return scanPackages;
	}

	
	/** 
	 * @param scanPackages
	 */
	protected void setScanPackages(List<String> scanPackages) {
		this.scanPackages = scanPackages;
	}

	
	/** 
	 * @return String
	 */
	public String getBlacklistRegexp() {
		return blacklistRegexp;
	}

	
	/** 
	 * @param blacklistRegexp
	 */
	protected void setBlacklistRegexp(String blacklistRegexp) {
		this.blacklistRegexp = blacklistRegexp;
	}

	
	/** 
	 * @return String
	 */
	public String getWhitelistRegexp() {
		return whitelistRegexp;
	}

	
	/** 
	 * @param whitelistRegexp
	 */
	protected void setWhitelistRegexp(String whitelistRegexp) {
		this.whitelistRegexp = whitelistRegexp;
	}

	
	/** 
	 * @return List<String>
	 */
	public List<String> getHideClasses() {
		return hideClasses;
	}

	
	/** 
	 * @param hideClasses
	 */
	protected void setHideClasses(List<String> hideClasses) {
		this.hideClasses = hideClasses;
	}

	
	/** 
	 * @return boolean
	 */
	public boolean isHideFields() {
		return hideFields;
	}

	
	/** 
	 * @param hideFields
	 */
	protected void setHideFields(boolean hideFields) {
		this.hideFields = hideFields;
	}

	
	/** 
	 * @return boolean
	 */
	public boolean isHideMethods() {
		return hideMethods;
	}

	
	/** 
	 * @param hideMethods
	 */
	protected void setHideMethods(boolean hideMethods) {
		this.hideMethods = hideMethods;
	}

	
	/** 
	 * @return boolean
	 */
	public boolean isRemoveFields() {
		return removeFields;
	}

	
	/** 
	 * @param removeFields
	 */
	protected void setRemoveFields(boolean removeFields) {
		this.removeFields = removeFields;
	}

	
	/** 
	 * @return boolean
	 */
	public boolean isRemoveMethods() {
		return removeMethods;
	}

	
	/** 
	 * @param removeMethods
	 */
	protected void setRemoveMethods(boolean removeMethods) {
		this.removeMethods = removeMethods;
	}

		
	/** 
	 * @return String
	 */
	public String getFieldBlacklistRegexp() {
		return fieldBlacklistRegexp;
	}

	
	/** 
	 * @param fieldBlacklistRegexp
	 */
	protected void setFieldBlacklistRegexp(String fieldBlacklistRegexp) {
		this.fieldBlacklistRegexp = fieldBlacklistRegexp;
	}

	
	/** 
	 * @return String
	 */
	public String getMethodBlacklistRegexp() {
		return methodBlacklistRegexp;
	}

	
	/** 
	 * @param methodBlacklistRegexp
	 */
	protected void setMethodBlacklistRegexp(String methodBlacklistRegexp) {
		this.methodBlacklistRegexp = methodBlacklistRegexp;
	}

	
	/** 
	 * @return VisibilityType
	 */
	public VisibilityType getMaxVisibilityFields() {
		return maxVisibilityFields;
	}

	
	/** 
	 * @param maxVisibilityFields
	 */
	public void setMaxVisibilityFields(VisibilityType maxVisibilityFields) {
		this.maxVisibilityFields = maxVisibilityFields;
	}

	
	/** 
	 * @return VisibilityType
	 */
	public VisibilityType getMaxVisibilityMethods() {
		return maxVisibilityMethods;
	}

	
	/** 
	 * @param maxVisibilityMethods
	 */
	public void setMaxVisibilityMethods(VisibilityType maxVisibilityMethods) {
		this.maxVisibilityMethods = maxVisibilityMethods;
	}

	public boolean isAddJPAAnnotations() {
		return addJPAAnnotations;
	}

	public void setAddJPAAnnotations(boolean addJPAAnnotations) {
		this.addJPAAnnotations = addJPAAnnotations;
	}

}