package model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;


public class PersonTest {

  private static final String FIRST_NAME = "First";
  private static final String LAST_NAME = "Last";
  private static final String PREFERRED_SAME = FIRST_NAME;
  private static final String PREFERRED_DIFFERENT = "Preferred";
  private static final String PREFERRED_LOWER_CASE = "first";
  private static final String OR_PREFERRED = "Preferred or Pref";
  private static final String IMAGE_LINK = "image link";
  private static final String EXPECTED = String.format(Person.FILE_FORMAT, FIRST_NAME, LAST_NAME);
  private static final String PREFERRED_EXPECTED = String.format(Person.FILE_FORMAT, PREFERRED_DIFFERENT, LAST_NAME);

  private Map<String, String> data;

  private Person testModel;

  @BeforeMethod
  public void setUp() {
    data = new LinkedHashMap<>();
    data.put(Person.GIVEN_FIRST_NAME_KEY, FIRST_NAME);
    data.put(Person.LAST_NAME_KEY, LAST_NAME);
    data.put(Person.PREFERRED_NAME_KEY, PREFERRED_SAME);

    testModel = new Person(data, IMAGE_LINK);
  }

  @Test
  public void shouldReturnCorrectFileName() {
    String result = testModel.getFileName();

    assertEquals(result, EXPECTED);
  }

  @Test
  public void shouldReturnPreferredNameInFileIfDifferent() {
    data.put(Person.PREFERRED_NAME_KEY, PREFERRED_DIFFERENT);

    String result = testModel.getFileName();

    assertEquals(result, PREFERRED_EXPECTED);
  }

  @Test
  public void shouldReturnFirstNameIfPreferredIsStringLiteralSame() {
    data.put(Person.PREFERRED_NAME_KEY, Person.SAME);

    String result = testModel.getFileName();

    assertEquals(result, EXPECTED);
  }

  @Test
  public void shouldBeCaseInsensitive() {
    data.put(Person.PREFERRED_NAME_KEY, PREFERRED_LOWER_CASE);

    String result = testModel.getFileName();

    assertEquals(result, EXPECTED);
  }

  @Test
  public void shouldFindFirstNameIfKeyIsDifferent() {
    data.remove(Person.GIVEN_FIRST_NAME_KEY);
    data.put(Person.FIRST_NAME_KEY, FIRST_NAME);

    String result = testModel.getFileName();

    assertEquals(result, EXPECTED);
  }

  @Test
  public void shouldGenerateCorrectPreferredNameIfOrIsIncluded() {
    data.put(Person.PREFERRED_NAME_KEY, OR_PREFERRED);

    String result = testModel.getFileName();

    assertEquals(result, PREFERRED_EXPECTED);
  }
}
