package acceptance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.SurveyParser;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

//This class is for acceptance testing, so all tests are disabled to ensure quick unit test runs.
public class AcceptanceTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(AcceptanceTests.class);

  private static final String URL = "https://docs.google.com/spreadsheets/d/1jahs48xDVd83UR1JNXroe-xkN5CAP0pdiaUqGH0b8rA";
  private static final String RANGE = "B1:I3";
  private static final String IMAGE_COLUMN = "D";

  private static final String FILE_PATH_TEST_ONE_EXPECTED = "src/acceptance/test/resources/Test One.html";
  private static final String FILE_PATH_TEST_TWO_EXPECTED = "src/acceptance/test/resources/Test Two.html";
  private static final String FILE_PATH_FORMATTING_EXPECTED = "src/acceptance/test/resources/style.css";

  private static final String STUDENT_DIRECTORY = "studentPages";
  private static final String FILE_NAME_FORMAT = "%s/%s";
  private static final String FILE_PATH_TEST_ONE_ACTUAL = String.format(FILE_NAME_FORMAT,
          STUDENT_DIRECTORY, "Test One.html");
  private static final String FILE_PATH_TEST_TWO_ACTUAL = String.format(FILE_NAME_FORMAT,
          STUDENT_DIRECTORY, "Test Two.html");
  private static final String FILE_PATH_FORMATTING = String.format(FILE_NAME_FORMAT,
          STUDENT_DIRECTORY, "style.css");

  private String testOneExpected;
  private String testTwoExpected;
  private String testFormattingExpected;

  private SurveyParser surveyParser;

  @BeforeClass
  public void setUp() throws IOException, GeneralSecurityException {
    cleanUp();
    surveyParser = new SurveyParser();

    LOGGER.info("Reading contents of expected files");
    testOneExpected = readData(new File(FILE_PATH_TEST_ONE_EXPECTED));
    testTwoExpected = readData(new File(FILE_PATH_TEST_TWO_EXPECTED));
    testFormattingExpected = readData(new File(FILE_PATH_FORMATTING_EXPECTED));
  }

  private String readData(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
    StringBuilder result = new StringBuilder();

    while (scanner.hasNext()) {
      result.append(scanner.next());
    }

    return result.toString();
  }

  @Test
  public void acceptanceTest() throws IOException {
    LOGGER.info("Starting run of Survey Parser with url={} range={} image_column={}", URL, RANGE, IMAGE_COLUMN);
    surveyParser.run(URL, RANGE, IMAGE_COLUMN);

    File testOneActual = new File(FILE_PATH_TEST_ONE_ACTUAL);
    File testTwoActual = new File(FILE_PATH_TEST_TWO_ACTUAL);
    File testFormatting = new File(FILE_PATH_FORMATTING);

    assertTrue(testOneActual.exists());
    assertTrue(testTwoActual.exists());
    assertTrue(testFormatting.exists());

    LOGGER.info("Comparing contents of files {} and {}", FILE_PATH_TEST_ONE_EXPECTED, FILE_PATH_TEST_ONE_ACTUAL);
    String testOneContents = readData(testOneActual);
    assertEquals(testOneExpected, testOneContents);

    LOGGER.info("Comparing contents of files {} and {}", FILE_PATH_TEST_TWO_EXPECTED, FILE_PATH_TEST_TWO_ACTUAL);
    String testTwoContents = readData(testTwoActual);
    assertEquals(testTwoExpected, testTwoContents);

    LOGGER.info("Comparing contents of files {} and {}", FILE_PATH_FORMATTING_EXPECTED, FILE_PATH_FORMATTING);
    String testFormattingContents = readData(testFormatting);
    assertEquals(testFormattingExpected, testFormattingContents);
  }

  @AfterClass
  public void tearDown() {
    cleanUp();
  }

  private void cleanUp() {
    File testOneActual = new File(FILE_PATH_TEST_ONE_ACTUAL);
    File testTwoActual = new File(FILE_PATH_TEST_TWO_ACTUAL);
    File testFormatting = new File(FILE_PATH_FORMATTING);
    File directory = new File(STUDENT_DIRECTORY);

    if (testOneActual.exists()) {
      assertTrue(testOneActual.delete());
    }
    if (testTwoActual.exists()) {
      assertTrue(testTwoActual.delete());
    }
    if (testFormatting.exists()) {
      assertTrue(testFormatting.delete());
    }
    if (directory.exists()) {
      assertTrue(directory.delete());
    }
  }
}
