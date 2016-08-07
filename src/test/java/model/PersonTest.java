package model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;


public class PersonTest {

  private static final String FIRST_NAME = "First";
  private static final String LAST_NAME = "Last";
  private static final String PREFERRED_SAME = FIRST_NAME;
  private static final String PREFERRED_DIFFERENT = "Preferred";
  private static final String PREFERRED_LOWER_CASE = "first";

  private Person testModel;

  private Map<String, String> data;

  @BeforeMethod
  public void setUp() {
    data = new HashMap<>();
    data.put(Person.GIVEN_FIRST_NAME_KEY, FIRST_NAME);
    data.put(Person.LAST_NAME_KEY, LAST_NAME);
    data.put(Person.PREFERRED_NAME_KEY, PREFERRED_SAME);

    testModel = new Person(data);
  }

  @Test
  public void shouldReturnCorrectFileName() {
    String expected = "First Last.html";

    String result = testModel.getFileName();

    assertEquals(expected, result);
  }

  @Test
  public void shouldReturnPreferredNameInFileIfDifferent() {
    data.put(Person.PREFERRED_NAME_KEY, PREFERRED_DIFFERENT);
    String expected = "Preferred Last.html";

    String result = testModel.getFileName();

    assertEquals(expected, result);
  }

  @Test
  public void shouldReturnFirstNameIfPreferredIsStringLiteralSame() {
    data.put(Person.PREFERRED_NAME_KEY, Person.SAME);
    String expected = "First Last.html";

    String result = testModel.getFileName();

    assertEquals(expected, result);
  }

  @Test
  public void shouldBeCaseInsensitive() {
    data.put(Person.PREFERRED_NAME_KEY, PREFERRED_LOWER_CASE);
    String expected = "First Last.html";

    String result = testModel.getFileName();

    assertEquals(expected, result);
  }
  
  @Test
  public void shouldFindFirstNameIfKeyIsDifferent() {
    data.remove(Person.GIVEN_FIRST_NAME_KEY);
    data.put(Person.FIRST_NAME_KEY, FIRST_NAME);
    String expected = "First Last.html";

    String result = testModel.getFileName();

    assertEquals(expected, result);
  }
}
