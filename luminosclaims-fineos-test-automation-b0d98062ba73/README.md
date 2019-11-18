# README #

### Summary
> This is automation framework, implemented using Java, Selenium/Webdriver, TestNG & Maven. 

### Prerequisite: ###
* Java
* Maven
* Selenium/WebDriver
* TestNg
* Browsers (Firefox, Chrome, IE)
* Respective Browser drivers
* Intellij or Eclipse

### How do I get set up? ###

* Download the zip or clone the Bitbucket repository
* Unzip the zip file (if you downloaded one).
* Open Eclipse
  * File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
  * Select the right project
* Open the terminal in the IDE / Open Command Prompt and Change directory (cd) to folder containing pom.xml
* Execute the following command:

```
  mvn clean test -Durl="<Fineos url>" -Dbrowser="<browser>"
```:

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines
