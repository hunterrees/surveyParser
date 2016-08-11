package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.SurveyParser;

import javax.swing.*;

/**
 * Client class for application. User interacts with this class to input data to download and parse spreadsheet data.
 */
public class UserInputDialog {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserInputDialog.class);

  private SurveyParser surveyParser;
  private JPanel dialog;

  /**
   * Main method for application. Launches the user interface and prompts the user for input.
   *
   * @param args command line arguments
   */
  public static void main(String args[]) {

  }
}
