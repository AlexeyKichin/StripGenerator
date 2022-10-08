# StripGenerator

REQUIREMENTS:
  - Java 8+
  - maven 3+


Directory structure follows the classic maven format.
Source classes are located in the home package.

Demo class is home.Strip. It represents a collection of tickets and methods to generate it according to the requirements.
I have also added the main method to it to see a demo. The main method generates 10000 strips, prints out first 10 of them and prints the overall execution time.
On my laptop(Core i5-8250U) it takes about 900ms to generate 10000 strips.

Unit tests cover 4 scenarios:
- number of tickets is correct
- all generated tickets are valid across 10000 sample strips
- If we intentianally break the ticket it is detected, so we are sure the broken tickets would have been detected in the previous step
- numbers are distributed correctly within the strip


In order to run the test pls do:
mvn test

In order to run the demo pls do:
mvn compile exec:java -Dexec.mainClass=home.Strip