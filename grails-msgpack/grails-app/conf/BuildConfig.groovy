grails.project.class.dir = "target/classes"
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
        mavenLocal()
        mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
        mavenRepo "http://msgpack.org/maven2/"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.org/maven2/"
        mavenRepo "http://repository.jboss.org/nexus/content/groups/public-jboss/"

    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        //compile group: 'org.codehaus.groovy', name: 'groovy-all', version: '1.8.0'
        compile group: 'commons-cli', name : 'commons-cli', version: '1.2'
        compile group: 'org.msgpack', name: 'msgpack', version: '0.5.2-devel'

        //■need 1.5.1 upper place into http://repository.jboss.org/maven2/
        compile 'org.slf4j:slf4j-api:1.5.8'
        compile 'org.slf4j:slf4j-log4j12:1.5.8'
        compile 'javassist:javassist:3.12.1.GA'
        // runtime 'mysql:mysql-connector-java:5.1.13'

        compile "org.msgpack:msgpack-rpc:0.6.1-devel"
        runtime "org.msgpack:msgpack-rpc:0.6.1-devel"
    }
}
