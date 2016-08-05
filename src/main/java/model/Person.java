package model;

import java.util.List;

/**
 * Model class that holds the survey data responses for one person.
 */
public class Person {

  private List<String> data;

  /**
   * Retrieves data responses of the person.
   * @return data responses
   */
  public List<String> getData() {
    return data;
  }

  /**
   * Sets data responses of person.
   * @param data a non-null list of strings
   */
  public void setData(List<String> data) {
    this.data = data;
  }

  /**
   * Generate the file name to be associated with this person (typically first and last name separated by a space).
   * @return name of file associated with person
   */
  public String getFileName() {
    return null;
  }
}
