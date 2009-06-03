package com.quui.qlog.core;

/**
 * Enum for the different possible levels of log messages;
 * @author Fabian Steeg (fsteeg)
 */
public enum Level {
    INFO("#CCFF99"),
    DEBUG("#F0F0F0"),
    TRACE("#FFFFFF"),
    FATAL("#CC3333"),
    ERROR("#FFCC66"),
    WARN("#99CCFF"),
    GARBAGE("#999900");
    private String color;

    Level(String color) {
        this.color = color;
    }

    /**
     * @return The color associated with this level
     */
    public String getColor() {
        return color;
    }
    /**
     * @param color The color string
     * @return The level that uses the given color, or null if none matches
     */
    public static Level from(String color) {
        for (Level level : Level.values()) {
            if (level.getColor().equals(color)) {
                return level;
            }
        }
        return null;
    }
}
