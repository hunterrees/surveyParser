package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.SurveyParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Component of the UI that contains all of the input fields and buttons.
 */
class UserInputComponent extends JPanel {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserInputComponent.class);

  private static final Dimension HORIZONTAL_BUFFER = new Dimension(4, 0);
  private static final Dimension VERTICAL_BUFFER = new Dimension(0, 10);
  private static final int INPUT_COLUMN_WIDTH = 60;

  private JPanel urlPanel;
  private JPanel rangePanel;
  private JPanel imageColumnPanel;
  private JPanel buttonPanel;

  private JTextField urlInput;
  private JTextField rangeInput;
  private JTextField imageColumnInput;

  UserInputComponent() {
    urlPanel = new JPanel();
    rangePanel = new JPanel();
    imageColumnPanel = new JPanel();
    buttonPanel = new JPanel();
    urlInput = new JTextField();
    rangeInput = new JTextField();
    imageColumnInput = new JTextField();

    this.add(Box.createRigidArea(VERTICAL_BUFFER));

    createUrlPanel();
    createRangePanel();
    createImageColumnPanel();

    this.add(Box.createRigidArea(VERTICAL_BUFFER));

    createButtonPanel();

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  }

  private void createUrlPanel() {
    LOGGER.info("Creating url panel.");
    createInputPanel(urlPanel, urlInput, "URL:");
  }

  private void createRangePanel() {
    LOGGER.info("Creating range panel.");
    createInputPanel(rangePanel, rangeInput, "Cell Range:");
  }

  private void createImageColumnPanel() {
    LOGGER.info("Creating image column panel.");
    createInputPanel(imageColumnPanel, imageColumnInput, "Image Column:");
  }

  private void createInputPanel(JPanel panel, JTextField input, String label) {
    panel.add(Box.createRigidArea(new Dimension(HORIZONTAL_BUFFER)));
    input.setColumns(INPUT_COLUMN_WIDTH);
    panel.add(new JLabel(label));
    panel.add(Box.createRigidArea(new Dimension(HORIZONTAL_BUFFER)));
    panel.add(input);
    panel.add(Box.createRigidArea(new Dimension(HORIZONTAL_BUFFER)));
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    this.add(panel);
  }

  private void createButtonPanel() {
    LOGGER.info("Creating button panel.");
    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> {
      String url = urlInput.getText();
      String range = rangeInput.getText();
      String imageColumn = imageColumnInput.getText();

      try {
        new SurveyParser().run(url, range, imageColumn);
        JOptionPane.showMessageDialog(UserInputDialog.getFrame(),
                "Finished generating files",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
      }
      catch (IllegalArgumentException illegalArgumentException) {
        LOGGER.info("User input error: {}", illegalArgumentException.getMessage());
        JOptionPane.showMessageDialog(UserInputDialog.getFrame(),
                illegalArgumentException.getMessage(),
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
      }
      catch (Exception unexpectedException) {
        LOGGER.error("Unexpected Exception", unexpectedException);
        JOptionPane.showMessageDialog(UserInputDialog.getFrame(),
                "Internal error likely involving credentials. Contact developer for help",
                "Internal Error",
                JOptionPane.ERROR_MESSAGE);
      }
    });

    JButton exitButton = new JButton("Exit");
    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });

    buttonPanel.add(submitButton);
    buttonPanel.add(Box.createRigidArea(HORIZONTAL_BUFFER));
    buttonPanel.add(exitButton);
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    this.add(buttonPanel);
  }
}
