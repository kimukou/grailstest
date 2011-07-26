call ../setEnvG9.bat

::set GRADLE_OPTS=-Dorg.gradle.daemon=true

gradle run-app
::gradle run-app > hogehoge.txt 2>&1
::gradle run-app -Dserver.port=8090 --info> hogehoge.txt 2>&1

