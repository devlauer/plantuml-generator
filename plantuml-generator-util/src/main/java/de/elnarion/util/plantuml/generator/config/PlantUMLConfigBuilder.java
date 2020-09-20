package de.elnarion.util.plantuml.generator.config;

import java.util.List;

import de.elnarion.util.plantuml.generator.classdiagram.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.VisibilityType;

public class PlantUMLConfigBuilder {

    private PlantUMLConfig plantUMLConfig = new PlantUMLConfig();

    public PlantUMLConfigBuilder(List<String> paramPackagesToScan) {
        plantUMLConfig.setScanPackages(paramPackagesToScan);
    }

    public PlantUMLConfigBuilder(String paramBlacklistRegexp, List<String> paramPackagesToScan) {
        plantUMLConfig.setBlacklistRegexp(paramBlacklistRegexp);
        plantUMLConfig.setScanPackages(paramPackagesToScan);
    }

    public PlantUMLConfigBuilder(String paramWhitelistRegexp) {
        plantUMLConfig.setWhitelistRegexp(paramWhitelistRegexp);
    }

    public PlantUMLConfigBuilder(List<String> paramPackagesToScan, String paramWhitelistRegexp) {
        plantUMLConfig.setWhitelistRegexp(paramWhitelistRegexp);
        if (paramPackagesToScan != null)
            plantUMLConfig.setScanPackages(paramPackagesToScan);
    }

    /**
     * @param paramDestinationClassLoader
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withClassLoader(ClassLoader paramDestinationClassLoader) {
        plantUMLConfig.setDestinationClassloader(paramDestinationClassLoader);
        return this;
    }

    /**
     * @param paramHideMethods
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withHideMethods(boolean paramHideMethods) {
        plantUMLConfig.setHideMethods(paramHideMethods);
        return this;
    }
    
    public PlantUMLConfigBuilder withJPAAnnotations(boolean paramAddJPAAnnotations) {
    	plantUMLConfig.setAddJPAAnnotations(paramAddJPAAnnotations);
    	return this;
    }

    /**
     * @param paramHideFields
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withHideFieldsParameter(boolean paramHideFields) {
        plantUMLConfig.setHideFields(paramHideFields);
        return this;
    }

    /**
     * @param paramClassesToHide
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withHideClasses(List<String> paramClassesToHide) {
        plantUMLConfig.setHideClasses(paramClassesToHide);
        return this;
    }

    /**
     * @param paramRemoveMethods
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withRemoveMethods(boolean paramRemoveMethods) {
        plantUMLConfig.setRemoveMethods(paramRemoveMethods);
        return this;
    }

    /**
     * @param paramRemoveFields
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withRemoveFields(boolean paramRemoveFields) {
        plantUMLConfig.setRemoveFields(paramRemoveFields);
        return this;
    }

    /**
     * @param paramBlacklistFieldRegexp
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withFieldBlacklistRegexp(String paramBlacklistFieldRegexp) {
        if (paramBlacklistFieldRegexp != null && paramBlacklistFieldRegexp.length() > 0)
            plantUMLConfig.setFieldBlacklistRegexp(paramBlacklistFieldRegexp);
        return this;
    }

    /**
     * @param paramBlacklistMethodRegexp
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLConfigBuilder withMethodBlacklistRegexp(String paramBlacklistMethodRegexp) {
        if (paramBlacklistMethodRegexp != null && paramBlacklistMethodRegexp.length() > 0)
            plantUMLConfig.setMethodBlacklistRegexp(paramBlacklistMethodRegexp);
        return this;
    }

    public PlantUMLConfigBuilder withMaximumMethodVisibility(VisibilityType paramVisibility) {
        plantUMLConfig.setMaxVisibilityMethods(paramVisibility);
        return this;
    }

    public PlantUMLConfigBuilder withMaximumFieldVisibility(VisibilityType paramVisibility) {
        plantUMLConfig.setMaxVisibilityFields(paramVisibility);
        return this;
    }

    public PlantUMLConfigBuilder addFieldClassifierToIgnore(ClassifierType paramClassifier) {
        if (paramClassifier != null)
            plantUMLConfig.getFieldClassifierToIgnore().add(paramClassifier);
        return this;
    }

    public PlantUMLConfigBuilder addFieldClassifiersToIgnore(List<ClassifierType> paramClassiferList) {
        if (paramClassiferList != null)
            plantUMLConfig.getFieldClassifierToIgnore().addAll(paramClassiferList);
        return this;
    }

    public PlantUMLConfigBuilder addMethodClassifierToIgnore(ClassifierType paramClassifier) {
        if (paramClassifier != null)
            plantUMLConfig.getMethodClassifierToIgnore().add(paramClassifier);
        return this;
    }

    public PlantUMLConfigBuilder addMethodClassifiersToIgnore(List<ClassifierType> paramClassiferList) {
        if (paramClassiferList != null)
            plantUMLConfig.getMethodClassifierToIgnore().addAll(paramClassiferList);
        return this;
    }

    /**
     * @return PlantUMLConfig
     */
    public PlantUMLConfig build() {
        return plantUMLConfig;
    }

}