package com.ryanantkowiak.logsyncviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 * The LogSyncViewer class implements the main window that allows the user to
 * view multiple log files in a single window, sorted by a time-stamp that
 * precedes each line. Files are color coded for ease of viewing.
 *
 * @author Ryan Antkowiak 
 *
 */
public class LogSyncViewer implements ActionListener, ItemListener
{
    /**
     * The current version of the LogSyncViewer program
     */
    public static final String VERSION_INFO = "1.0.2019.09.05";
    /**
     * Default path where log files can be loaded from
     */

    private static final String DEFAULT_PATH = "C:\\logs\\";

    /**
     * Data model of all of the line items to display
     */
    private final LogSyncListModel m_listModel;

    /**
     * Renderer for each line item to display in the list
     */
    private final LogSyncListItemRenderer m_logItemRenderer;

    /**
     * The Java Swing List UI element for displaying all of the log entries
     */
    private JList<LogSyncListItem> m_listView;

    /**
     * The search panel at the top of the viewer
     */
    private final SearchPanel m_searchPanel;

    /**
     * The menu at the top of the window for toggling viewed files
     */
    private JMenu m_viewMenu;

    /**
     * List of menu items for toggling files to view or hide
     */
    private List<JCheckBoxMenuItem> m_viewMenuCheckBoxes;

    /**
     * The main window frame
     */
    private JFrame m_frame;

    /**
     * Constructor. Initialized the LogSyncViewer
     *
     * @param files Optional list of files to load at start-up
     */
    public LogSyncViewer(final List<String> files)
    {
        m_viewMenuCheckBoxes = new ArrayList<JCheckBoxMenuItem>();

        if (files != null)
        {
            for (final String f : files)
            {
                final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(f, true);
                checkBoxMenuItem.addItemListener(this);
                checkBoxMenuItem.setOpaque(true);
                checkBoxMenuItem.setBackground(ColorPicker.get(f));
                m_viewMenuCheckBoxes.add(checkBoxMenuItem);
            }
        }

        m_listModel = new LogSyncListModel();
        m_logItemRenderer = new LogSyncListItemRenderer();
        m_searchPanel = new SearchPanel(this);
    }

    /**
     * Invoked when an action occurs
     *
     * @param e The ActionEvent that occurred
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        final JMenuItem menuItem = (JMenuItem) e.getSource();

        if (menuItem != null)
        {
            if (menuItem.getText() == "Open Files...")
            {
                menuActionOpenFiles();
            }
            else if (menuItem.getText() == "Add Files...")
            {
                menuActionAddFiles();
            }
            else if (menuItem.getText() == "Close Files")
            {
                menuActionCloseFiles();
            }
            else if (menuItem.getText() == "Refresh Files")
            {
                menuActionRefresh();
            }
            else if (menuItem.getText() == "Export...")
            {
                menuActionExport();
            }
            else if (menuItem.getText() == "Exit")
            {
                menuActionExit();
            }
            else if (menuItem.getText() == "Select All")
            {
                menuActionSelectAll();
            }
            else if (menuItem.getText() == "Deselect All")
            {
                menuActionDeselectAll();
            }
            else if (menuItem.getText() == "Copy Selection")
            {
                menuActionCopySelection();
            }
            else if (menuItem.getText() == "Find...")
            {
                menuActionFind();
            }
            else if (menuItem.getText() == "Find Next")
            {
                menuActionFindNext();
            }
            else if (menuItem.getText() == "Find Previous")
            {
                menuActionFindPrevious();
            }
            else if (menuItem.getText() == "Check All")
            {
                menuActionCheckAll();
            }
            else if (menuItem.getText() == "Uncheck All")
            {
                menuActionUncheckAll();
            }
            else if (menuItem.getText() == "Increase Font Size")
            {
                menuActionIncreaseFontSize();
            }
            else if (menuItem.getText() == "Decrease Font Size")
            {
                menuActionDecreaseFontSize();
            }
            else if (menuItem.getText() == "Date/Time Format")
            {
                menuActionDateTimeFormat();
            }
            else if (menuItem.getText() == "About")
            {
                menuActionAbout();
            }
        }
    }

    /**
     * Creates the menu bar at the top of the log viewer
     */
    private void createMenu()
    {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        menuBar.add(file);

        final JMenuItem open = new JMenuItem("Open Files...");
        final JMenuItem add = new JMenuItem("Add Files...");
        final JMenuItem close = new JMenuItem("Close Files");
        final JMenuItem refresh = new JMenuItem("Refresh Files");
        final JMenuItem export = new JMenuItem("Export...");
        final JMenuItem exit = new JMenuItem("Exit");

        file.add(open);
        file.add(add);
        file.add(close);
        file.add(refresh);
        file.addSeparator();
        file.add(export);
        file.addSeparator();
        file.add(exit);

        open.addActionListener(this);
        add.addActionListener(this);
        close.addActionListener(this);
        refresh.addActionListener(this);
        export.addActionListener(this);
        exit.addActionListener(this);

        open.setMnemonic(KeyEvent.VK_O);
        add.setMnemonic(KeyEvent.VK_A);
        close.setMnemonic(KeyEvent.VK_C);
        refresh.setMnemonic(KeyEvent.VK_H);
        export.setMnemonic(KeyEvent.VK_R);
        exit.setMnemonic(KeyEvent.VK_X);

        open.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        close.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        refresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        exit.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        export.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        open.setToolTipText("Replaces all loaded log files with a whole new set of log files that you open");
        add.setToolTipText("Adds additional log files to the set of log files you are already viewing");
        close.setToolTipText("Closes all loaded log files");
        refresh.setToolTipText("Reloads all log file data from disk");
        export.setToolTipText("Export currently visible log to a new combined log file");
        exit.setToolTipText("Exits the LogSync Viewer prgram");

        final JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        menuBar.add(edit);

        final JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(this);
        selectAll.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        selectAll.setMnemonic(KeyEvent.VK_A);
        edit.add(selectAll);

        final JMenuItem deselectAll = new JMenuItem("Deselect All");
        deselectAll.addActionListener(this);
        deselectAll.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        deselectAll.setMnemonic(KeyEvent.VK_D);
        edit.add(deselectAll);

        final JMenuItem copy = new JMenuItem("Copy Selection");
        copy.addActionListener(this);
        copy.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        copy.setMnemonic(KeyEvent.VK_C);
        edit.add(copy);

        edit.addSeparator();

        final JMenuItem find = new JMenuItem("Find...");
        find.addActionListener(this);
        find.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        find.setMnemonic(KeyEvent.VK_F);
        edit.add(find);

        final JMenuItem findNext = new JMenuItem("Find Next");
        findNext.addActionListener(this);
        findNext.setMnemonic(KeyEvent.VK_N);
        findNext.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        edit.add(findNext);

        final JMenuItem findPrev = new JMenuItem("Find Previous");
        findPrev.addActionListener(this);
        findPrev.setMnemonic(KeyEvent.VK_P);
        findPrev.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        edit.add(findPrev);

        m_viewMenu = new JMenu("View");
        m_viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(m_viewMenu);

        final JMenu format = new JMenu("Format");
        format.setMnemonic(KeyEvent.VK_O);
        menuBar.add(format);

        final JMenuItem increaseFontSize = new JMenuItem("Increase Font Size");
        increaseFontSize.addActionListener(this);
        increaseFontSize.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        increaseFontSize.setMnemonic(KeyEvent.VK_I);
        format.add(increaseFontSize);

        final JMenuItem decreaseFontSize = new JMenuItem("Decrease Font Size");
        decreaseFontSize.addActionListener(this);
        decreaseFontSize.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        decreaseFontSize.setMnemonic(KeyEvent.VK_D);
        format.add(decreaseFontSize);

        format.addSeparator();

        final JMenuItem dateFormat = new JMenuItem("Date/Time Format");
        dateFormat.addActionListener(this);
        dateFormat.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        dateFormat.setMnemonic(KeyEvent.VK_T);
        format.add(dateFormat);

        final JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        menuBar.add(help);

        final JMenuItem about = new JMenuItem("About");
        about.addActionListener(this);
        about.setMnemonic(KeyEvent.VK_A);
        help.add(about);

        m_frame.setJMenuBar(menuBar);
    }

    /**
     * Returns the menu item that corresponds to one of the toggle-files in the View
     * menu
     *
     * @param item The string description of the menu item to get
     * @return the menu item
     */
    private JCheckBoxMenuItem getViewMenuItem(final String item)
    {
        if (m_viewMenuCheckBoxes != null)
        {
            for (final JCheckBoxMenuItem i : m_viewMenuCheckBoxes)
            {
                if ((i != null) && (i.getText() == item))
                {
                    return i;
                }
            }
        }

        return null;
    }

    /**
     * Hides the search panel
     */
    public void handleFindHide()
    {
        m_searchPanel.hideSearchPanel();
    }

    /**
     * Handles the item state change event. This handles the case when a file is
     * toggled on/off in the View menu.
     *
     * @param e The ItemEvent
     */
    @Override
    public void itemStateChanged(final ItemEvent e)
    {
        reload();
    }

    /**
     * Handles the user selecting the "About" menu item
     */
    private void menuActionAbout()
    {
        final String stats = FileMap.getStatistics();

        JOptionPane.showMessageDialog(m_frame, "Created by Ryan Antkowiak\nVersion: " + VERSION_INFO + stats,
                "About LogSync Viewer", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles the user selecting the "Add Files" menu item
     */
    private void menuActionAddFiles()
    {
        final JFileChooser jfc = new JFileChooser(DEFAULT_PATH);
        jfc.setDialogTitle("Select log file(s) to add:");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(true);
        final int retVal = jfc.showOpenDialog(m_frame);

        if (retVal == JFileChooser.APPROVE_OPTION)
        {
            final File[] files = jfc.getSelectedFiles();

            for (final File f : files)
            {
                final String sFile = f.getAbsolutePath();

                if ((new File(sFile)).exists())
                {
                    if ((getViewMenuItem(sFile) == null) && (FileMap.containsFileName(sFile) == false))
                    {
                        final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(sFile, true);
                        checkBoxMenuItem.addItemListener(this);
                        checkBoxMenuItem.setOpaque(true);
                        checkBoxMenuItem.setBackground(ColorPicker.get(sFile));
                        m_viewMenuCheckBoxes.add(checkBoxMenuItem);
                    }
                }
            }
        }

        reload();
    }

    /**
     * Handles the user selecting the "Check All" menu item
     */
    private void menuActionCheckAll()
    {
        if (m_viewMenuCheckBoxes != null)
        {
            for (final JCheckBoxMenuItem i : m_viewMenuCheckBoxes)
            {
                i.removeItemListener(this);
                i.setSelected(true);
                i.addItemListener(this);
            }

            reload();
        }
    }

    /**
     * Handles the user selecting the "Close Files" menu item
     */
    private void menuActionCloseFiles()
    {
        m_viewMenuCheckBoxes = new ArrayList<JCheckBoxMenuItem>();
        reload();
        ColorPicker.reset();
        FileMap.purge();
    }

    /**
     * Handles the user selecting the "Copy Selection" menu item
     */
    private void menuActionCopySelection()
    {
        final List<LogSyncListItem> items = m_listView.getSelectedValuesList();

        final StringBuilder sb = new StringBuilder();

        for (final LogSyncListItem i : items)
        {
            sb.append(i.getText() + System.lineSeparator());
        }

        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(sb.toString()), null);
    }

    /**
     * Handles the user selecting the "Date/Time Format" menu item
     */
    private void menuActionDateTimeFormat()
    {
        final String newFmt = (String) JOptionPane.showInputDialog(m_frame, "Enter New Date/Time Format:",
                "Date/Time Format", JOptionPane.QUESTION_MESSAGE, null, null, LogSyncListItem.getDateFormat());

        if ((newFmt != null) && (newFmt.length() > 0))
        {
            LogSyncListItem.setDateFormat(newFmt);
            reload();
        }
    }

    /**
     * Handles the user selecting the "Decrease Font Size" menu item
     */
    private void menuActionDecreaseFontSize()
    {
        m_logItemRenderer.decreaseFontSize();
        reload();
    }

    /**
     * Handles the user selecting the "Deselect All" menu item
     */
    private void menuActionDeselectAll()
    {
        m_listView.clearSelection();
    }

    /**
     * Handles the user selecting the "Exit" menu item
     */
    private void menuActionExit()
    {
        System.exit(0);
    }

    /**
     * Handles the user selecting the "Export" menu item
     */
    private void menuActionExport()
    {
        if (m_listModel.getSize() > 0)
        {
            final JFileChooser jfc = new JFileChooser(DEFAULT_PATH);
            jfc.setDialogTitle("Export combined log to file");
            jfc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
            jfc.setMultiSelectionEnabled(false);
            final int retVal = jfc.showSaveDialog(m_frame);

            if (retVal == JFileChooser.APPROVE_OPTION)
            {
                final File outputFile = jfc.getSelectedFile();
                final StringBuilder sb = new StringBuilder();

                for (int i = 0; i < m_listModel.getSize(); ++i)
                {
                    sb.append(m_listModel.getElementAt(i).getText() + System.lineSeparator());
                }

                try
                {
                    Files.write(Paths.get(outputFile.toString()), sb.toString().getBytes());
                }
                catch (final Exception e)
                {
                }
            }
        }
    }

    /**
     * Handles the user selecting the "Find" menu item
     */
    private void menuActionFind()
    {
        if (m_searchPanel.isVisible())
        {
            m_searchPanel.hideSearchPanel();
        }
        else
        {
            m_searchPanel.showSearchPanel();
        }
    }

    /**
     * Handles the user selecting the "Find Next" menu item
     */
    public void menuActionFindNext()
    {
        final int listSize = m_listView.getModel().getSize();

        if ((listSize > 0) && (m_searchPanel.getSearchText().length() > 0))
        {
            int startIndex = m_listView.getSelectedIndex();
            if (startIndex == -1)
            {
                startIndex = 0;
            }

            for (int i = startIndex + 1; i < listSize; ++i)
            {
                if (m_searchPanel.isMatchCase()
                        && m_listView.getModel().getElementAt(i).toString().contains(m_searchPanel.getSearchText()))
                {
                    m_listView.setSelectedIndex(i);
                    m_listView.ensureIndexIsVisible(i);
                    return;
                }
                else if ((!m_searchPanel.isMatchCase()) && m_listView.getModel().getElementAt(i).toString()
                        .toLowerCase().contains(m_searchPanel.getSearchText().toLowerCase()))
                {
                    m_listView.setSelectedIndex(i);
                    m_listView.ensureIndexIsVisible(i);
                    return;
                }
            }

            if (m_searchPanel.isWrap())
            {
                for (int i = 0; i < (startIndex - 1); ++i)
                {
                    if (m_searchPanel.isMatchCase()
                            && m_listView.getModel().getElementAt(i).toString().contains(m_searchPanel.getSearchText()))
                    {
                        m_listView.setSelectedIndex(i);
                        m_listView.ensureIndexIsVisible(i);
                        return;
                    }
                    else if ((!m_searchPanel.isMatchCase()) && m_listView.getModel().getElementAt(i).toString()
                            .toLowerCase().contains(m_searchPanel.getSearchText().toLowerCase()))
                    {
                        m_listView.setSelectedIndex(i);
                        m_listView.ensureIndexIsVisible(i);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Handles the user selecting the "Find Previous" menu item
     */
    public void menuActionFindPrevious()
    {
        final int listSize = m_listView.getModel().getSize();

        if ((listSize > 0) && (m_searchPanel.getSearchText().length() > 0))
        {
            int startIndex = m_listView.getSelectedIndex();
            if (startIndex == -1)
            {
                startIndex = listSize;
            }

            for (int i = startIndex - 1; i > 0; --i)
            {
                if (m_searchPanel.isMatchCase()
                        && m_listView.getModel().getElementAt(i).toString().contains(m_searchPanel.getSearchText()))
                {
                    m_listView.setSelectedIndex(i);
                    m_listView.ensureIndexIsVisible(i);
                    return;
                }
                else if ((!m_searchPanel.isMatchCase()) && m_listView.getModel().getElementAt(i).toString()
                        .toLowerCase().contains(m_searchPanel.getSearchText().toLowerCase()))
                {
                    m_listView.setSelectedIndex(i);
                    m_listView.ensureIndexIsVisible(i);
                    return;
                }
            }

            if (m_searchPanel.isWrap())
            {
                for (int i = listSize - 1; i > startIndex; --i)
                {
                    if (m_searchPanel.isMatchCase()
                            && m_listView.getModel().getElementAt(i).toString().contains(m_searchPanel.getSearchText()))
                    {
                        m_listView.setSelectedIndex(i);
                        m_listView.ensureIndexIsVisible(i);
                        return;
                    }
                    else if ((!m_searchPanel.isMatchCase()) && m_listView.getModel().getElementAt(i).toString()
                            .toLowerCase().contains(m_searchPanel.getSearchText().toLowerCase()))
                    {
                        m_listView.setSelectedIndex(i);
                        m_listView.ensureIndexIsVisible(i);
                        return;
                    }
                }
            }
        }

    }

    /**
     * Handles the user selecting the "Increase Font Size" menu item
     */
    private void menuActionIncreaseFontSize()
    {
        m_logItemRenderer.increaseFontSize();
        reload();
    }

    /**
     * Handles the user selecting the "Open" menu item
     */
    private void menuActionOpenFiles()
    {
        m_viewMenuCheckBoxes = new ArrayList<JCheckBoxMenuItem>();

        final JFileChooser jfc = new JFileChooser(DEFAULT_PATH);
        jfc.setDialogTitle("Select log file(s) to open:");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(true);
        final int retVal = jfc.showOpenDialog(m_frame);

        if (retVal == JFileChooser.APPROVE_OPTION)
        {
            ColorPicker.reset();
            FileMap.purge();

            final File[] files = jfc.getSelectedFiles();

            for (final File f : files)
            {
                final String sFile = f.getAbsolutePath();
                if ((new File(sFile)).exists())
                {
                    if (getViewMenuItem(sFile) == null)
                    {
                        final JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(sFile, true);
                        checkBoxMenuItem.addItemListener(this);
                        checkBoxMenuItem.setOpaque(true);
                        checkBoxMenuItem.setBackground(ColorPicker.get(sFile));
                        m_viewMenuCheckBoxes.add(checkBoxMenuItem);
                    }
                }
            }
        }

        reload();
    }

    /**
     * Handles the user selecting the "Refresh Files" menu item
     */
    private void menuActionRefresh()
    {
        final int selectedIndex = m_listView.getSelectedIndex();
        m_listModel.refresh();
        
        if (selectedIndex < m_listModel.getSize())
        {
            m_listView.setSelectedIndex(selectedIndex);
        }
        else
        {
            m_listView.setSelectedIndex(m_listModel.getSize());
        }
    }
    
    /**
     * Handles the user selecting the "Select All" menu item
     */
    private void menuActionSelectAll()
    {
        m_listView.addSelectionInterval(0, m_listModel.getSize());
    }

    /**
     * Handles the user selecting the "Uncheck All" menu item
     */
    private void menuActionUncheckAll()
    {
        if (m_viewMenuCheckBoxes != null)
        {
            for (final JCheckBoxMenuItem i : m_viewMenuCheckBoxes)
            {
                i.removeItemListener(this);
                i.setSelected(false);
                i.addItemListener(this);
            }

            reload();
        }
    }

    /**
     * Reloads the data model in accordance with the loaded and selected log files,
     * sorts the line items. Updates the display and the View menu accordingly.
     */
    private void reload()
    {
        final List<String> files = new ArrayList<String>();

        m_viewMenu.removeAll();

        final JMenuItem checkAll = new JMenuItem("Check All");
        checkAll.addActionListener(this);
        checkAll.setMnemonic(KeyEvent.VK_C);
        m_viewMenu.add(checkAll);

        final JMenuItem uncheckAll = new JMenuItem("Uncheck All");
        uncheckAll.addActionListener(this);
        uncheckAll.setMnemonic(KeyEvent.VK_U);
        m_viewMenu.add(uncheckAll);

        if (m_viewMenuCheckBoxes != null)
        {
            if (m_viewMenuCheckBoxes.size() > 0)
            {
                m_viewMenu.addSeparator();
            }
            else
            {
                checkAll.setEnabled(false);
                uncheckAll.setEnabled(false);
            }

            for (final JCheckBoxMenuItem i : m_viewMenuCheckBoxes)
            {
                if (i.isSelected() && !files.contains(i.getText()))
                {
                    files.add(i.getText());
                }

                m_viewMenu.add(i);
            }
        }

        m_listModel.reload(files);
    }

    /**
     * Complete the initialization of the LogSyncViewer and displays the main window
     * frame
     */
    public void run()
    {
        m_listView = new JList<LogSyncListItem>();
        m_listView.setModel(m_listModel);
        m_listView.setCellRenderer(m_logItemRenderer);

        final Action findNextAction = new AbstractAction("FindNext")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                menuActionFindNext();
            }
        };

        m_listView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0),
                "FindNext");
        m_listView.getActionMap().put("FindNext", findNextAction);

        final Action findPrevAction = new AbstractAction("FindPrev")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                menuActionFindPrevious();
            }
        };

        m_listView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK), "FindPrev");
        m_listView.getActionMap().put("FindPrev", findPrevAction);

        m_frame = new JFrame("LogSync Viewer");
        m_frame.setMinimumSize(new Dimension(800, 200));
        m_frame.setSize(800, 600);

        createMenu();

        m_frame.getContentPane().setLayout(new BorderLayout());
        m_frame.getContentPane().add(m_searchPanel, BorderLayout.NORTH);
        m_frame.getContentPane().add(new JScrollPane(m_listView), BorderLayout.CENTER);

        m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        reload();
        m_frame.setVisible(true);
    }
}
