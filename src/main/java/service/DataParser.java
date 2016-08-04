package service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import model.Student;

import java.io.File;
import java.util.List;

/**
 * Extracts data from Google Spreadsheets and parses it into headers and individual Student objects.
 */
class DataParser {

  private HttpTransport httpTransport;
  private JsonFactory jsonFactory;
  private FileDataStoreFactory dataStoreFactory;
  private File dataStoreDirectory;
  private Sheets retriever;

  /**
   * Retrieves the data in the given range from the Google Spreadsheet associated with the url.
   * @param url a non-null string which is the full url path of the Google Spreadsheet
   * @param range a non-null string which is range of cells to extract from the spreadsheet
   * @return values from the spreadsheet in the given range
   */
  ValueRange retrieveData(String url, String range) {
    return null;
  }

  /**
   * Parses the list of objects given into a list of headers.
   * @param values a non-null list of objects
   * @return list of string containing header values
   */
  List<String> parseHeaders(List<Object> values) {
    return null;
  }

  /**
   * Parses the list of lists of objects into a list of Student objects.
   * @param data a non-null list of lists of objects containg the spreadsheet data
   * @param imageColumn a non-null string which is column in the spreadsheet that contains the url of the student image
   * @return list of Student objects
   */
  List<Student> parseData(List<List<Object>> data, String imageColumn) {
    return null;
  }

}
