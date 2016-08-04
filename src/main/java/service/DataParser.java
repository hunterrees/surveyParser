package service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import model.Student;

import java.io.File;
import java.util.List;

class DataParser {

  private HttpTransport httpTransport;
  private JsonFactory jsonFactory;
  private FileDataStoreFactory dataStoreFactory;
  private File dataStoreDirectory;
  private Sheets retriever;

  ValueRange retrieveData(String url, String range) {
    return null;
  }

  List<String> parseHeaders(List<Object> values) {
    return null;
  }

  List<Student> parseData(List<List<Object>> data, String imageColumn) {
    return null;
  }

}
