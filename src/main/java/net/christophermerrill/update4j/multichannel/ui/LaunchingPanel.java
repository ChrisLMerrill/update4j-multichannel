package net.christophermerrill.update4j.multichannel.ui;

import net.christophermerrill.update4j.multichannel.core.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class LaunchingPanel extends JPanel implements AppLauncher.LaunchListener
    {
    LaunchingPanel(VisibleUpdateController main_ui)
        {
        _main_ui = main_ui;
        AppLauncher.LISTENERS.add(this);

        setLayout(new GridBagLayout());
        _status = new JLabel("Starting");
        Font font = getFont();
        _status.setFont(font.deriveFont(font.getSize() * 2f));
        _status.setHorizontalAlignment(JLabel.CENTER);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(_status, constraints);

        _message = new JLabel("get ready...");
        _message.setHorizontalAlignment(JLabel.CENTER);
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(_message, constraints);

        _message2 = new JLabel("...almost there");
        _message2.setHorizontalAlignment(JLabel.CENTER);
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(0, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        add(_message2, constraints);

        _close_button = new JButton("Quit");
        _close_button.setHorizontalAlignment(JLabel.CENTER);
        constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.insets = new Insets(0, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        add(_close_button, constraints);
        _close_button.setActionCommand("close");
        _close_button.setVisible(false);
        _close_button.addActionListener(e ->
            {
            if ("close".equals(e.getActionCommand()))
                _main_ui.shutdown();
            });

        _open_button = new JButton("Open Log File");
        _open_button.setHorizontalAlignment(JLabel.CENTER);
        constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.insets = new Insets(0, 5, 5, 5);
        constraints.anchor = GridBagConstraints.EAST;
        add(_open_button, constraints);
        _open_button.setActionCommand("open");
        _open_button.setVisible(false);
        _open_button.addActionListener(e ->
            {
            if ("open".equals(e.getActionCommand()))
                try
                    {
                    Desktop.getDesktop().open(SimpleLogger.LOG_FILE);
                    }
                catch (IOException e1)
                    {
                    SimpleLogger.log("Unable to open log file in system application", e1);
                    _open_button.setEnabled(false);
                    }
            });
        }

    @Override
    public void starting()
        {
        _message.setText("");
        _message2.setText("");
        }

    @Override
    public void completed(Throwable exception)
        {
        if (exception == null)
            _status.setText("Running");
        else
            {
            _status.setText("Failed to launch");
            _message.setText("Include log file with your bug report:");
            _message2.setText(SimpleLogger.LOG_FILE.getAbsolutePath());
            _close_button.setVisible(true);
            _open_button.setVisible(true);
            }

        new Thread(() -> AppLauncher.LISTENERS.remove(this)).start();
        }

    private final VisibleUpdateController _main_ui;
    private final JLabel _status;
    private final JLabel _message;
    private final JLabel _message2;
    private final JButton _close_button;
    private final JButton _open_button;
    }
