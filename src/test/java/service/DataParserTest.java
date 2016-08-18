package service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import model.Person;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class DataParserTest {

  private static final String SPREADSHEET_ID = "test";
  private static final String URL = "https://docs.google.com/spreadsheets/d/" + SPREADSHEET_ID;
  private static final String EDIT_URL = URL + "/edit#gid1234";
  private static final String DATA_RANGE = "A1:C3";
  private static final int IMAGE_COLUMN = 2;
  private static final String IMAGE_LINK = "picture";

  @Mock
  private Sheets retriever;
  @Mock
  private Sheets.Spreadsheets spreadsheets;
  @Mock
  private Sheets.Spreadsheets.Values values;
  @Mock
  private Sheets.Spreadsheets.Values.Get get;
  @Mock
  private HttpTransport httpTransport;
  @Mock
  private JsonFactory jsonFactory;
  @Mock
  private FileDataStoreFactory dataStoreFactory;

  private List<List<Object>> data;
  private List<Object> innerData1;
  private List<Object> innerData2;
  private List<Person> people;

  private DataParser testModel;

  @BeforeMethod
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);

    data = new ArrayList<>();

    List<Object> headers = new ArrayList<>();
    headers.add(Person.GIVEN_FIRST_NAME_KEY);
    headers.add(Person.LAST_NAME_KEY);
    headers.add("Link to Picture");

    innerData1 = new ArrayList<>();
    innerData1.add("First");
    innerData1.add("Last");
    innerData1.add(IMAGE_LINK);

    innerData2 = new ArrayList<>();
    innerData2.add("Second");
    innerData2.add("Second to Last");
    innerData2.add(IMAGE_LINK);

    data.add(headers);
    data.add(innerData1);
    data.add(innerData2);

    ValueRange valueRange = new ValueRange();
    valueRange.setValues(data);
    when(retriever.spreadsheets()).thenReturn(spreadsheets);
    when(spreadsheets.values()).thenReturn(values);
    when(values.get(SPREADSHEET_ID, DATA_RANGE)).thenReturn(get);
    when(get.execute()).thenReturn(valueRange);


    people = new ArrayList<>();

    Map<String, String> data1 = new LinkedHashMap<>();
    data1.put(Person.GIVEN_FIRST_NAME_KEY, "First");
    data1.put(Person.LAST_NAME_KEY, "Last");

    Map<String, String> data2 = new LinkedHashMap<>();
    data2.put(Person.GIVEN_FIRST_NAME_KEY, "Second");
    data2.put(Person.LAST_NAME_KEY, "Second to Last");

    people.add(new Person(data1, IMAGE_LINK));
    people.add(new Person(data2, IMAGE_LINK));

    testModel = new DataParser(httpTransport, jsonFactory, dataStoreFactory) {
      @Override
      Sheets getSheets(HttpTransport httpTransport, JsonFactory jsonFactory, FileDataStoreFactory dataStoreFactory) {
        return retriever;
      }
    };
  }

  @Test
  public void shouldRetrieveDataProperly() throws IOException {
    testModel.retrieveData(URL, DATA_RANGE);

    verify(values).get(SPREADSHEET_ID, DATA_RANGE);
  }

  @Test
  public void shouldReturnCorrectData() throws IOException {
    List<List<Object>> result = testModel.retrieveData(URL, DATA_RANGE);

    assertEquals(result, data);
  }

  @Test
  public void shouldParseDataCorrectly() {
    List<Person> result = testModel.parseData(data, IMAGE_COLUMN);

    assertEquals(result, people);
  }

  @Test
  public void shouldHandleEditUrlCorrectly() throws IOException {
    testModel.retrieveData(EDIT_URL, DATA_RANGE);

    verify(values).get(SPREADSHEET_ID, DATA_RANGE);
  }

  @Test
  public void shouldOrderPersonObjectsAlphabeticallyByLastName() {
    data.remove(innerData1);
    data.add(innerData1);

    List<Person> result = testModel.parseData(data, IMAGE_COLUMN);

    assertEquals(result, people);
  }
}
