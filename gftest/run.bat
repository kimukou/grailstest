set GROOVY_HOME=c:\opt\groovy-1.7.10
set GRAILS_HOME=C:\opt\grails-1.3.7
set JAVA_HOME=c:\opt\jdk

set PATH=%GROOVY_HOME%/bin;%GRAILS_HOME%/bin;%JAVA_HOME%/bin

grails run-app > hogehoge.txt 2>&1
::grails run-app
