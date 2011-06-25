see 
	https://bitbucket.org/bgoetzmann/grails-atmosphere-plugin/wiki/Home

	http://codeyeti.com/posts/ajax-push-in-grails-using-the-atmosphere-plugin/
---------------------------------------------
operation memo)

edit grails-app/conf/buildConfig.groovy
	runtime 'org.atmosphere:atmosphere-jgroups:0.7.2'

grails install-plugin atmosphere
grails create-atmosphere-handler ectest.Chat 

ectest.ChatAtmosphereHandler.groovy edit and add 
	void destroy(){

	}

grails create-service ectest.MagneticPoetryService
edit

grails-app/conf/AtmosphereConfig.groovy
