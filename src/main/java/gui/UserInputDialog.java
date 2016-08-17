package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.SurveyParser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Client class for application. User interacts with this class to input data to download and parse spreadsheet data.
 */
public class UserInputDialog extends JFrame {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserInputDialog.class);

  private static final Color BACKGROUND_COLOR = new Color(178, 178, 178);
  private static final Point WINDOW_LOCATION = new Point(300, 350);
  private static final String TITLE = "Survey Parser Application";
  private static UserInputDialog frame;

  private UserInputDialog() {
    this.setTitle(TITLE);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setBackground(BACKGROUND_COLOR);
    this.setResizable(false);
    this.setLocation(WINDOW_LOCATION);

    this.add(new UserInputComponent());
    this.pack();
    this.setVisible(true);
  }

  /**
   * Main method for application. Launches the user interface and prompts the user for input.
   *
   * @param args command line arguments
   * @throws IOException if files aren't found properly (only when run from CLI)
   * @throws GeneralSecurityException if there is a security error (only when run from CLI)
   */
  public static void main(String args[]) throws IOException, GeneralSecurityException {
    if (args.length > 0) {
      LOGGER.info("Command line arguments found: {}", (Object[]) args);
      String url = args[0];
      String range = args[1];
      String imageColumn = args[2];
      new SurveyParser().run(url, range, imageColumn);
    }
    else {
      LOGGER.info("No command line arguments found. Opening up User Input Dialog Window.");
      frame = new UserInputDialog();
    }
  }

  /**
   * Gets the JFrame. Used when creating JOptionPane windows for errors and information.
   *
   * @return Parent component frame (UserInputDialog)
   */
  static JFrame getFrame() {
    return frame;
  }
}
