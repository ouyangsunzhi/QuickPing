package com.oy.quickping.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityColorManager {
    private static final Map<Integer, Integer> ENTITY_COLORS = new ConcurrentHashMap<>();

    public static void setEntityColor(int entityId, float red, float green, float blue) {
        int r = (int) (red * 255);
        int g = (int) (green * 255);
        int b = (int) (blue * 255);
        int color = (0xFF << 24) | (r << 16) | (g << 8) | b;

        ENTITY_COLORS.put(entityId, color);
    }

    public static int getEntityColor(int entityId) {
        return ENTITY_COLORS.getOrDefault(entityId, -1);
    }

    public static void removeEntityColor(int entityId) {
        ENTITY_COLORS.remove(entityId);
    }
}
