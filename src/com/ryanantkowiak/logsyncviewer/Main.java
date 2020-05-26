package com.ryanantkowiak.logsyncviewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Main class that creates and launches the LogSyncViewer
 *
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class Main
{
    /**
     * Main program entry point
     *
     * @param args Contains an optional list of initial files to open
     */
    public static void main(final String[] args)
    {
        final List<String> validFiles = new ArrayList<String>();

        for (final String a : args)
        {
            if (new File(a).exists())
            {
                validFiles.add(a);
            }
        }

        final LogSyncViewer viewer = new LogSyncViewer(validFiles);
        viewer.run();
    }
}
