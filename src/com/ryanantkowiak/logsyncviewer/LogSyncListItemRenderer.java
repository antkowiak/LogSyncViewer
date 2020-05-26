package com.ryanantkowiak.logsyncviewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Renderer for the log viewer list items. Determines how each list item shoudl
 * be rendered.
 *
 * @author Ryan Antkowiak (antkowiak@gmail.com)
 *
 */
public class LogSyncListItemRenderer extends JLabel implements ListCellRenderer<LogSyncListItem>
{
    /**
     * Version of serialized data (required from parent classes)
     */
    private static final long serialVersionUID = 1L;

    /**
     * The font size for the display of log entries
     */
    private int m_fontSize = 12;

    /**
     * The font object used for rendering log entries
     */
    private Font m_font = new Font("Consolas", Font.BOLD, 12);

    /**
     * Constructor
     */
    public LogSyncListItemRenderer()
    {
        setOpaque(true);
    }

    /**
     * Decreases the font size of the log entries
     */
    public void decreaseFontSize()
    {
        if (m_fontSize > 8)
        {
            m_fontSize -= 2;
            m_font = new Font("Consolas", Font.BOLD, m_fontSize);
        }
    }

    /**
     * Return a component that has been configured to display the specified value.
     * The value object is the LogSyncListItem from the data model.
     *
     * @param list         - The JList we're painting.
     * @param value        - The value returned by
     *                     list.getModel().getElementAt(index).
     * @param index        - The cells index.
     * @param isSelected   - True if the specified cell was selected.
     * @param cellHasFocus - True if the specified cell has the focus.
     *
     */
    @Override
    public Component getListCellRendererComponent(final JList<? extends LogSyncListItem> list,
            final LogSyncListItem value, final int index, final boolean isSelected, final boolean cellHasFocus)
    {
        if (isSelected)
        {
            setForeground(Color.WHITE);
            setBackground(Color.BLUE);
        }
        else
        {
            setForeground(Color.BLACK);
            setBackground(value.getColor());
        }

        setFont(m_font);
        setText(value.getText());
        setToolTipText(value.getToolTip());
        return this;
    }

    /**
     * Increases the font size of the log entries
     */
    public void increaseFontSize()
    {
        m_fontSize += 2;
        m_font = new Font("Consolas", Font.BOLD, m_fontSize);
    }
}
