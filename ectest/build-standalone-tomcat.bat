call ../setEnvG9.bat

::call grails war ectest.zip
call grails build-standalone ectest.jar -Dwarfile="../target/ectest.zip"
java -jar ectest.jar

