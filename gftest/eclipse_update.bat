call ../setEnvG9.bat

call grails install-plugin ../grails-eclipse-support/grails-eclipse-support-0.1.zip
call grails eclipse-update
call grails uninstall-plugin eclipse-support
::grails eclipse-update

