// grails run-it
// grails test-app -integration -no-reports 相当を行う
includeTargets << grailsScript("Init")
includeTargets << grailsScript("_GrailsClean")
includeTargets << grailsScript("_GrailsTest")

target(main: "Run a Grails application integration tests") {
  depends(checkVersion, configureProxy, parseArguments, cleanTestReports)
  phasesToRun << "integration"
  createTestReports = false
  allTests()
}

setDefaultTarget(main)
