package com.ryanantkowiak.logsyncviewer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the colors for displaying different log files, the order
 * that the colors are chosen, and caches specific colors for a given log file
 * name
 *
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class ColorPicker
{
    /**
     * Array of the colors to use
     */
    private static Color[] COLORS = new Color[] { new Color(224, 224, 224), new Color(255, 153, 153),
            new Color(255, 204, 153), new Color(255, 255, 153), new Color(204, 255, 153), new Color(153, 255, 153),
            new Color(153, 255, 204), new Color(153, 255, 255), new Color(153, 204, 255), new Color(153, 153, 255),
            new Color(204, 153, 255), new Color(255, 153, 255),

            new Color(255, 102, 102), new Color(255, 178, 102), new Color(255, 255, 102), new Color(178, 255, 102),
            new Color(102, 255, 102), new Color(102, 255, 178), new Color(102, 255, 255), new Color(102, 178, 255),
            new Color(102, 102, 255), new Color(178, 102, 255), new Color(255, 102, 255), new Color(102, 178, 255),

            new Color(255, 204, 204), new Color(255, 229, 204), new Color(255, 255, 204), new Color(229, 255, 204),
            new Color(204, 255, 204), new Color(204, 255, 229), new Color(204, 255, 255), new Color(204, 229, 255),
            new Color(204, 204, 255), new Color(229, 204, 255), new Color(255, 204, 255), new Color(255, 204, 229),
            new Color(255, 255, 255) };

    /**
     * The last color index (incremented when choosing a new color)
     */
    private static short COLOR_INDEX = -1;

    /**
     * Map of filenames to the color to be displayed for each file
     */
    private static Map<String, Color> COLOR_MAP = new HashMap<String, Color>();

    /**
     * Returns the color that corresponds to a given log file path
     *
     * @param logFilePath The log file path for which a color will be chosen and
     *                    returned
     * @return The color for the file
     */
    public static Color get(final String logFilePath)
    {
        if (COLOR_MAP.containsKey(logFilePath))
        {
            return COLOR_MAP.get(logFilePath);
        }

        ++COLOR_INDEX;
        final Color color = COLORS[COLOR_INDEX % COLORS.length];
        COLOR_MAP.put(logFilePath, color);
        return color;
    }

    /**
     * Resets the map of filenames to colors
     */
    public static void reset()
    {
        COLOR_INDEX = -1;
        COLOR_MAP = new HashMap<String, Color>();
    }

    /**
     * Private constructor (singleton)
     */
    private ColorPicker()
    {
    }
}
