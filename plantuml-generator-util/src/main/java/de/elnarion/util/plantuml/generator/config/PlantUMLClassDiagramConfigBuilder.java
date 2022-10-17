package de.elnarion.util.plantuml.generator.config;

import java.util.List;

import de.elnarion.util.plantuml.generator.classdiagram.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.VisibilityType;

public class PlantUMLClassDiagramConfigBuilder {

    private PlantUMLClassDiagramConfig plantUMLConfig = new PlantUMLClassDiagramConfig();

    public PlantUMLClassDiagramConfigBuilder(List<String> paramPackagesToScan) {
        plantUMLConfig.setScanPackages(paramPackagesToScan);
    }

    public PlantUMLClassDiagramConfigBuilder(String paramBlacklistRegexp, List<String> paramPackagesToScan) {
        plantUMLConfig.setBlacklistRegexp(paramBlacklistRegexp);
        plantUMLConfig.setScanPackages(paramPackagesToScan);
    }

    public PlantUMLClassDiagramConfigBuilder(String paramWhitelistRegexp) {
        plantUMLConfig.setWhitelistRegexp(paramWhitelistRegexp);
    }

    public PlantUMLClassDiagramConfigBuilder(List<String> paramPackagesToScan, String paramWhitelistRegexp) {
        plantUMLConfig.setWhitelistRegexp(paramWhitelistRegexp);
        if (paramPackagesToScan != null)
            plantUMLConfig.setScanPackages(paramPackagesToScan);
    }

    /**
     * @param paramDestinationClassLoader
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withClassLoader(ClassLoader paramDestinationClassLoader) {
        plantUMLConfig.setDestinationClassloader(paramDestinationClassLoader);
        return this;
    }

    /**
     * @param paramHideMethods
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withHideMethods(boolean paramHideMethods) {
        plantUMLConfig.setHideMethods(paramHideMethods);
        return this;
    }
    
    public PlantUMLClassDiagramConfigBuilder withJPAAnnotations(boolean paramAddJPAAnnotations) {
    	plantUMLConfig.setAddJPAAnnotations(paramAddJPAAnnotations);
    	return this;
    }

    /**
     * @param paramHideFields
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withHideFieldsParameter(boolean paramHideFields) {
        plantUMLConfig.setHideFields(paramHideFields);
        return this;
    }

    /**
     * @param paramClassesToHide
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withHideClasses(List<String> paramClassesToHide) {
        plantUMLConfig.setHideClasses(paramClassesToHide);
        return this;
    }

    /**
     * @param paramRemoveMethods
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withRemoveMethods(boolean paramRemoveMethods) {
        plantUMLConfig.setRemoveMethods(paramRemoveMethods);
        return this;
    }

    /**
     * @param paramRemoveFields
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withRemoveFields(boolean paramRemoveFields) {
        plantUMLConfig.setRemoveFields(paramRemoveFields);
        return this;
    }

    /**
     * @param paramBlacklistFieldRegexp
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withFieldBlacklistRegexp(String paramBlacklistFieldRegexp) {
        if (paramBlacklistFieldRegexp != null && paramBlacklistFieldRegexp.length() > 0)
            plantUMLConfig.setFieldBlacklistRegexp(paramBlacklistFieldRegexp);
        return this;
    }

    /**
     * @param paramBlacklistMethodRegexp
     * @return PlantUMLConfigBuilder
     */
    public PlantUMLClassDiagramConfigBuilder withMethodBlacklistRegexp(String paramBlacklistMethodRegexp) {
        if (paramBlacklistMethodRegexp != null && paramBlacklistMethodRegexp.length() > 0)
            plantUMLConfig.setMethodBlacklistRegexp(paramBlacklistMethodRegexp);
        return this;
    }

    public PlantUMLClassDiagramConfigBuilder withMaximumMethodVisibility(VisibilityType paramVisibility) {
        plantUMLConfig.setMaxVisibilityMethods(paramVisibility);
        return this;
    }

    public PlantUMLClassDiagramConfigBuilder withMaximumFieldVisibility(VisibilityType paramVisibility) {
        plantUMLConfig.setMaxVisibilityFields(paramVisibility);
        return this;
    }

    public PlantUMLClassDiagramConfigBuilder addFieldClassifierToIgnore(ClassifierType paramClassifier) {
        if (paramClassifier != null)
            plantUMLConfig.getFieldClassifierToIgnore().add(paramClassifier);
        return this;
    }

    public PlantUMLClassDiagramConfigBuilder addFieldClassifiersToIgnore(List<ClassifierType> paramClassiferList) {
        if (paramClassiferList != null)
            plantUMLConfig.getFieldClassifierToIgnore().addAll(paramClassiferList);
        return this;
    }

    public PlantUMLClassDiagramConfigBuilder addMethodClassifierToIgnore(ClassifierType paramClassifier) {
        if (paramClassifier != null)
            plantUMLConfig.getMethodClassifierToIgnore().add(paramClassifier);
        return this;
    }

    public PlantUMLClassDiagramConfigBuilder addMethodClassifiersToIgnore(List<ClassifierType> paramClassiferList) {
        if (paramClassiferList != null)
            plantUMLConfig.getMethodClassifierToIgnore().addAll(paramClassiferList);
        return this;
    }

    public PlantUMLClassDiagramConfigBuilder addAdditionalPlantUmlConfigs(List<String> additionalPlantUmlConfigs) {
        if (additionalPlantUmlConfigs != null)
            plantUMLConfig.getAdditionalPlantUmlConfigs().addAll(additionalPlantUmlConfigs);
        return this;
    }

    /**
     * @return PlantUMLConfig
     */
    public PlantUMLClassDiagramConfig build() {
        return plantUMLConfig;
    }

}