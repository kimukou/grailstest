//
// http://d.hatena.ne.jp/mottsnite/20100516/1274025543
//

import org.codehaus.groovy.tools.groovydoc.GroovyDocTool
import org.codehaus.groovy.groovydoc.GroovyRootDoc
import static groovy.io.FileType.*

includeTargets << grailsScript("Init")

/**
 * Generate i18n messages.properties from the domain class comments.
 * support version: grails-1.3.0 or above
 * usage: grails generate-i18n-labels ja
 */
target(main: "Generate i18n messages.properties from the domain class comments") {
  depends(parseArguments)

  String lang = argsMap['params']?argsMap['params'][0]:''

  File domainDir = new File('grails-app','domain')
  List srcFiles =[]
  String domainSrcPath = domainDir.absolutePath+'/'
  
  domainDir.eachFileRecurse FILES,{ srcFiles<<(it.absolutePath - domainSrcPath) }
  
  def docTool = new GroovyDocTool([domainSrcPath] as String[])
  docTool.add(srcFiles)
  GroovyRootDoc root = docTool.getRootDoc()
  def classes = root.classes()
  def excludeProp = ['constraints','hasMany','belongsTo','mapping']
  def generateI18nPropLabelFor = {d,propFile->
    def comment = d.getRawCommentText()
    def classLabel = comment.find(/@label(.*)/){it[1].trim()}?:d.firstSentenceCommentText()
    def className = d.name()
    def propName = className[0].toLowerCase() + className[1..-1]
    
    propFile.append "\n\n${propName}.label=${classLabel}\n"
    d.properties().each{p->
      if(!excludeProp.contains(p.name()))
      propFile.append "${propName}.${p.name()}.label=${p.commentText().trim()?:p.name()}\n"
    }
  }
  
  File propertiesFile = new File("${basedir}/grails-app/i18n/messages${lang.length()>0?'_'+lang:''}.properties")
  classes.each { generateI18nPropLabelFor it, propertiesFile }
}

setDefaultTarget(main)
