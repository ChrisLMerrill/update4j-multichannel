package net.christophermerrill.update4j.multichannel.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class CheckingUpdatesPanel extends JPanel
    {
    CheckingUpdatesPanel()
        {
        setLayout(new GridBagLayout());

        JLabel heading = new JLabel("Checking for updates");
        Font font = getFont();
        heading.setFont(font.deriveFont(font.getSize() * 2f));
        heading.setHorizontalAlignment(JLabel.CENTER);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(heading, constraints);

        _status = new JLabel("<asset name>");
        _status.setHorizontalAlignment(JLabel.CENTER);
        constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(_status, constraints);
        }

    void setMessage(String message)
        {
        _status.setText(message);
        }

    private final JLabel _status;
    }


