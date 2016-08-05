package service;

import model.Person;

import java.io.FileWriter;
import java.util.List;

/**
 * Uses the headers generated and a given list of Person objects to create one-page views of each person.
 * These views are generated in the form of html files.
 */
class FileGenerator {

  private FileWriter fileWriter;
  private List<String> headers;

  /**
   * Generates an html file for each person in the list given.
   * @param people a non-null list of Person objects
   */
  void generateFiles(List<Person> people) {

  }

}
