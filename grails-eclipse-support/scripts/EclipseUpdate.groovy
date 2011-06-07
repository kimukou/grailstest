/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 * 
 * modify 20110605 kimukou.buzz
 */

import grails.util.PluginBuildSettings
//import static griffon.util.GriffonApplicationUtils.is64Bit
//import static griffon.util.GriffonApplicationUtils.isWindows

import groovy.xml.MarkupBuilder

includeTargets << grailsScript("Init")
includeTargets << grailsScript("_PluginDependencies")

target(updateEclipseClasspath: "Update the application's Eclipse classpath file") {
    updateEclipseClasspathFile()
}
setDefaultTarget('updateEclipseClasspath')

//2011/06/06 kimukou.buzz add start
def dirCheckFile ={
    ['grails-app', 'src', 'test'].each { base ->
        switch(base){
          case 'grails-app':
            ant.mkdir(dir:"$base/conf/hibernate")
            ant.mkdir(dir:"$base/conf/spring")
            ant.mkdir(dir:"$base/controllers")
            ant.mkdir(dir:"$base/domain")
            ant.mkdir(dir:"$base/services")
            ant.mkdir(dir:"$base/taglib")
            ant.mkdir(dir:"$base/utils")
            ant.mkdir(dir:"$base/views")
            break;
          case 'src':
            ant.mkdir(dir:"$base/groovy")
            ant.mkdir(dir:"$base/java")
            break;
          case 'test':
            ant.mkdir(dir:"$base/integration")
            ant.mkdir(dir:"$base/unit")
            break;
        }
    }
}
//2011/06/06 kimukou.buzz add end

updateEclipseClasspathFile = { newPlugin = null ->
    println "Updating Eclipse classpath file..."

    if(newPlugin) event('SetClasspath', [classLoader])
    //grailsSettings.resetDependencies()
    resetDependencies()
    def visitedDependencies = []

    boolean isWindows = System.getProperty("os.name").matches("Windows.*")
    boolean is64Bit = false
    String platform = ""

    //String userHomeRegex = isWindows ? userHome.toString().replace('\\', '\\\\') : userHome.toString()
    //String grailsHomeRegex = isWindows ? grailsHome.toString().replace('\\', '\\\\') : grailsHome.toString()
    String userHomeRegex = isWindows ? userHome.toString().replace('\\', '/') : userHome.toString()
    String grailsHomeRegex = isWindows ? grailsHome.toString().replace('\\', '/') : grailsHome.toString()

//println "userHomeRegex=$userHomeRegex"
//println "grailsHomeRegex=$grailsHomeRegex"

    String indent = '    '
    def writer = new PrintWriter(new FileWriter('.classpath'))
    def xml = new MarkupBuilder(new IndentPrinter(writer, indent))
    xml.setDoubleQuotes(true)
    xml.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8')
    xml.mkp.comment("Auto generated on ${new Date()}")
    xml.mkp.yieldUnescaped '\n'
    
    dirCheckFile()  //2011/06/06 kimukou.buzz add

    xml.classpath {
        mkp.yieldUnescaped("\n${indent}<!-- source paths -->")
        ['grails-app', 'src', 'test'].each { base ->
            //need git commit src if not exists
            if(!new File(base).exists())ant.mkdir(dir:base)
            new File(base).eachDir { dir ->
                if (! (dir.name =~ /^\..+/) && dir.name != 'templates') {
                    classpathentry(kind: 'src', path: "${base}/${dir.name}")
                }
            }
        }
        buildConfig.eclipse?.classpath?.include?.each { dir ->
            File target = new File(basedir, dir)
            if(target.exists()) classpathentry(kind: 'src', path: dir)
        }        

        mkp.yieldUnescaped("\n${indent}<!-- output paths -->")
        classpathentry(kind: 'con', path: 'org.eclipse.jdt.launching.JRE_CONTAINER')
        if(isWindows){
          classesDirPath = classesDirPath.replace('\\', '/')
        }

        classpathentry(kind: 'output', path: classesDirPath.replaceFirst(~/$userHomeRegex/, 'USER_HOME'))
        // [TODO]2011/06/06 classesDirPath is different ?
        //classpathentry(kind: 'output', path: "web-app/WEB-INF/classes")

        def normalizeFilePath = { file ->
            String path = file.absolutePath
            if(isWindows){
              path = path.replace('\\', '/')
            }
            String originalPath = path
            path = path.replaceFirst(~/$grailsHomeRegex/, 'GRAILS_HOME')
            path = path.replaceFirst(~/$userHomeRegex/, 'USER_HOME')
            boolean var = path != originalPath
            originalPath = path
            if(isWindows){
              basedirPath = grailsSettings.baseDir.path.replace('\\', '/')
//println "basedirPath=$basedirPath"
              path = path.replaceFirst(~/${basedirPath}(\/)/, '')
//println "originalPath=$originalPath"
//println "path=$path"
            }
            else{
              path = path.replaceFirst(~/${grailsSettings.baseDir.path}(\\|\/)/, '')
            }
            var = path == originalPath && !path.startsWith(File.separator)
//println "[OK]var=$var"
//println "[OK]path=$path"
//println ""
            [kind: var? 'var' : 'lib', path: path]
        }
        def visitDependencies = {List dependencies ->
            dependencies.each { File f ->
                if(visitedDependencies.contains(f)) return
                visitedDependencies << f
                Map pathEntry = normalizeFilePath(f)
                classpathentry(kind: pathEntry.kind, path: pathEntry.path)   
            }    
        }
               
        mkp.yieldUnescaped("\n${indent}<!-- runtime -->")
        visitDependencies(grailsSettings.runtimeDependencies)
        mkp.yieldUnescaped("\n${indent}<!-- test -->")
        visitDependencies(grailsSettings.testDependencies)
        mkp.yieldUnescaped("\n${indent}<!-- compile -->")
        visitDependencies(grailsSettings.compileDependencies)
        mkp.yieldUnescaped("\n${indent}<!-- build -->")
        visitDependencies(grailsSettings.buildDependencies)

        def visitPlatformDir = {libdir ->
            def nativeLibDir = new File("${libdir}/${platform}")
            if(nativeLibDir.exists()) {
                nativeLibDir.eachFileMatch(~/.*\.jar/) { file ->
                    Map pathEntry = normalizeFilePath(file)
                    classpathentry(kind: pathEntry.kind, path: pathEntry.path)   
                }
            }
/*
            nativeLibDir = new File("${libdir}/${platform[0..-3]}")
            if(is64Bit && nativeLibDir.exists()) {
                nativeLibDir.eachFileMatch(~/.*\.jar/) { file ->
                    Map pathEntry = normalizeFilePath(file)
                    classpathentry(kind: pathEntry.kind, path: pathEntry.path)   
                }
            }
*/
        }
        mkp.yieldUnescaped("\n${indent}<!-- platform specific -->")
        //visitPlatformDir(new File("${basedir}/lib"))

        doWithPlugins{ pluginName, pluginVersion, pluginDir ->
            if("${pluginName}-${pluginVersion}" == newPlugin) return
            def libDir = new File(pluginDir, 'lib')
            visitPlatformDir(libDir)
        }
        if(newPlugin) {
            def libDir = new File([pluginsHome, newPlugin, 'lib'].join(File.separator))
            visitPlatformDir(libDir)
        }
    }
    updateEclipseProjectFile(newPlugin)
}



updateEclipseProjectFile = { newPlugin = null ->
    println "Updating Eclipse project file..."

    if(newPlugin) event('SetProjectpath', [classLoader])

    boolean isWindows = System.getProperty("os.name").matches("Windows.*")

    String userHomeRegex = isWindows ? userHome.toString().replace('\\', '/') : userHome.toString()
    String grailsHomeRegex = isWindows ? grailsHome.toString().replace('\\', '/') : grailsHome.toString()

    String indent = '    '
    def writer = new PrintWriter(new FileWriter('.project'))
    def xml = new MarkupBuilder(new IndentPrinter(writer, indent))
    xml.setDoubleQuotes(true)
    xml.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8')
    xml.mkp.comment("Auto generated on ${new Date()}")
    xml.mkp.yieldUnescaped '\n'
    


    def normalizeFilePathP = { file ->
        String path = file.absolutePath
        if(isWindows){
          path = path.replace('\\', '/')
        }
        String originalPath = path
        path = path.replaceFirst(~/$grailsHomeRegex/, 'GRAILS_HOME')
        path = path.replaceFirst(~/$userHomeRegex/, 'USER_HOME')
        boolean var = path != originalPath
        originalPath = path
        if(isWindows){
          basedirPath = grailsSettings.baseDir.path.replace('\\', '/')
          path = path.replaceFirst(~/${basedirPath}(\/)/, '')
        }
        else{
          path = path.replaceFirst(~/${grailsSettings.baseDir.path}(\\|\/)/, '')
        }
        path == originalPath && !path.startsWith(File.separator)
        return path.toString()
    }

    def visitPlatformDirP = {mkp,pluginName, pluginVersion,baseDir ->
      if(!baseDir.exists())return
      baseDir.eachDir { dir ->
        if (! (dir.name =~ /^\..+/) && dir.name != 'templates' && dir.name != 'docs') {
            mkp.yieldUnescaped("\n${indent}${indent}<link>")
            mkp.yieldUnescaped("\n${indent}${indent}${indent}<name>${pluginName}-${pluginVersion}-${baseDir.name}-${dir.name}</name>")
            mkp.yieldUnescaped("\n${indent}${indent}${indent}<type>2</type>")
            mkp.yieldUnescaped("\n${indent}${indent}${indent}<location>${normalizeFilePathP(dir)}</location>")
            mkp.yieldUnescaped("\n${indent}${indent}</link>")
        }
      }
    }

    xml.projectDescription {
       mkp.yieldUnescaped("\n${indent}<name>$grailsAppName</name>")
       comment()
       projects()
       mkp.yieldUnescaped("\n\n${indent}<!-- buildSpec -->")

       mkp.yieldUnescaped("\n${indent}<buildSpec>")
         buildCommand(){
            mkp.yieldUnescaped("\n${indent}<name>org.eclipse.wst.common.project.facet.core.builder</name>")
            arguments()
         }
         buildCommand(){
            mkp.yieldUnescaped("\n${indent}<name>org.eclipse.jdt.core.javabuilder</name>")
            arguments()
         }
       mkp.yieldUnescaped("\n${indent}</buildSpec>")
       mkp.yieldUnescaped("\n\n${indent}<!-- natures -->")
       natures(){
            nature('com.springsource.sts.grails.core.nature')
            nature('org.eclipse.jdt.groovy.core.groovyNature')
            nature('org.eclipse.jdt.core.javanature')
            nature('org.eclipse.wst.common.project.facet.core.nature')
       }
       mkp.yieldUnescaped("\n\n${indent}<!-- linkedResources -->")
       mkp.yieldUnescaped("\n${indent}<linkedResources>")
         doWithPlugins{ pluginName, pluginVersion, pluginDir ->
              if("${pluginName}-${pluginVersion}" == newPlugin) return
              ["$pluginDir/grails-app", "$pluginDir/src", "$pluginDir/test"].each { base ->
                  def baseDir = new File(base)
                  visitPlatformDirP(mkp,pluginName, pluginVersion,baseDir)
              }
         }
         if(newPlugin) {
              pluginDirTmp = new File(pluginsHome, newPlugin)
              ["$pluginDirTmp/grails-app", "$pluginDirTmp/src", "$pluginDirTmp/test"].each { base ->
                  def baseDir = new File(base)
                  visitPlatformDirP(mkp,pluginName, pluginVersion,baseDir)
              }
              
         }
       mkp.yieldUnescaped("\n${indent}</linkedResources>")
    }
}




//==============================================================================================
//[REMARKS] griffon events copy
doWithPlugins = { callback = null ->
    if(!callback) return

    def plugins = metadata.findAll { it.key.startsWith("plugins.")}.collect {
       [
        name:it.key[8..-1],
        version: it.value
       ]
    }

    for(p in plugins) {
        def name = p.name
        def version = p.version
        // def fullName = "$name-$version"
        def pluginDir = getPluginDirForName(name)
        if(!pluginDir) installPluginForName(name)
    }

    // read again as the list might have been updated
    metadata.findAll { it.key.startsWith("plugins.")}.collect {
        name = it.key[8..-1]
        version = it.value
        def pluginDir = getPluginDirForName(name)
        callback(name, version, pluginDir.file)
    }
}


//BuildSettings
public void resetDependencies() {
    resetCompileDependencies()
    resetRuntimeDependencies()
    resetTestDependencies()
    resetProvidedDependencies()
    resetBuildDependencies()
}

public void resetCompileDependencies() {
    grailsSettings.compileDependencies.clear()
    grailsSettings.compileDependencies.addAll(grailsSettings.defaultCompileDependencies)
}

public void resetRuntimeDependencies() {
    grailsSettings.runtimeDependencies.clear()
    grailsSettings.runtimeDependencies.addAll(grailsSettings.defaultRuntimeDependencies)
}

public void resetTestDependencies() {
    grailsSettings.testDependencies.clear()
    grailsSettings.testDependencies.addAll(grailsSettings.defaultTestDependencies)
}

public void resetProvidedDependencies() {
    grailsSettings.providedDependencies.clear()
    grailsSettings.providedDependencies.addAll(grailsSettings.providedDependencies)
}

public void resetBuildDependencies() {
    grailsSettings.buildDependencies.clear()
    grailsSettings.buildDependencies.addAll(grailsSettings.buildDependencies)
}

