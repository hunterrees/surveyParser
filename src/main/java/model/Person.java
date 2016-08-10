package model;

import java.util.Map;

/**
 * Model class that holds the survey data responses for one person.
 */
public class Person {

  public static final String GIVEN_FIRST_NAME_KEY = "Given First Name";
  public static final String LAST_NAME_KEY = "Last Name";
  static final String FIRST_NAME_KEY = "First Name";
  static final String PREFERRED_NAME_KEY = "Preferred First Name";
  static final String SAME = "Same";

  private Map<String, String> data;
  private String imageLink;

  public Person(Map<String, String> data, String imageLink) {
    this.data = data;
    this.imageLink = imageLink;
  }

  /**
   * Retrieves data responses of the person.
   * @return data responses
   */
  public Map<String, String> getData() {
    return data;
  }

  /**
   * Retrieves image link of the person.
   * @return image link
   */
  public String getImageLink() {
    return imageLink;
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
      result.append(getPreferredName(preferredName));
    }
    result.append(" ");
    result.append(lastName);
    result.append(".html");

    return result.toString();
  }

  private boolean areSame(String firstName, String preferredName) {
    return firstName.equalsIgnoreCase(preferredName) || preferredName.equalsIgnoreCase(SAME);
  }

  private String getPreferredName(String preferredName) {
    int indexOfOr = preferredName.indexOf("or");
    if (indexOfOr != -1) {
      preferredName = preferredName.substring(0, indexOfOr).trim();
    }
    return preferredName;
  }

  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if (o instanceof Person) {
      Person that = (Person) o;
      result = this.getData().equals(that.getData()) && this.getImageLink().equals(that.getImageLink());
    }
    return result;
  }
}
