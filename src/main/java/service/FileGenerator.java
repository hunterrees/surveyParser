package service;

import model.Student;

import java.io.FileWriter;
import java.util.List;

/**
 * Uses the headers generated and a given list of Student objects to create one-page views of each student.
 * These views are generated in the form of html files.
 */
class FileGenerator {

  private FileWriter fileWriter;
  private List<String> headers;

  /**
   * Generates an html file for each student in the list given.
   * @param students a non-null list of Student objects
   */
  void generateFiles(List<Student> students) {

  }

}
