set JAVA_HOME=c:\opt\jdk
set GROOVY_HOME=C:\opt\Groovy-1.7.10
set GRIFFON_HOME=C:\opt\griffon-0.9.2
set GRAILS_HOME=C:\opt\grails-1.3.7
set GRADLE_HOME=D:\Tooldev\gradle-1.0-milestone-3

set PATH=%GRIFFON_HOME%/bin;%JAVA_HOME%/bin;%GROOVY_HOME%/bin;%GRAILS_HOME%/bin;%GRADLE_HOME%/bin

gradle run-app
::gradle run-app -Dserver.port=8090 --info> hogehoge.txt 2>&1
