//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/grails-app/jobs")
//


def configFile = new File("${basedir}/grails-app/conf/AtmosphereConfig.groovy")
if (!configFile.exists())
    configFile.write """\
atmospherePlugin {
    servlet {
    	// Servlet initialization parameters
    	// Example: initParams = ['org.atmosphere.useNative': 'true', 'org.atmosphere.useStream': 'false']
    	initParams = []
    	urlPattern = '/atmosphere/*'
    }
    handlers {
    	// This closure is used to generate the atmosphere.xml using a MarkupBuilder instance in META-INF folder
    	atmosphereDotXml = {
        	//'atmosphere-handler'('context-root': '/atmosphere/chat', 'class-name': 'com.odelia.grails.plugins.atmosphere.ChatAtmosphereHandler')
    	}
    }
}"""

// Write the context.xml file in META-INF and WEB-INF
def contextDotXml = """\
<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<Context>
    <Loader delegate=\"true\"/>
</Context>"""
new File("$basedir/web-app/META-INF/context.xml").write contextDotXml
new File("$basedir/web-app/WEB-INF/context.xml").write contextDotXml
