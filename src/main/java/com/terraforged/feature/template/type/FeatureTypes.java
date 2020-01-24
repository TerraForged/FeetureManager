package com.terraforged.feature.template.type;

import com.terraforged.feature.template.placement.Placement;
import com.terraforged.feature.template.placement.TreePlacement;
import net.minecraft.world.gen.feature.Feature;

import java.util.*;

public class FeatureTypes {

    private static final Map<String, FeatureType> types = new HashMap<>();
    private static final Map<FeatureType, Set<Feature<?>>> features = new HashMap<>();

    public static final FeatureType ANY = register("any", Placement.ANY);
    public static final FeatureType TREE = register("tree", TreePlacement.PLACEMENT);

    public static FeatureType getType(String type) {
        return types.getOrDefault(type, ANY);
    }

    public static void register(FeatureType type, Feature<?> feature) {
        features.computeIfAbsent(type, t -> new HashSet<>()).add(feature);
    }

    protected static Collection<Feature<?>> getFeatures(FeatureType type) {
        return features.getOrDefault(type, Collections.emptySet());
    }

    private static FeatureType register(String name, Placement placement) {
        return register(new FeatureType(name, placement));
    }

    private static FeatureType register(FeatureType type) {
        types.put(type.getName(), type);
        return type;
    }
}