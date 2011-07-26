call ../setEnvG9.bat

::gradle test > hogehoge.txt 2>&1
::gradle war -PcmdOpt="ectest.zip"  > hogehoge.txt 2>&1
::gradle war > hogehoge.txt 2>&1
gradle run-app > hogehoge.txt 2>&1
