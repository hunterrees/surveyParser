# Survey Parser

Application that parses Google Forms data from an associated Google Spreadsheet. 

#General Guidelines

**Java JDK 8**
In order to run this application, you must have Java JDK 8 downloaded on your computer. 
You can find that [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Choose the one that fits your operating system, download, and install before running this application.

**Setting up Google Spreadsheet**

1. Ensure that you are using a shareable link on your spreadsheet, otherwise the application will not have the ability to retrieve the spreadsheet data.
2. Be sure to include the row with the column entries (First Name, Last Name, etc). This is the only way the application can correctly generate the one-page views for each survey response.
3. Image column indicates the column in the spreadsheet where the image links are found, not where the images themselves appear.
4. If there are columns you wish to not include, you must rearrange the columns in your spreadsheet. You can only give one continuous range of cells as input to the application.

*A "Student Directory" page is created that has links to all other generated files.

#Command Line Interface

To run from the command line use the following command:

`java -jar Survey\ Parser\ <version>.jar <url> <cell range> <image column>`


#Graphical User Interface

1. Click on the `Survey Parser <version>.jar` file.
2. Enter the url, cell range, and image column.
3. Click the Submit button.
4. Close application upon completion.

#Troubleshooting and Problems

If you encounter problems in running the application please contact the developer at: hunter.rees13@gmail.com