package com.ryanantkowiak.logsyncviewer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class defines the search panel at the top of the log viewer window
 *
 * @author Ryan Antkowiak 
 *
 */
public class SearchPanel extends JPanel implements ActionListener
{
    /**
     * Version of serialized data (required from parent classes)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Handle to the main log viewer instance
     */
    private final LogSyncViewer m_logSyncViewer;

    /**
     * Text field used for entering search query
     */
    private final JTextField m_textField;

    /**
     * Check box to indicate if the search is case sensitive
     */
    private final JCheckBox m_matchCase;

    /**
     * Check box to indicate if the search should wrap around
     */
    private final JCheckBox m_wrap;

    /**
     * Button to find next occurrence of search string
     */
    private final JButton m_findNext;

    /**
     * Button to find the previous occurrence of search string
     */
    private final JButton m_findPrev;

    /**
     * Button to hide the search panel
     */
    private final JButton m_hide;

    /**
     * Constructor. Initialize the Search Panel display
     *
     * @param lsv
     */
    public SearchPanel(final LogSyncViewer lsv)
    {
        super();

        m_logSyncViewer = lsv;

        m_textField = new JTextField(25);
        m_matchCase = new JCheckBox("Match-Case");
        m_wrap = new JCheckBox("Wrap");

        m_textField.setFont(new Font("Consolas", Font.BOLD, 12));

        m_findNext = new JButton("Find Next");
        m_findPrev = new JButton("Find Prev");
        m_hide = new JButton("Hide");

        m_findNext.setMnemonic(KeyEvent.VK_N);
        m_findPrev.setMnemonic(KeyEvent.VK_P);
        m_hide.setMnemonic(KeyEvent.VK_D);

        m_matchCase.setMnemonic(KeyEvent.VK_C);
        m_wrap.setMnemonic(KeyEvent.VK_W);

        m_textField.addActionListener(this);

        m_findNext.addActionListener(this);
        m_findPrev.addActionListener(this);
        m_hide.addActionListener(this);

        setSize(500, 20);
        add(new JLabel("Find:"));
        add(m_textField);
        add(m_matchCase);
        add(m_wrap);
        add(m_findNext);
        add(m_findPrev);
        add(m_hide);

        setOpaque(true);
        setVisible(false);
    }

    /**
     * Handles actions such as button presses, search text entry, and checkboxes.
     *
     * @param e The ActionEvent object that occurred
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() instanceof JButton)
        {
            final JButton btn = (JButton) e.getSource();

            if (btn == m_findNext)
            {
                m_logSyncViewer.menuActionFindNext();
            }
            else if (btn == m_findPrev)
            {
                m_logSyncViewer.menuActionFindPrevious();
            }
            else if (btn == m_hide)
            {
                m_logSyncViewer.handleFindHide();
            }
        }

        if (e.getSource() instanceof JTextField)
        {
            final JTextField jtf = (JTextField) e.getSource();

            if (jtf == m_textField)
            {
                m_logSyncViewer.menuActionFindNext();
            }
        }
    }

    /**
     * Returns the value of the search text
     *
     * @return the search text
     */
    public String getSearchText()
    {
        return m_textField.getText();
    }

    /**
     * Hides the Search Panel
     */
    public void hideSearchPanel()
    {
        m_textField.select(0, 0);
        setVisible(false);
    }

    /**
     * Returns true if the search is case sensitive
     *
     * @return true if the search must match case
     */
    public boolean isMatchCase()
    {
        return m_matchCase.isSelected();
    }

    /**
     * Returns true if the search should wrap around
     *
     * @return true if the search should wrap
     */
    public boolean isWrap()
    {
        return m_wrap.isSelected();
    }

    /**
     * Shows the search panel
     */
    public void showSearchPanel()
    {
        setVisible(true);
        m_textField.selectAll();
        m_textField.requestFocus();
    }

}
