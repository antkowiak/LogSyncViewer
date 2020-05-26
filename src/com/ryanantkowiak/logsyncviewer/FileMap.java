package com.ryanantkowiak.logsyncviewer;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileMap
{
    /**
     * List of the filenames that are loaded
     */
    private static List<String> FILE_NAME_LIST = new ArrayList<String>();

    /**
     * List of arrays of log file data
     */
    private static List<String[]> FILE_DATA_LIST = new ArrayList<String[]>();

    /**
     * List of colors for corresponding log files
     */
    private static List<Color> FILE_COLOR_LIST = new ArrayList<Color>();

    /**
     * Map of filenames to the cached log data
     */
    private static Map<String, String[]> CACHE_DATA = new HashMap<String, String[]>();

    /**
     * Adds data from a file to the cache
     *
     * @param fileName  The path of the file to add
     * @param fileColor The color to use for the file
     * @return The index of the file that was added to the cache
     */
    public static int addFile(final String fileName, final Color fileColor)
    {
        if (FILE_NAME_LIST.contains(fileName))
        {
            return FILE_NAME_LIST.indexOf(fileName);
        }

        try
        {
            String[] fileData;

            if (CACHE_DATA.containsKey(fileName))
            {
                fileData = CACHE_DATA.get(fileName);
            }
            else
            {
                final List<String> lines = Files.readAllLines(Paths.get(fileName));
                fileData = lines.toArray(new String[0]);
            }

            FILE_NAME_LIST.add(fileName);
            FILE_DATA_LIST.add(fileData);
            FILE_COLOR_LIST.add(fileColor);

            CACHE_DATA.put(fileName, fileData);

            final int index = FILE_NAME_LIST.size() - 1;
            return index;
        }
        catch (final Exception e)
        {
            return -1;
        }
    }

    /**
     * Returns true if the given filename is cached
     *
     * @param fileName The file to check if is already cached
     * @return true if the cache contains the file
     */
    public static boolean containsFileName(final String fileName)
    {
        return FILE_NAME_LIST.contains(fileName);
    }

    /**
     * Returns the color that corresponds to the given file index
     *
     * @param fileNum The index of the log file
     * @return The color for the given file index
     */
    public static Color getFileColor(final int fileNum)
    {
        return FILE_COLOR_LIST.get(fileNum);
    }

    /**
     * Returns the text data for the log entry of the given file index and line
     * number
     *
     * @param fileNum The file index of the log file
     * @param lineNum The line number for the log entry
     * @return The text data that corresponds to the given file and line
     */
    public static String getFileData(final int fileNum, final int lineNum)
    {
        return (FILE_DATA_LIST.get(fileNum))[lineNum];
    }

    /**
     * Returns the file name for the given file index
     *
     * @param fileNum The given file index
     * @return The file name that corresponds to the index
     */
    public static String getFileName(final int fileNum)
    {
        return FILE_NAME_LIST.get(fileNum);
    }

    /**
     * Returns the number of lines for a given file index
     *
     * @param fileNum The given file index
     * @return The number of log entry lines in the given file index
     */
    public static int getFileNumLines(final int fileNum)
    {
        return FILE_DATA_LIST.get(fileNum).length;
    }

    /**
     * Returns statistic data about loaded files.
     *
     * @return The statistic data about the loaded files
     */
    public static String getStatistics()
    {
        final long numCachedFiles = CACHE_DATA.entrySet().size();

        long numLogEntries = 0;
        long numBytes = 0;

        for (final Entry<String, String[]> entry : CACHE_DATA.entrySet())
        {
            final String[] value = entry.getValue();

            numLogEntries += value.length;

            for (final String line : value)
            {
                numBytes += line.length();
            }
        }

        final StringBuilder sb = new StringBuilder(System.lineSeparator() + System.lineSeparator());
        sb.append("Files Cached: " + numCachedFiles + System.lineSeparator());
        sb.append("Log Entries:  " + String.format("%,d", numLogEntries) + System.lineSeparator());
        sb.append("Bytes Cached: " + String.format("%,d", numBytes) + System.lineSeparator());

        return sb.toString();
    }

    /**
     * Resets the FileMap cache, and purges ALL loaded file data.
     */
    public static void purge()
    {
        reset();
        CACHE_DATA = new HashMap<String, String[]>();
    }

    /**
     * Refresh the FileMap cache. Reloads all log files from disk.
     */
    public static void refresh()
    {
        for (int i = 0 ; i < FILE_NAME_LIST.size() ; ++i)
        {
            final String fileName = FILE_NAME_LIST.get(i);
            
            try
            {
                String[] fileData;

                final List<String> lines = Files.readAllLines(Paths.get(fileName));
                fileData = lines.toArray(new String[0]);

                FILE_DATA_LIST.set(i, fileData);
                CACHE_DATA.put(fileName, fileData);

            }
            catch (final Exception e)
            {
                FILE_DATA_LIST.set(i, new String [0]);
                CACHE_DATA.put(fileName, new String [0]);
            }
        }
    }
    
    /**
     * Reset the FileMap cache. Resets the list of files, file data, and color data.
     */
    public static void reset()
    {
        FILE_NAME_LIST = new ArrayList<String>();
        FILE_DATA_LIST = new ArrayList<String[]>();
        FILE_COLOR_LIST = new ArrayList<Color>();
    }

    /**
     * Constructor (private for singleton)
     */
    private FileMap()
    {
    }

}
