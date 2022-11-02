package com.ryanantkowiak.logsyncviewer;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class represents the data model for each of the individual log line
 * items. It is defined by indexes into a file cache and the line number of a
 * particular file. This class also caches the time-stamp of the log entry as a
 * "long" for optimized comparisons against other log entries.
 *
 * @author Ryan Antkowiak 
 *
 */
public class LogSyncListItem implements Comparable<LogSyncListItem>
{
    /**
     * Static instance of an object that can parse the time-stamp of a log entry
     */
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd HH:mm:ss");
    //private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");

    /**
     * Returns the current format of the time-stamp parser
     *
     * @return the current string format of the time-stamp parser
     */
    public static String getDateFormat()
    {
        return DATE_FORMAT.toPattern();
    }

    /**
     * Sets the format of the time-stamp parser
     *
     * @param newFormat The new string format of the time-stamp parser to use
     */
    public static void setDateFormat(final String newFormat)
    {
        DATE_FORMAT = new SimpleDateFormat(newFormat);
    }

    /**
     * The number of ms since the epoch for this log entry
     */
    private long m_timeStamp = Long.MAX_VALUE;

    /**
     * Index into cached list of files for this log entry
     */
    private final int m_fileNum;

    /**
     * Line number for this log entry
     */
    private final int m_lineNum;

    /**
     * Constructor. Crates the LogSyncListItem with the provided file and line
     * indexes
     *
     * @param fileNum_ Index into map of cached files for this log file
     * @param lineNum_ The line number of this log entry
     */
    public LogSyncListItem(final int fileNum_, final int lineNum_)
    {
        m_fileNum = fileNum_;
        m_lineNum = lineNum_;
    }

    /**
     * Compare two LogSyncListItem objects. The comparison is first based on
     * time-stamp, then based on filename index, and then finally based on line
     * number.
     *
     * @param o The LogSyncListItem that will be compared against this object
     * @return The results of the comparison. (0 if equal, negative if less-than,
     *         positive if greater-than)
     */
    @Override
    public int compareTo(final LogSyncListItem o)
    {
        final long timeStampDiff = getTimestamp() - o.getTimestamp();

        if (timeStampDiff != 0)
        {
            return (timeStampDiff > 0) ? 1 : -1;
        }

        final String f1 = FileMap.getFileName(m_fileNum);
        final String f2 = FileMap.getFileName(o.m_fileNum);

        final int cmpF = f1.compareTo(f2);

        if (cmpF != 0)
        {
            return cmpF;
        }

        return (m_lineNum - o.m_lineNum);
    }

    /**
     * Returns the color that corresponds to this log entry
     *
     * @return The color of this log entry
     */
    public Color getColor()
    {
        return FileMap.getFileColor(m_fileNum);
    }

    /**
     * Returns the textual data of this log entry
     *
     * @return The textual data of this log entry
     */
    public String getText()
    {
        return FileMap.getFileData(m_fileNum, m_lineNum);
    }

    /**
     * Returns the time-stamp of this log entry
     *
     * @return the time-stamp of this log entry
     */
    public long getTimestamp()
    {
        if (m_timeStamp != Long.MAX_VALUE)
        {
            return m_timeStamp;
        }

        m_timeStamp = 0;

        final String line = getText();

        if (line.length() >= 21)
        {
            try
            {
                m_timeStamp = DATE_FORMAT.parse(line.substring(0, 21)).getTime();
            }
            catch (final ParseException e)
            {
            }
        }

        return m_timeStamp;
    }

    /**
     * Returns the tool-tip text of this log entry. It will be the actual filename
     * and line number of the log entry
     *
     * @return the tool-tip text of this log entry
     */
    public String getToolTip()
    {
        return FileMap.getFileName(m_fileNum) + ":" + (m_lineNum + 1);
    }

    /**
     * Converts this object to a string
     *
     * @return The string representation of this object
     */
    @Override
    public String toString()
    {
        return getText();
    }
}
