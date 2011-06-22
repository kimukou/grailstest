Ant.property(environment:"env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsCreateArtifacts")

target('default': "Creates a new Atmosphere handler") {
    depends(checkVersion, parseArguments)

    def type = "AtmosphereHandler"
    promptForName(type: type)

    def name = argsMap["params"][0]
    createArtifact(name: name, suffix: type, type: type, path: "grails-app/atmosphereHandlers")
    createUnitTest(name: name, suffix: type)
}