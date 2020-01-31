## Changelog

### [0.01.7] (2020-02-10)
the stream torrent-client now has entered the documentation phase and is executeable via the IntelliJ IDEA IDE, for more
exact instructions on how to run it please view the main readme. for a full list of features and fixed bugs see the
full list of features below below<br/>

the project team is currently working on the final documentation in a microsoft word document. this document will be made accessible to the general public once it is finished

### Documentation
execute the following maven goal inside your IntelliJ IDEA in order to generate the accommodating javadoc within the
folder target/site inside the project directory

    + mvn javadoc:javadoc

the documentation document is still being edited by the project team and is scheduled for release in april 2020
### Features

+ completed javadoc-documentation

### Bug fixes

+ fixed failing execution when choosing partial download

### Dependencies

+ the current dependencies can be seen under https://github.com/PichlerMartin/stream/network/dependencies
+ a full list of dependencies also can be viewed in pom.xml or stream.iml

### [0.01.6] (2020-01-27)
the stream torrent-client now has entered the documentation phase and is executeable via the IntelliJ IDEA IDE, for more
exact instructions on how to run it please view the main readme. for a full list of features and fixed bugs see the
full list of features below below<br/>

### Documentation
execute the following maven goal inside your IntelliJ IDEA in order to generate the accommodating javadoc within the
folder target/site inside the project directory

    + mvn javadoc:javadoc

the documentation document is still being edited by the project team and is scheduled for release in april 2020
### Features

+ darkmode added
+ language support for spanish and french added
+ resume downloads now supported
+ seeding now supported
+ partial downloads now fully supported
+ .torrent-files now supported
+ jar artifact now supported
    - build with
    

    + mvn package
+ terminal output now supported

### Bug fixes

+ partial download now works flawlessly
+ distorted display of french labels now adjusted

### Dependencies

+ the current dependencies can be seen under https://github.com/PichlerMartin/stream/network/dependencies
+ a full list of dependencies also can be viewed in pom.xml or stream.iml

### ui with enabled darkmode

![dark-mode](https://i.ibb.co/jVLZ8K5/dark-mode.png)

### [0.01.5] (2020-01-07)
the stream client is now in closed-beta phase! the client is now working and options like switching languages
with ResourceBundles, setting a default download directory and storing this data in java preferences have already
been implemented.<br/>

### Documentation

### Features

### Bug fixes

### Dependencies

the current dependencies can be seen under https://github.com/PichlerMartin/stream/network/dependencies

### updated ui
![ui-settings](https://i.ibb.co/PmmC3p7/stream-UI-Add-Torrent.png)

### ui settings
![ui-settings](https://i.ibb.co/ZcqXhyS/stream-UI-Settings.png)

### [0.01.4] (2019-11-29)
 in order to make to function work you need to download the github repository bt-client-demo from its original
 creator and compile it into a .jar-file using intellij idea. after that, take said .jar and include it in your
 project as a java library
 
### [0.01.3] (2019-08-11)
 the stream torrent client is now working in demo mode! the first "version" of stream is now live an can be used
 trough the environment  of your ide. all you have to do is follow the installation instructions below and locate
 the file "UI_Controller.java" where you can insert a magnet-link and begin downloading.
 
### [0.01.2] (2019-07-29)
 worked out some test cases for the bt torrent library, updated ui design and readme.md, 
 further updates to be announced
 
### [0.01.1] (2019-07-21)
 cloned private repository from my github account to this repository, further updates comming soon ...
