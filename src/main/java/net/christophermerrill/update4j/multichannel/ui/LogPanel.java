package net.christophermerrill.update4j.multichannel.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class LogPanel extends JPanel
    {
    LogPanel()
        {
        JScrollPane scroller = new JScrollPane(_textarea);
        setLayout(new BorderLayout());
        add(scroller);
        }

    void addMessage(String message)
        {
        _textarea.append(message);

        }

    private final JTextArea _textarea = new JTextArea(10, 40);
    }


