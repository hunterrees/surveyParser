package service;

import model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Uses the headers generated and a given list of Person objects to create one-page views of each person.
 * These views are generated in the form of html files.
 */
class FileGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileGenerator.class);

  private static final String STUDENT_PAGES_FOLDER = "studentPages";
  private static final String CSS_FILE_NAME = "style.css";
  private static final String FILE_NAME_FORMAT = "%s/%s";
  private static final String CSS_LOCATION = String.format(FILE_NAME_FORMAT, STUDENT_PAGES_FOLDER, CSS_FILE_NAME);

  private static final String CSS_DATA = "body {\n" +
          "  background: lightyellow;\n" +
          "}\n" +
          "\n" +
          "img {\n" +
          "  max-height: 200px;\n" +
          "  max-width: 200px;\n" +
          "  padding:0px;\n" +
          "  border-width:0px;\n" +
          "  margin:0px;\n" +
          "}\n";

  private static final String TOP_HTML_ENTRIES = "<!DOCTYPE html><html>";
  private static final String HEAD_FORMAT = "<head><title>%s Profile</title>" +
          "<link rel=\"stylesheet\"type=\"text/css\" href=\"%s\"></head>";
  private static final String TABLE_FORMAT = "<body><table><tr><td><img src=\"%s\"></td>" +
          "<td><h1>%s</h1></td></tr></table>";
  private static final String ENTRY_FORMAT = "<b>%s: </b>%s<br>";
  private static final String COLON_ENTRY_FORMAT = "<b>%s </b>%s<br>";
  private static final String BOTTOM_HTML_ENTRIES = "</body></html>";

  private final File directory;

  FileGenerator() throws FileNotFoundException {
    this(new File(STUDENT_PAGES_FOLDER));
  }

  FileGenerator(File directory) {
    this.directory = directory;
  }

  /**
   * Generates an html file for each person in the list given.
   *
   * @param people a non-null list of Person objects
   * @throws IOException if one of the files is not created properly
   */
  void generateFiles(List<Person> people) throws IOException {
    LOGGER.info("Creating root file directoy {}", STUDENT_PAGES_FOLDER);
    if (!directory.mkdir() && !directory.exists()) {
      throw new IOException(String.format("Directory %s was not created successfully", STUDENT_PAGES_FOLDER));
    }
    writeStyleCssFile();
    for (Person person : people) {
      generateFile(person);
    }
  }

  private void writeStyleCssFile() throws IOException {
    FileWriter fileWriter = getFileWriter(CSS_LOCATION);
    LOGGER.info("Writing css file data to file {}", CSS_LOCATION);
    fileWriter.write(CSS_DATA);
    fileWriter.close();
  }

  private void generateFile(Person person) throws IOException {
    String name = person.getName();
    LOGGER.info("Creating and writing file for {}", name);
    FileWriter fileWriter = getFileWriter(String.format(FILE_NAME_FORMAT, STUDENT_PAGES_FOLDER, person.getFileName()));

    fileWriter.write(TOP_HTML_ENTRIES);
    fileWriter.write(String.format(HEAD_FORMAT, name, CSS_FILE_NAME));
    fileWriter.write(String.format(TABLE_FORMAT, person.getImageLink(), name));

    Map<String, String> data = person.getData();
    for (String header : data.keySet()) {
      String entry = data.get(header);
      if (entry.isEmpty() || entry.equalsIgnoreCase("N/A")) {
        continue;
      }

      if (header.endsWith(":")) {
        fileWriter.write(String.format(COLON_ENTRY_FORMAT, header, entry));
      }
      else {
        fileWriter.write(String.format(ENTRY_FORMAT, header, entry));
      }
    }

    fileWriter.write(BOTTOM_HTML_ENTRIES);
    fileWriter.close();
  }


  /**
   * Creates a FileWriter to use to write an individual file. Package protected so unit tests can override and mock.
   *
   * @param fileName name of file to be created
   * @return FileWriter to use to write to file
   * @throws IOException if file isn't successfully created
   */
  FileWriter getFileWriter(String fileName) throws IOException {
    return new FileWriter(fileName);
  }

}
