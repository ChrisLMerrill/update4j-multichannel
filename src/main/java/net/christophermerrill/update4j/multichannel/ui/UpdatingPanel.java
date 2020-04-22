package net.christophermerrill.update4j.multichannel.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class UpdatingPanel extends JPanel
    {
    UpdatingPanel()
        {
        setLayout(new GridBagLayout());

        JLabel heading = new JLabel("Updating");
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

        _status = new JLabel();
        _status.setHorizontalAlignment(JLabel.CENTER);
        constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(_status, constraints);

        _progress = new JProgressBar(0, 100);
        constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(_progress, constraints);
        }

    void setMessage(String message)
        {
        _status.setText(message);
        }

    void setProgress(float percent)
        {
        _progress.setValue((int)(percent * 100f));
        }

    void startNewDownload()
        {
        _count++;
        if (_count > 1)
            _status.setText(String.format("%d of %d", _count, _total));
        }

    void setTotalUpdates(int number)
        {
        _total = number;
        }

    private final JLabel _status;
    private final JProgressBar _progress;
    private int _total = 0;
    private int _count = 0;
    }