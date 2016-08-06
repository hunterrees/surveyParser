package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade of the back-end service of the application. Uses the DataParser and FileGenerator classes to take
 * the survey data and create the appropriate file for each person.
 */
public class SurveyParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(SurveyParser.class);

  private DataParser dataParser;
  private FileGenerator fileGenerator;

  /**
   * Runs the Survey Parser program.
   * With the given inputs it will generate the files for each person with their given responses.
   * @param url a non-null string which is the full url path of the Google Spreadsheet
   * @param range a non-null string which is range of cells to extract from the spreadsheet
   * @param imageColumn a non-null string which is column in the spreadsheet that contains the url of the person's image
   */
  public void run(String url, String range, String imageColumn) {

  }
}
