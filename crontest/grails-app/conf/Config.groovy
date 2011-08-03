// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

}

// log4j configuration
/*
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}
*/

//
//
//		http://d.hatena.ne.jp/nobeans/20090301/1235881796
//

// log4j configuration
import org.apache.log4j.rolling.RollingFileAppender
import org.apache.log4j.rolling.TimeBasedRollingPolicy

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    def rollingFile = new RollingFileAppender(name: 'rollingFileAppender', layout: pattern(conversionPattern: "%d [%t] %-5p %c{2} %x - %m%n"))
    def rollingPolicy = new TimeBasedRollingPolicy(fileNamePattern: 'logs/backup/app.%d{yyyy-MM-dd}.gz', activeFileName: 'logs/app.log')
    rollingPolicy.activateOptions()
    rollingFile.setRollingPolicy rollingPolicy

    def cronrollingFile = new RollingFileAppender(name: 'cronFileAppender', layout: pattern(conversionPattern: "%d [%t] %-5p %c{2} %x - %m%n"))
    def cronrollingPolicy = new TimeBasedRollingPolicy(fileNamePattern: 'logs/backup/cron.%d{yyyy-MM-dd}.gz', activeFileName: 'logs/cron.log')
    cronrollingPolicy.activateOptions()
    cronrollingFile.setRollingPolicy cronrollingPolicy

    def logoutFile = new RollingFileAppender(name: 'logoutFileAppender', layout: pattern(conversionPattern: "%d [%t] %-5p %c{2} %x - %m%n"))
    def logoutPolicy = new TimeBasedRollingPolicy(fileNamePattern: 'logs/backup/logout.%d{yyyy-MM-dd}.gz', activeFileName: 'logs/logout.log')
    logoutPolicy.activateOptions()
    logoutFile.setRollingPolicy logoutPolicy

    def utilFile = new RollingFileAppender(name: 'utilFileAppender', layout: pattern(conversionPattern: "%d [%t] %-5p %c{2} %x - %m%n"))
    def utilPolicy = new TimeBasedRollingPolicy(fileNamePattern: 'logs/backup/util.%d{yyyy-MM-dd}.gz', activeFileName: 'logs/util.log')
    utilPolicy.activateOptions()
    utilFile.setRollingPolicy utilPolicy

    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: '%c{2} %m%n'), layout
        //file name: 'stactrace', file: 'logs/stactrace.log'
        //rollingFile name:"myAppender", maxFileSize:1024, fileName:"/tmp/logs/myApp.log
        appender cronrollingFile
        appender rollingFile
        appender utilFile
    }

    debug  'cronFileAppender': "grails.app.task",// quartz
    			 'logoutFileAppender': [
								"grails.app.controller.LoginController",
								"grails.app.controller.LogoutController"
						],
						'utilFileAppender':"grails.app.service.util"

    info   'rollingFileAppender': [
        'org.mortbay.log',
        'grails.app.controller',
        'grails.app.service.crontest'
    ]

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'
/*
    root {
        debug 'stdout'
        //debug 'stdout','cronFileAppender'
        //warn  'stdout','rollingFileAppender'
        additivity = false
    }
*/
}

// h2database console setting
//   url is http://localhost:8080/ecg-ana/h2-console/
//
plugins {
  h2 {
    //system{
    //  bindAddress = "127.0.0.1"
    //}
    /**
     * For console.standalone, tcpserver, and pgserver, 'disable' is a special option, and the rest are H2 Server options.
     * Check http://www.h2database.com/javadoc/org/h2/tools/Server.html#r8 
     */
    console {
      servlet {
        disable = true; mapping = '/h2-console/*' //must end with '/*'
      }
      standalone { // refer to the -web* options
        disable = true; webPort = 8082; webAllowOthers = true; //webSSL = false;
      }
    }
    tcpserver { disable = true; tcpPort = 8043; tcpAllowOthers = true }
    pgserver { disable = true; pgPort = 5432; pgAllowOthers = true; baseDir = './data/h2'; trace = '' }
  }
}

grails{
	backup{
		path='backup'
		cronExpression="0 0/10 * * * ?" 
	}
}


// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'crontest.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'crontest.UserAuthority'
grails.plugins.springsecurity.authority.className = 'crontest.Authority'
grails.plugins.springsecurity.requestMap.className = 'crontest.Requestmap'
grails.plugins.springsecurity.securityConfigType = grails.plugins.springsecurity.SecurityConfigType.Requestmap//'Requestmap'

grails.plugins.springsecurity.successHandler.defaultTargetUrl='/quartz/list'

