package service;

import model.Person;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SurveyParserTest {

  private static final String URL = "https://docs.google.com/spreadsheets/test";
  private static final String DATA_RANGE = "A1:D3";
  private static final String IMAGE_COLUMN = "C";
  private static final int IMAGE_INDEX = 2;
  private static final String INVALID_URL = "https://facebook.com";
  private static final String INVALID_COLUMN_RANGE = "D1:A2";
  private static final String INVALID_ROW_RANGE = "A2:B1";
  private static final String INVALID_IMAGE_COLUMN = "E";

  @Mock
  private DataParser dataParser;
  @Mock
  private FileGenerator fileGenerator;

  private List<Person> people;
  private List<List<Object>> data;

  private SurveyParser testModel;

  @BeforeMethod
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);
    people = new ArrayList<>();
    data = new ArrayList<>();

    when(dataParser.parseData(data, IMAGE_INDEX)).thenReturn(people);
    when(dataParser.retrieveData(URL, DATA_RANGE)).thenReturn(data);

    testModel = new SurveyParser(dataParser, fileGenerator);
  }

  @Test
  public void shouldRetrieveDataFromGoogle() throws IOException {
    testModel.run(URL, DATA_RANGE, IMAGE_COLUMN);

    verify(dataParser).retrieveData(URL, DATA_RANGE);
  }

  @Test
  public void shouldParseRetrievedData() throws IOException {
    testModel.run(URL, DATA_RANGE, IMAGE_COLUMN);

    verify(dataParser).parseData(data, IMAGE_INDEX);
  }

  @Test (expectedExceptions = RuntimeException.class)
  public void shouldGenerateFilesFromParsedData() throws IOException {
    doThrow(new RuntimeException()).when(fileGenerator).generateFiles(people);

    testModel.run(URL, DATA_RANGE, IMAGE_COLUMN);
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*URL must contain.*")
  public void shouldRejectInvalidUrl() throws IOException {
    testModel.run(INVALID_URL, DATA_RANGE, IMAGE_COLUMN);
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*Range is invalid.*")
  public void shouldRejectInvalidColumnRange() throws IOException {
    testModel.run(URL, INVALID_COLUMN_RANGE, IMAGE_COLUMN);
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*Range is invalid.*")
  public void shouldRejectInvalidRowRange() throws IOException {
    testModel.run(URL, INVALID_ROW_RANGE, IMAGE_COLUMN);
  }

  @Test (expectedExceptions = IllegalArgumentException.class,
          expectedExceptionsMessageRegExp = ".*Image Column must be within given range.*")
  public void shouldRejectInvalidImageColumn() throws IOException {
    testModel.run(URL, DATA_RANGE, INVALID_IMAGE_COLUMN);
  }
}
