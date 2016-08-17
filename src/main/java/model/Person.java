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
  static final String NAME_FORMAT = "%s %s";

  private Map<String, String> data;
  private String imageLink;

  /**
   * Constructor that takes in all given fields
   * @param data non-null Map which contains data responses of the person
   * @param imageLink non-null String which contains image link of the person
   */
  public Person(Map<String, String> data, String imageLink) {
    this.data = data;
    this.imageLink = imageLink;
  }

  /**
   * Generate the file name to be associated with this person (typically first and last name separated by a space).
   *
   * @return name of file associated with person
   */
  public String getFileName() {
    return getName() + ".html";
  }

  /**
   * Gets the name of person (first and last separated by a space).
   *
   * @return name of person
   */
  public String getName() {
    StringBuilder result = new StringBuilder();

    String firstName = data.get(GIVEN_FIRST_NAME_KEY);
    if (firstName == null) {
      firstName = data.get(FIRST_NAME_KEY);
    }
    String lastName = data.get(LAST_NAME_KEY);
    String preferredName = data.get(PREFERRED_NAME_KEY);

    result.append(String.format(NAME_FORMAT, getFirstName(firstName, preferredName), lastName));

    return result.toString();
  }

  private String getFirstName(String firstName, String preferredName) {
    if (preferredName == null || firstName.equalsIgnoreCase(preferredName) || preferredName.equalsIgnoreCase(SAME)) {
      return firstName;
    }
    else {
      return getPreferredName(preferredName);
    }
  }

  private String getPreferredName(String preferredName) {
    if (preferredName.contains("/")) {
      return preferredName.split("/")[0];
    }

    int indexOfOr = preferredName.indexOf("or");
    if (indexOfOr != -1) {
      preferredName = preferredName.substring(0, indexOfOr).trim();
    }
    return preferredName;
  }

  /**
   * Compares to Person objects based on data responses and image link.
   *
   * @param o Object to compare against
   * @return if two objects are equal
   */
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if (o instanceof Person) {
      Person that = (Person) o;
      result = this.getData().equals(that.getData()) && this.getImageLink().equals(that.getImageLink());
    }
    return result;
  }

  /**
   * Retrieves data responses of the person.
   *
   * @return data responses
   */
  public Map<String, String> getData() {
    return data;
  }

  /**
   * Retrieves image link of the person.
   *
   * @return image link
   */
  public String getImageLink() {
    return imageLink;
  }
}
