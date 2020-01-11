<h1 align="center">
    <a href="https://github.com/PichlerMartin/stream/">stream</a>
</h1>

<p align="center"><strong>
<sup>
A simple and comfy torrent client in Java 8
<br/><a href="http://bittorrent.org/beps/bep_0011.html">peer exchange</a> | <a href="http://bittorrent.org/beps/bep_0009.html">magnet links</a> | <a href="http://bittorrent.org/beps/bep_0005.html">DHT</a>
</sup>
</strong></p>

<p align="center"> 
<img src="https://i.ibb.co/28NTH0B/logo.png" height="400" widht="400">
</p>

<b>de</b>
<p align="justify">
    stream ist eine kostenlose Applikation zum Dateitransfer, die mithilfe der Programmiersprache Java umgesetzt wurde. Zur Datenübertragung wird das BitTorrent Protokoll genutzt. Mit diesem Protokoll ist es möglich, einfach und schnell auch große Dateien herunterzuladen und an andere zu verteilen. stream ist eine Diplomarbeit von Martin Pichler und Marcel Topeiner, die im Zuge der Ausbildung in der HTL Paul Hahn entstanden ist. Ziel dieser Arbeit war es, eine Java Applikation zu entwerfen, die auch von Personen benutzt werden kann, die sich weniger in dieser Materie auskennen. Trotzdem sollte die UI-Umsetzung möglichst modern und ansprechend aufgebaut sein.
</p>
<b>en</b>
<p align="justify">
    stream is a free application for filetransport, which was implemented with the programming language java. The BitTorrent-protocol is used for file transmission. With this protocol it's possible to download and share large files fast and easy. stream is a diploma thesis by Martin Pichler and Marcel Topeiner, which has been developed during their schoolarship at the Paul-Hahn-HTL (higher technical college). Goal of the work was to design a java application, which could be used by professionals as well as rookies. Another aspect aimed at the creation of a modern and appealing user-interface.
</p>
</div>


<p align="center">
    <img src="https://i.ibb.co/QYwvCdj/demo.gif" alt="Demo GIF">
</p>

## Announcement
because of the broad positive feedback which we received during the doors-open-days on the 10th and 11th of january we will leave this repository open to the public indefinitely

## Update Log

### update 07.01.2020
the stream client is now in closed-beta phase! the client is now working and options like switching languages with ResourceBundles, setting a default download directory and storing this data in java preferences have already been implemented.<br />
Here are some pictures of the updated ui:
![ui-settings](https://i.ibb.co/PmmC3p7/stream-UI-Add-Torrent.png)
picture of some settings:
![ui-settings](https://i.ibb.co/ZcqXhyS/stream-UI-Settings.png)

### update 29.11.2019
 in order to make to function work you need to download the github repository bt-client-demo from its original creator and compile it into a .jar-file using intellij idea. after that, take said .jar and include it in your project as a java library
 
### update 11.08.2019
 the stream torrent client is now working in demo mode! the first "version" of stream is now live an can be used trough the environment  of your ide. all you have to do is follow the installation instructions below and locate the file "UI_Controller.java" where you can insert a magnet-link and begin downloading.
 
### update 29.07.2019
 worked out some test cases for the bt torrent library, updated ui design and readme.md, 
 further updates to be announced
 
### udpate 21.07.2019
 cloned private repository from my github account to this repository, further updates comming soon ...
 
<!--
## use stream the way you like
 use stream to upload and download your favourite files, 
 you even can create torrents on your own!
-->
 
<!--
## ui preview (updated!)
![ui](https://i.ibb.co/jrQTcF3/Entwurf3.png)
-->

<!--
# splash screen preview
 ![splasj screen](https://i.ibb.co/G0877sL/Whats-App-Image-2019-07-29-at-01-07-26.jpg)
-->

<!--
# ui preview
 ![ui](https://i.ibb.co/ZHqmm01/MainPage.png)
-->

## Navigation

* **[HOME](https://github.com/PichlerMartin/stream)** – repository with documentation (will be added april 2020)
* **[TROUBLESHOOTING](#troubleshooting)** - solutions for some common problems

## Media

* **[Bt by atomashpolskiy](https://github.com/atomashpolskiy/bt)** - Library which is used to realize the backend capabilities of the application

## Installation instructions
 download or clone the zipped repository and open the project in
 IntelliJ IDEA. now you can compile and run the programm
 in the JVM.
 
### JDK
 
 in order to run stream perfectly you are required to have java installed. If your computer is not one of the 3 Billion lucky devices yet, its about time. access your webbrowser and dwonload java from the oracle website, after that follow the installation instructions of the installer. now you can continue with the JDK:
 (JDK)https://www3.ntu.edu.sg/home/ehchua/programming/howto/JDK_Howto.html
 
### IntelliJ IDEA

 after that has happened, follow the link below to get instructions on how to install the IDE IntelliJ IDEA:
 https://treehouse.github.io/installation-guides/windows/intellij-idea-win.html

### Maven

 you're almost there! now you need to set up the downloaded repository in IntelliJ IDEA. a guide to do so is listed below:
 https://subscription.packtpub.com/book/application_development/9781785286124/2/ch02lvl1sec24/creating-a-new-maven-project-in-intellij-idea
 
### VPN

 important: in this stage of development you need the free software "betternet vpn" in order to induce your downloads. download it here:
 https://www.betternet.co/

## Usage

 after that you can open the project in your intellij idea environment, navigate to the folder "testui" and start the file UI_Main.java (the one with the green arrow) after that the software starts and load a test sequence. more functionality is sadly not implemented yet
 
 if you'd like to see a demo of the ui start the file UI_Main.java in the folder "streamUI"
 
 if you fell a little confident you can try to set up an jar artifact an run said, but that hasn't been tested jet ...
 
 stay tuned for more breathtaking updates and the upcomming ui!
 
 
## What makes stream stand out from the crowd

### nice name

it does what it says

### And much more...

* _**check out [Release Notes](https://github.com/atomashpolskiy/bt/blob/master/RELEASE-NOTES.md#bt-release-notes) for details!**_
 
## Troubleshooting

### Torrent added, Download won't start

You may need to install and set up a VPN on your machine, we recommend https://www.betternet.co/

## Support and feedback

Any thoughts, ideas, criticism, etc. are welcome, as well as votes for new features to be added. You have the following option to share your ideas, receive help or report bugs:

* open a new [GitHub issue](https://github.com/PichlerMartin/stream/issues)
 
## List of supported BEPs (from https://github.com/atomashpolskiy/bt/blob/master/README.md)

* [BEP-3: The BitTorrent Protocol Specification](http://bittorrent.org/beps/bep_0003.html)
* [BEP-5: DHT Protocol](http://bittorrent.org/beps/bep_0005.html)
* [BEP-9: Extension for Peers to Send Metadata Files](http://bittorrent.org/beps/bep_0009.html)
* [BEP-10: Extension Protocol](http://bittorrent.org/beps/bep_0010.html)
* [BEP-11: Peer Exchange (PEX)](http://bittorrent.org/beps/bep_0011.html)
* [BEP-12: Multitracker metadata extension](http://bittorrent.org/beps/bep_0012.html)
* [BEP-14: Local Service Discovery](http://bittorrent.org/beps/bep_0014.html)
* [BEP-15: UDP Tracker Protocol](http://bittorrent.org/beps/bep_0015.html)
* [BEP-20: Peer ID Conventions](http://bittorrent.org/beps/bep_0020.html)
* [BEP-23: Tracker Returns Compact Peer Lists](http://bittorrent.org/beps/bep_0023.html)
* [BEP-27: Private Torrents](http://bittorrent.org/beps/bep_0027.html)
* [BEP-41: UDP Tracker Protocol Extensions](http://bittorrent.org/beps/bep_0041.html)
* [Message Stream Encryption](http://wiki.vuze.com/w/Message_Stream_Encryption)
