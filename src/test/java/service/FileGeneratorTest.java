package service;

import model.Person;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class FileGeneratorTest {

  private static final String IMAGE_LINK = "image.org";
  private static final String STYLE_SHEET = "<head><title>Test One Profile</title><link rel=\"stylesheet\"" +
          "type=\"text/css\" href=\"style.css\"></head>";
  private static final String IMAGE_EMBED = "<body><table><tr><td><img src=\"image.org\"></td><td>" +
          "<h1>Test One</h1></td></tr></table>";
  private static final String ADD_ENTRY = "<b>Test: </b>Test<br>";
  private static final String BLANK_ENTRY = "<b>Test: </b><br>";
  private static final String NA_ENTRY = "<b>Test: </b>N/A<br>";

  @Mock
  FileWriter fileWriterOne;
  @Mock
  FileWriter fileWriterTwo;
  @Mock
  FileWriter fileWriterStyle;
  @Mock
  File directory;

  private List<String> trace;
  private List<Person> people;
  private FileGenerator testModel;

  @BeforeMethod
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);
    trace = new ArrayList<>();

    Map<String, String> data1 = new HashMap<>();
    data1.put(Person.GIVEN_FIRST_NAME_KEY, "Test");
    data1.put(Person.LAST_NAME_KEY, "One");

    Map<String, String> data2 = new HashMap<>();
    data2.put(Person.GIVEN_FIRST_NAME_KEY, "Test");
    data2.put(Person.LAST_NAME_KEY, "Two");

    people = new ArrayList<>();
    people.add(new Person(data1, IMAGE_LINK));
    people.add(new Person(data2, IMAGE_LINK));

    when(directory.mkdir()).thenReturn(true);
    when(directory.exists()).thenReturn(true);

    testModel = new FileGenerator(directory) {
      @Override
      FileWriter getFileWriter(String fileName) throws IOException {
        switch (fileName) {
          case "studentPages/Test One.html":
            trace.add(fileName);
            return fileWriterOne;
          case "studentPages/Test Two.html":
            trace.add(fileName);
            return fileWriterTwo;
          case "studentPages/style.css":
            trace.add(fileName);
            return fileWriterStyle;
          default:
            return null;
        }
      }
    };
  }

  @Test (expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*style.css added to header.*")
  public void shouldIncludeStyleCssFileInHeader() throws IOException {
    doThrow(new RuntimeException("style.css added to header")).when(fileWriterOne).write(STYLE_SHEET);

    testModel.generateFiles(people);
  }

  @Test (expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*style.css written.*")
  public void shouldCreateStyleCssFile() throws IOException {
    doThrow(new RuntimeException("style.css written")).when(fileWriterStyle).write(anyString());

    testModel.generateFiles(people);
  }

  @Test (expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*embed image.*")
  public void shouldEmbedImage() throws IOException {
    doThrow(new RuntimeException("embed image")).when(fileWriterOne).write(IMAGE_EMBED);

    testModel.generateFiles(people);
  }

  @Test
  public void shouldGenerateFileForEachStudent() throws IOException {
    testModel.generateFiles(people);

    assertEquals(trace.size(), 3);
  }

  @Test
  public void shouldWriteFileToCorrectLocation() throws IOException {
    testModel.generateFiles(people);

    assertEquals(trace.get(1), "studentPages/Test One.html");
  }

  @Test (expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*add colon entry.*")
  public void shouldNotAddColonIfAlreadyThere() throws IOException {
    doThrow(new RuntimeException("add colon entry")).when(fileWriterOne).write(ADD_ENTRY);
    List<Person> people = new ArrayList<>();
    Map<String, String> data = new HashMap<>();
    data.put(Person.GIVEN_FIRST_NAME_KEY, "Test");
    data.put(Person.LAST_NAME_KEY, "One");
    data.put("Test:", "Test");
    people.add(new Person(data, IMAGE_LINK));

    testModel.generateFiles(people);
  }

  @Test
  public void shouldNotAddEntryIfBlank() throws IOException {
    doThrow(new RuntimeException("add blank entry")).when(fileWriterOne).write(BLANK_ENTRY);
    List<Person> people = new ArrayList<>();
    Map<String, String> data = new HashMap<>();
    data.put(Person.GIVEN_FIRST_NAME_KEY, "Test");
    data.put(Person.LAST_NAME_KEY, "One");
    data.put("Test", "");
    people.add(new Person(data, IMAGE_LINK));

    testModel.generateFiles(people);
  }

  @Test
  public void shouldNotAddEntryIfNA() throws IOException {
    doThrow(new RuntimeException("add N/A entry")).when(fileWriterOne).write(NA_ENTRY);
    List<Person> people = new ArrayList<>();
    Map<String, String> data = new HashMap<>();
    data.put(Person.GIVEN_FIRST_NAME_KEY, "Test");
    data.put(Person.LAST_NAME_KEY, "One");
    data.put("Test", "N/A");
    people.add(new Person(data, IMAGE_LINK));

    testModel.generateFiles(people);
  }

  @Test (expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*add entry.*")
  public void shouldContinueAfterBlankEntry() throws IOException {
    doThrow(new RuntimeException("add entry")).when(fileWriterOne).write(ADD_ENTRY);
    List<Person> people = new ArrayList<>();
    Map<String, String> data = new HashMap<>();
    data.put(Person.GIVEN_FIRST_NAME_KEY, "Test");
    data.put(Person.LAST_NAME_KEY, "One");
    data.put("Test", "N/A");
    data.put("Test", "Test");
    people.add(new Person(data, IMAGE_LINK));

    testModel.generateFiles(people);
  }
}
