package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.SurveyParser;

import javax.swing.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Client class for application. User interacts with this class to input data to download and parse spreadsheet data.
 */
public class UserInputDialog {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserInputDialog.class);

  private static JPanel dialog;

  public UserInputDialog() throws IOException, GeneralSecurityException {

  }

  /**
   * Main method for application. Launches the user interface and prompts the user for input.
   *
   * @param args command line arguments
   */
  public static void main(String args[]) throws IOException, GeneralSecurityException {
    if (args.length > 0) {
      String url = args[0];
      String range = args[1];
      String imageColumn = args[2];
      new SurveyParser().run(url, range, imageColumn);
    }
    else {
      //start up UI
    }
  }
}
