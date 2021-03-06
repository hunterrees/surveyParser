package service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Extracts data from Google Spreadsheets and parses it into headers and individual Person objects.
 */
class DataParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataParser.class);

  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
  private static final String APPLICATION_NAME = "Survey Parser";
  private static final String SECRET_LOCATION = "/client_secret.json";
  private static final String CREDENTIALS_LOCATION = ".credentials/sheets.googleapis.com-java-quickstart.json";
  private static final String EDIT_URL = "/edit";

  private final Sheets retriever;
  private List<String> headers;

  /**
   * Default Constructor.
   *
   * @throws IOException if files aren't found properly
   * @throws GeneralSecurityException if there is a security error
   */
  DataParser() throws IOException, GeneralSecurityException {
    this(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
            new FileDataStoreFactory(new java.io.File(System.getProperty("user.home"), CREDENTIALS_LOCATION)));
  }

  /**
   * Constructor used only for unit testing.
   *
   * @param httpTransport    HttpTransport used by the Sheet object
   * @param jsonFactory      JsonFactory used by the Sheet object
   * @param dataStoreFactory FileDataStoreFactory used by the Sheet object
   * @throws IOException if files aren't found properly
   */
  DataParser(HttpTransport httpTransport, JsonFactory jsonFactory,
             FileDataStoreFactory dataStoreFactory) throws IOException {
    retriever = getSheets(httpTransport, jsonFactory, dataStoreFactory);
  }

  /**
   * Gets an authorized Google Spreadsheets API Client. Package protected to override and mock in unit tests.
   *
   * @param httpTransport    HttpTransport used by the Sheet object
   * @param jsonFactory      JsonFactory used by the Sheet object
   * @param dataStoreFactory FileDataStoreFactory used by the Sheet object
   * @return an authorized Sheets API client service
   * @throws IOException when unable to create sheets object
   */
  Sheets getSheets(HttpTransport httpTransport, JsonFactory jsonFactory,
                   FileDataStoreFactory dataStoreFactory) throws IOException {
    Credential credential = getCredentials(httpTransport, jsonFactory, dataStoreFactory);
    LOGGER.info("Creating sheets object for application={}", APPLICATION_NAME);
    return new Sheets.Builder(httpTransport, jsonFactory, credential).setApplicationName(APPLICATION_NAME).build();
  }

  private Credential getCredentials(HttpTransport httpTransport, JsonFactory jsonFactory,
                                    FileDataStoreFactory dataStoreFactory) throws IOException {
    LOGGER.info("Retrieving client_secret.json");
    InputStream inputStream = DataParser.class.getResourceAsStream(SECRET_LOCATION);

    LOGGER.info("Loading client_secret.json");
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(inputStream));

    LOGGER.info("Building credentials");
    GoogleAuthorizationCodeFlow codeFlow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
            clientSecrets, SCOPES).setDataStoreFactory(dataStoreFactory).setAccessType("offline").build();
    return new AuthorizationCodeInstalledApp(codeFlow, new LocalServerReceiver()).authorize("user");
  }

  /**
   * Retrieves the data in the given range from the Google Spreadsheet associated with the url.
   *
   * @param url   a non-null string which is the full url path of the Google Spreadsheet
   * @param range a non-null string which is range of cells to extract from the spreadsheet
   * @return values from the spreadsheet in the given range
   * @throws IOException if unable to retrieve data from spreadsheet
   */
  List<List<Object>> retrieveData(String url, String range) throws IOException {
    String spreadsheetId = getSpreadsheetId(url);
    LOGGER.info("Now attempting to retrieve data from spreadsheet with spreadsheetId={}", spreadsheetId);
    return retriever.spreadsheets().values().get(spreadsheetId, range).execute().getValues();
  }

  private String getSpreadsheetId(String url) {
    String result = url;
    int editIndex = result.indexOf(EDIT_URL);
    if (editIndex != -1) {
      result = result.substring(0, editIndex);
    }
    return result.replace(SurveyParser.EXPECTED_URL_PREFIX, "").replace("/", "");
  }

  /**
   * Parses the list of lists of objects into a list of Person objects.
   *
   * @param data       a non-null list of lists of objects containing the spreadsheet data
   * @param imageIndex an index which is column in the spreadsheet that contains the url of the person's image
   * @return list of Person objects
   */
  List<Person> parseData(List<List<Object>> data, int imageIndex) {
    List<Person> people = new ArrayList<>();

    getHeaders(data.get(0));

    LOGGER.info("Retrieving and parsing data for each person");
    for (int i = 1; i < data.size(); i++) {
      people.add(parseOnePerson(data.get(i), imageIndex));
    }
    Collections.sort(people, (Person p1, Person p2) -> p1.getLastName().compareTo(p2.getLastName()));
    return people;
  }

  private void getHeaders(List<Object> dataToParse) {
    LOGGER.info("Retrieving and parsing headers");
    headers = dataToParse.stream().map(Object::toString).collect(Collectors.toList());
  }

  private Person parseOnePerson(List<Object> dataToParse, int imageIndex) {
    Map<String, String> data = new LinkedHashMap<>();
    String imageLink = "";
    for (int i = 0; i < dataToParse.size(); i++) {
      if (i != imageIndex) {
        data.put(headers.get(i), normalizeData(dataToParse.get(i).toString()));
      }
      else {
        imageLink = dataToParse.get(i).toString();
      }
    }
    return new Person(data, imageLink);
  }

  private String normalizeData(String input) {
    String result = Normalizer.normalize(input, Normalizer.Form.NFD);
    return result.replaceAll("[^\\p{ASCII}]", "");
  }
}
