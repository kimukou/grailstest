grails.project.dependency.resolution = {
    repositories {
        inherits "global"
        mavenRepo "http://groovypp.artifactoryonline.com/groovypp/libs-releases-local/"
    }

    dependencies {
        runtime(
                [group:'org.mbte.groovypp', name:'groovypp-all', version:'0.4.217']
        )
    }
}