package service;

import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Facade of the back-end service of the application. Uses the DataParser and FileGenerator classes to take
 * the survey data and create the appropriate file for each person.
 */
public class SurveyParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(SurveyParser.class);

  static final String EXPECTED_URL_PREFIX = "https://docs.google.com/spreadsheets/d/";
  private static final String INVALID_EDIT = "edit#";
  private static final int RANGE_COLUMN_START_INDEX = 0;
  private static final int RANGE_COLUMN_END_INDEX = 3;
  private static final int RANGE_ROW_START_INDEX = RANGE_COLUMN_START_INDEX + 1;
  private static final int RANGE_ROW_END_INDEX = RANGE_COLUMN_END_INDEX + 1;
  private static final int VERY_LONG_RANGE_LENGTH = 6;

  private DataParser dataParser;
  private FileGenerator fileGenerator;

  public SurveyParser() throws IOException, GeneralSecurityException {
    this(new DataParser(), new FileGenerator());
  }

  SurveyParser(DataParser dataParser, FileGenerator fileGenerator) {
    this.dataParser = dataParser;
    this.fileGenerator = fileGenerator;
  }

  /**
   * Runs the Survey Parser program.
   * With the given inputs it will generate the files for each person with their given responses.
   *
   * @param url         a non-null string which is the full url path of the Google Spreadsheet
   * @param range       a non-null string which is range of cells to extract from the spreadsheet
   * @param imageColumn a non-null string which is column in the spreadsheet that contains the url of the person's image
   * @throws IOException if something in the dataParser of fileGenerator goes wrong
   */
  public void run(String url, String range, String imageColumn) throws IOException {

    LOGGER.info("Validating input");

    validateInput(url, range, imageColumn);

    LOGGER.info("Beginning to retrieve data from spreadsheet={} with range={}", url, range);
    List<List<Object>> data = dataParser.retrieveData(url, range);

    int imageIndex = imageColumn.toUpperCase().charAt(0) - range.charAt(RANGE_COLUMN_START_INDEX);
    LOGGER.info("Picture link is at index={}", imageIndex);

    LOGGER.info("Beginning to parse through retrieved data");
    List<Person> people = dataParser.parseData(data, imageIndex);

    LOGGER.info("Beginning to generate files");
    fileGenerator.generateFiles(people);
  }

  private void validateInput(String url, String range, String imageColumn) {
    if (!urlValid(url)) {
      throw new IllegalArgumentException(String.format("URL must contain %s. URL given was= %s",
              EXPECTED_URL_PREFIX, url));
    }
    if (urlNotEdit(url)) {
      throw new IllegalArgumentException(String.format("URL invalid. Please remove everything from \"%s\" " +
              "to the end of the URL and try again", INVALID_EDIT));
    }
    if (!validateRange(range)) {
      throw new IllegalArgumentException(String.format("Range is invalid. Range given was=%s", range));
    }
    if (!validateImageColumn(range, imageColumn)) {
      throw new IllegalArgumentException(String.format("Image Column must be within given range. Range given was=%s. "
              + "Image Column given was=%s", range, imageColumn));
    }
  }

  private boolean urlValid(String url) {
    return url.contains(EXPECTED_URL_PREFIX);
  }

  private boolean urlNotEdit(String url) {
    return url.contains(INVALID_EDIT);
  }

  private boolean validateRange(String range) {
    char beginningColumn = range.charAt(RANGE_COLUMN_START_INDEX);
    char endingColumn = range.charAt(RANGE_COLUMN_END_INDEX);
    int beginningRow = range.charAt(RANGE_ROW_START_INDEX);
    int endingRow = range.charAt(RANGE_ROW_END_INDEX);

    return (beginningColumn < endingColumn && beginningRow < endingRow) || range.length() >= VERY_LONG_RANGE_LENGTH ;
  }

  private boolean validateImageColumn(String range, String imageColumn) {
    char beginningColumn = range.charAt(RANGE_COLUMN_START_INDEX);
    char endingColumn = range.charAt(RANGE_COLUMN_END_INDEX);

    return (imageColumn.charAt(0) > beginningColumn && imageColumn.charAt(0) < endingColumn)
            || range.length() >= VERY_LONG_RANGE_LENGTH;
  }
}
