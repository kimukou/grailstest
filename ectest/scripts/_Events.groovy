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
  def upperDir = basedir.substring(0,basedir.lastIndexOf('\\'))
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

