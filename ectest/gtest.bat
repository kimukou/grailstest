set JAVA_HOME=c:\opt\jdk
set GROOVY_HOME=C:\opt\Groovy-1.8.0
set GRIFFON_HOME=C:\opt\griffon-0.9.2
set GRAILS_HOME=C:\opt\grails-1.3.7
set GRADLE_HOME=D:\Tooldev\gradle-1.0-milestone-3

set PATH=%GRIFFON_HOME%/bin;%JAVA_HOME%/bin;%GROOVY_HOME%/bin;%GRAILS_HOME%/bin;%GRADLE_HOME%/bin
set JAVA_OPTS=-Dgroovy.source.encoding=UTF-8 -Dfile.encoding=UTF-8

::gradle test > hogehoge.txt 2>&1
::gradle war -PcmdOpt="ectest.zip"  > hogehoge.txt 2>&1
::gradle war > hogehoge.txt 2>&1
gradle run-app > hogehoge.txt 2>&1
