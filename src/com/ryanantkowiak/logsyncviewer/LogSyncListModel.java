package com.ryanantkowiak.logsyncviewer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;

/**
 * Data model for the individual log entries
 *
 * @author Ryan Antkowiak 
 *
 */
public class LogSyncListModel extends DefaultListModel<LogSyncListItem>
{
    /**
     * Version of serialized data (required from parent classes)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cache of the list of files that are added for loading
     */
    private List<String> m_cachedFileList = null;

    /**
     * Compares two lists of strings to see if they are equivalent
     *
     * @param listA The first list to compare
     * @param listB The second list to compare
     * @return True if both lists contain the same strings, in the same order
     */
    private static boolean listsEqual(final List<String> listA, final List<String> listB)
    {
        if ((listA == null) || (listB == null))
        {
            return false;
        }

        if (listA.size() != listB.size())
        {
            return false;
        }

        for (int i = 0; i < listA.size(); ++i)
        {
            if (listA.get(i) != listB.get(i))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Refreshes the data by re-reading all log files from disk.
     */
    public void refresh()
    {
        FileMap.refresh();

        removeAllElements();

        final List<LogSyncListItem> elementsToAdd = new ArrayList<LogSyncListItem>();

        for (final String file : m_cachedFileList)
        {
            final Color color = ColorPicker.get(file);

            final int fileIndex = FileMap.addFile(file, color);

            if (fileIndex != -1)
            {
                final int numLines = FileMap.getFileNumLines(fileIndex);

                for (int i = 0; i < numLines; ++i)
                {
                    elementsToAdd.add(new LogSyncListItem(fileIndex, i));
                }
            }
        }

        Collections.sort(elementsToAdd);

        for (final LogSyncListItem lsli : elementsToAdd)
        {
            addElement(lsli);
        }

        fireContentsChanged(this, 0, elementsToAdd.size());
    }
    
    /**
     * Reloads the data inside the data model, using the given list of files. Sorts
     * the data according to time-stamp.
     *
     * @param files The log files to load data from
     */
    public void reload(final List<String> files)
    {
        if (listsEqual(m_cachedFileList, files))
        {
            fireContentsChanged(this, 0, getSize());
            return;
        }

        m_cachedFileList = files;

        removeAllElements();

        final List<LogSyncListItem> elementsToAdd = new ArrayList<LogSyncListItem>();

        for (final String file : files)
        {
            final Color color = ColorPicker.get(file);

            final int fileIndex = FileMap.addFile(file, color);

            if (fileIndex != -1)
            {
                final int numLines = FileMap.getFileNumLines(fileIndex);

                for (int i = 0; i < numLines; ++i)
                {
                    elementsToAdd.add(new LogSyncListItem(fileIndex, i));
                }
            }
        }

        Collections.sort(elementsToAdd);

        for (final LogSyncListItem lsli : elementsToAdd)
        {
            addElement(lsli);
        }

        fireContentsChanged(this, 0, elementsToAdd.size());
    }
}
