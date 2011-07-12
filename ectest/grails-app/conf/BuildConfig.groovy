//grails.project.class.dir = "target/classes"
grails.project.class.dir = "web-app/WEB-INF/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
				runtime 'org.atmosphere:atmosphere-jgroups:0.7.2'
				compile 'net.sf.groovydice:groovydice:1.4.1'
				runtime 'net.sf.groovydice:groovydice:1.4.1'
    }
}
//grails.plugin.location."eclipse-support"="../grails-eclipse-support"
grails.plugin.location."grails-standalone"="../plugin_ext/grails-plugins-grails-standalone"
grails.plugin.location."groovy-plus-plus"="../plugin_ext/grails-groovy-plus-plus-0.4.217"
