SessionBasedRetrieval
=====================

Prerequisites
-------------

1. Make sure you have jdk 7 or above installed and in the **PATH** system variable
2. Also install Gradle and make sure it's added to **PATH**
3. IntelliJ or Eclipse IDE

Setup
-----

1. Open a terminal to the project's directory
2. Generating IDE-specific files
    1. For **IntelliJ**, run ```gradle idea``` from the command line
    2. For **Eclipse**, run ```gradle eclipse``` from the command line
3. Importing the project in your IDE
    1. For **Eclipse**: open ```File->Import->General->Existing project into workspace``` 
    and browse to your project's directory.
    2. For **IntelliJ**, ```File->Open``` and select the ```.ipr``` file in the project's 
    directory. Note that the .ipr file was created by the ```gradle idea``` command.