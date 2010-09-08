class Log4jXmlGrailsPlugin {
    def version = '0.2'
    def dependsOn = [:],loadBefore=['logging']

    def author = "Mingfai Ma",authorEmail = "mingfai.ma@gmail.com"
    def title = "Allow configure log4j by Groovy/XML in Grails 1.1"
    def description = '''\\
 - detect if a log4j.groovy exists in grails-app/conf, if yes, generate a log4j.xml
 - detect if a log4j.xml exists
 - set log4j.configuration system property to log4j.xml
 - store the log4j.xml as string
 - provide a log4j controller to display the log4j.xml in development enviroment
'''
    def documentation = "http://grails.org/Log4j+XML+Plugin"

    def initializer = new org.grails.plugins.log4jxml.Log4jConfigurationInitializer()

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
