call ../setEnvG9.bat

call grails -Denv=standalone createStandaloneWar
pause 
java -jar ../target/ectest.war

