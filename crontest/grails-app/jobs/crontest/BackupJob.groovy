package crontest


//
// see http://grails.org/plugin/quartz
//     http://www.h2database.com/html/tutorial.html
//
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.web.util.WebUtils

class BackupJob {
//  cronExpression: "s m h D M W Y"
//                   | | | | | | `- Year [optional]
//                   | | | | | `- Day of Week, 1-7 or SUN-SAT, ?
//                   | | | | `- Month, 1-12 or JAN-DEC
//                   | | | `- Day of Month, 1-31, ?
//                   | | `- Hour, 0-23
//                   | `- Minute, 0-59
//                   `- Second, 0-59
  
  
   static triggers = {
      cron name: 'myTrigger', cronExpression: ApplicationHolder.application.config.grails.backup.cronExpression
   }
   def group = "ectest"
  
   def execute(){
     log.debug "=====> backup job start! ${new Date()}"
     
     def rootDir = ApplicationHolder.application.config.grails.backup.path
       if(new File("$rootDir/${new Date().format('yyyyMMdd')}").exists()==false){
        new File("$rootDir/${new Date().format('yyyyMMdd')}").mkdirs()
     }
     def filename = "$rootDir/${new Date().format('yyyyMMdd')}/${new Date().format('yyyyMMdd-hhmmss')}.zip"
      
     def args =[
        '-url', ApplicationHolder.application.config.dataSource.url,
      '-user', ApplicationHolder.application.config.dataSource.username, 
      '-script',filename,
      '-options', 'compression','zip'
      ] as String[]
     org.h2.tools.Script.main(args ) 
     log.debug "<===== backup job end! ${new Date()}"
   }
}
