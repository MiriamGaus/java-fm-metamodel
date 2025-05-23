package de.vill.conversion;

import de.vill.model.Feature;
import de.vill.model.FeatureModel;
import de.vill.model.Group;
import de.vill.model.LanguageLevel;
import de.vill.util.Constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DropTypeLevel implements IConversionStrategy {
    @Override
    public Set<LanguageLevel> getLevelsToBeRemoved() {
        return new HashSet<>(Collections.singletonList(LanguageLevel.TYPE_LEVEL));
    }

    @Override
    public Set<LanguageLevel> getTargetLevelsOfConversion() {
        return new HashSet<>();
    }

    @Override
    public void convertFeatureModel(final FeatureModel rootFeatureModel, final FeatureModel featureModel) {
        this.traverseFeatures(featureModel.getRootFeature());
    }

    private void traverseFeatures(final Feature feature) {
        feature.setFeatureType(null);
        feature.getAttributes().remove(Constants.TYPE_LEVEL_VALUE);
        feature.getAttributes().remove(Constants.TYPE_LEVEL_LENGTH);

        for (final Group group : feature.getChildren()) {
            for (final Feature subFeature : group.getFeatures()) {
                this.traverseFeatures(subFeature);
            }
        }
    }
}
