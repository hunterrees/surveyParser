package service;

/**
 * Facade of the back-end service of the application. Uses the DataParser and FileGenerator classes to take
 * the survey data and create the appropriate file for each student.
 */
public class SurveyParser {

  private DataParser dataParser;
  private FileGenerator fileGenerator;

  /**
   * Runs the Survey Parser program.
   * With the given inputs it will generate the files for each student with their given responses.
   * @param url a non-null string which is the full url path of the Google Spreadsheet
   * @param range a non-null string which is range of cells to extract from the spreadsheet
   * @param imageColumn a non-null string which is column in the spreadsheet that contains the url of the student image
   */
  public void run(String url, String range, String imageColumn) {

  }
}
