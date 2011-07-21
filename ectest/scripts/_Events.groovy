eventCreatedFile={fileName->
  println "==eventCreatedFile(${fileName})=="
  growlNotify(fileName)
}

eventExiting={returnCode->
  println "==eventExiting(${returnCode})=="
  growlNotify(returnCode)
}

eventStatusError= { msg ->
  println "==eventStatusError(${msg})=="
  growlNotify(msg)
}

eventStatusFinal = { msg ->
  println "==eventStatusFinal(${msg})=="
  growlNotify(msg)
}
 
eventStatusUpdate = { msg ->
  println "==eventStatusUpdate(${msg})=="
  growlNotify(msg)
}
 
growlNotify = { message ->
    println "==growlNotify(${message})=="
    //path="/usr/local/bin/growlnotify"
    path="c:/opt/Growl/growlnotify.exe"
    if(!new File(path).exists())return
    imgpath="${basedir}/web-app/images/grails_logo.png"

    ant.exec(executable:path) {
          arg(value:"/t:$grailsAppName")
          arg(value:"/i:\"${imgpath}\"")
          arg(value:"\"${message}\"")
    }
}
eventCleanStart = {event->
  println "== clean start =="
}

eventCleanEnd = {event->
  println "== clean end =="
}

eventRunStart ={event->
  println "== run start =="
}

eventRunEnd ={event->
  println "== run end =="
}

eventCompileStart ={event->
  println "== compile start =="
}


eventCompileEnd ={event->
  println "== compile end =="

  ant.gspc( destdir:classesDir,
            srcdir:"${basedir}/web-app",
            packagename:"${grailsAppName}_webapp",
            serverpath:"/",
            classpathref:"grails.compile.classpath",
            tmpdir:gspTmpDir)
}

eventWarStart ={event->
  println "== war start =="
/*
  ant.mkdir(dir:"${stagingDir}/WEB-INF/spring")
  ant.mkdir(dir:"${stagingDir}/WEB-INF/classes")
  ant.copy(file: "grails-app/conf/spring/resources.xml.org", todir: "${stagingDir}/WEB-INF/spring/resources.xml",overwrite:true)
  ant.copy(file: "grails-app/conf/log4j.xml", todir: "${stagingDir}/WEB-INF/classes",overwrite:true)
*/
}

eventWarEnd ={event->
  println "== war end =="

println basedir
	def upperDir =""
    if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
		upperDir = basedir.substring(0,basedir.lastIndexOf('\\'))
	}
	else{
		upperDir = basedir.substring(0,basedir.lastIndexOf('/'))
	}
println "upperDir=$upperDir"
  def jettyDeployDir = "${upperDir}/target"
println "jettyDeployDir=$jettyDeployDir"

  if(new File(jettyDeployDir).exists()==false)new File(jettyDeployDir).mkdir()
  ant.delete(file: "$jettyDeployDir/${warName}", quiet: true, failonerror: false)
  ant.delete(dir: "$jettyDeployDir/$grailsAppName", quiet: true, failonerror: false)
  ant.unzip(src:warName,dest:grailsAppName,overwrite:true)
	//config outside-setiing
	ant.copy(file: "grails-app/conf/Config.groovy", tofile: "$grailsAppName/WEB-INF/classes/${grailsAppName}-config.groovy",overwrite:true)
  ant.move(file:grailsAppName,todir:jettyDeployDir)
  //ant.move(file:warName,todir:jettyDeployDir)
  ant.copy(file:warName,todir:jettyDeployDir)

/*
  ant.delete(dir: "$grailsAppName", quiet: true, failonerror: false)
  ant.unzip(src:warName,dest:'$grailsAppName',overwrite:true)
  ant.copy(file: "grails-app/conf/spring/resources.xml.org", tofile: "$grailsAppName/WEB-INF/spring/resources.xml",overwrite:true)
  ant.copy(file: "grails-app/conf/log4j.xml", todir: "$grailsAppName/WEB-INF/classes",overwrite:true)
*/
}


//======================================================================================
// stac trace output
//   http://literal-ice.blogspot.com/2009/12/grailstest-appstacktrace.html
//
import grails.build.GrailsBuildListener;
public class StackTraceDumper implements GrailsBuildListener {
    void receiveGrailsBuildEvent(String name, Object[] args) {
        if (name == 'TestFailure') {
            this.doTestFailure(*args)
        }
    }
    protected doTestFailure(String name, failure, boolean isError) {
        failure?.printStackTrace()
    }
}
eventListener.addGrailsBuildListener(new StackTraceDumper())


//
// see http://literal-ice.blogspot.com/2010/07/grails133nullpointerexception.html
//
/*
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.plugins.PluginManagerHolder
 
eventTestPhaseStart = { phase ->
    if (phase == 'unit') {
        PluginManagerHolder.pluginManager = [hasGrailsPlugin: { String name -> true }] as GrailsPluginManager
    }
}
 
eventTestPhaseEnd = { phase ->
    if (phase == 'unit') {
        PluginManagerHolder.pluginManager = null
    }
}
*/
//======================================================================================

eventTestSuiteEnd = {typeName  ->
  new File(testReportsDir, "plain").eachFileMatch(~/.*Tests\.txt/) { file ->
    file.withReader("UTF-8") {reader ->
      reader.readLine()
      def line = reader.readLine()
      // JUnit log line 2 reading ,test failer.console output
      (line =~ /.*, Failures: (\d), Errors: (\d), .*/).each {m0, m1, m2 ->
        if (m1 == "0" && m2 == "0") return
        println "== ${file.name} =============================================="
        println line
        while ((line = reader.readLine()) != null) println line
      }
    }
  }
}
