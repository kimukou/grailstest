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

updateEclipseClasspathFile = { newPlugin = null ->
    println "Updating Eclipse classpath file..."

    if(newPlugin) event('SetClasspath', [classLoader])
    //grailsSettings.resetDependencies()
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
    xml.classpath {
        mkp.yieldUnescaped("\n${indent}<!-- source paths -->")
        ['grails-app', 'src', 'test'].each { base ->
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
//println "[○]var=$var"
//println "[○]path=$path"
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
        visitPlatformDir(new File("${basedir}/lib"))

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
}

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


