package com.terraforged.mod.feature.manager.template;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.terraforged.mod.feature.manager.FeatureManager;
import com.terraforged.mod.feature.manager.data.DataManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateLoader {

    private final DataManager manager;
    private final Map<ResourceLocation, Template> cache = new HashMap<>();

    public TemplateLoader(DataManager manager) {
        this.manager = manager;
    }

    public List<Template> load(String namespace, JsonArray paths) {
        List<Template> list = new ArrayList<>();
        for (JsonElement element : paths) {
            ResourceLocation location = parsePath(namespace, element.getAsString());
            manager.forEach(location.getPath(), DataManager.NBT, (name, data) -> {
                Template template = cache.get(name);
                if (template == null) {
                    FeatureManager.LOG.debug(" Loading template {}", name);
                    Template.load(data).ifPresent(t -> {
                        cache.put(name, t);
                        list.add(t);
                    });
                } else {
                    list.add(template);
                }
            });
        }
        return list;
    }

    private static ResourceLocation parsePath(String namespace, String path) {
        String location = path;
        int split = location.indexOf(':');
        if (split > 0) {
            namespace = location.substring(0, split);
            location = location.substring(split + 1);
            if (!location.startsWith("structures/")) {
                location = "structures/" + location;
            }
        }
        return new ResourceLocation(namespace, location);
    }
}