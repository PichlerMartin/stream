# package translator

this package contains the static class StreamTranslator which can be run via a main method. the purpose of this class
is to read a properties file from the resources directory and translate it to any given language. it aims to simplify
the process of translating every properties file one by one.

## first attempt

implemented restful webservice by "whatsmate", but this service works only on a limited free trial of 10 translations
professional usage requires a paid subscription, that's why this method is seen as unsuitable for our needs, and thus
being pursued no more

https://whatsmate.github.io/2016-08-18-translate-text-java/

## second attempt

tried to use proposed solution from stackoverflow, however this does not work anymore, because the requested server
which provides this webservice is unreachable, was removed in recent commit

https://stackoverflow.com/questions/8147284/how-to-use-google-translate-api-in-my-java-application

## third attempt

used google cloud services to translate text, but failed due to non-existing authentication credentials

## final solution

proceeded to translate the contents of english resource bundle by hand into spanish and french