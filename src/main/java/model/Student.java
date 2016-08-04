package model;

import java.util.List;

/**
 * Model class that holds the survey data responses for one student.
 */
public class Student {

  private List<String> data;

  /**
   * Retrieves data responses of the student.
   * @return data responses
   */
  public List<String> getData() {
    return data;
  }

  /**
   * Sets data responses of student.
   * @param data a non-null list of strings
   */
  public void setData(List<String> data) {
    this.data = data;
  }

  /**
   * Generate the file name to be associated with this student (typically first and last name separated by a space).
   * @return name of file associated with student
   */
  public String getFileName() {
    return null;
  }
}
