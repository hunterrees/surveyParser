package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class that holds the survey data responses for one person.
 */
public class Person {

  static final String GIVEN_FIRST_NAME_KEY = "Given First Name";
  static final String FIRST_NAME_KEY = "First Name";
  static final String LAST_NAME_KEY = "Last Name";
  static final String PREFERRED_NAME_KEY = "Preferred First Name";
  static final String SAME = "Same";

  private Map<String, String> data;

  public Person() {
    data = new HashMap<>();
  }

  public Person(Map<String, String> data) {
    this.data = data;
  }

  /**
   * Retrieves data responses of the person.
   * @return data responses
   */
  public Map<String, String> getData() {
    return data;
  }

  /**
   * Sets data responses of person.
   * @param data a non-null list of strings
   */
  public void setData(Map<String, String> data) {
    this.data = data;
  }

  /**
   * Generate the file name to be associated with this person (typically first and last name separated by a space).
   * @return name of file associated with person
   */
  public String getFileName() {
    StringBuilder result = new StringBuilder();

    String firstName = data.get(GIVEN_FIRST_NAME_KEY);
    if (firstName == null) {
      firstName = data.get(FIRST_NAME_KEY);
    }
    String lastName = data.get(LAST_NAME_KEY);
    String preferredName = data.get(PREFERRED_NAME_KEY);

    if (areSame(firstName, preferredName)) {
      result.append(firstName);
    }
    else {
      result.append(preferredName);
    }
    result.append(" " + lastName + ".html");

    return result.toString();
  }

  private boolean areSame(String firstName, String preferredName) {
    return firstName.equalsIgnoreCase(preferredName) || preferredName.equalsIgnoreCase(SAME);
  }
}
