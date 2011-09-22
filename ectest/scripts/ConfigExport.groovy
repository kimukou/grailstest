//
// locating grails-app 
// run 
//		grails prod ConfigExport
// 
includeTargets << grailsScript("_GrailsSettings")
//==== loading Config.groovy ===
includeTargets << grailsScript("_GrailsPackage")
//==== loading Config.groovy ===

@GrabResolver(name="kobo-maven-repo", root="https://github.com/kobo/maven-repo/raw/master/snapshot")
//@GrabConfig(systemClassLoader=true) // ★GroovyServ need
//@Grapes([
//	@Grab("org.jggug.kobo:gexcelapi:0.3-SNAPSHOT"),
//	@GrabExclude(':xmlbeans')
//])
@Grab("org.jggug.kobo:gexcelapi:0.3-SNAPSHOT")
@Grab("commons-io:commons-io:2.0.1")

import org.jggug.kobo.gexcelapi.GExcel
import org.apache.poi.hssf.usermodel.*
import org.apache.poi.ss.usermodel.*

target(main: "config.groovy parse=>to excel") {

	GExcel.metaClass.'static'.create << { 
		def wb = new HSSFWorkbook() 
		wb.createSheet()
		wb
	}

	Workbook.metaClass.save << { filename->
		FileOutputStream out = new FileOutputStream(filename)
		delegate.write(out)
		out.close()
	}


	// Excel file read(need Excel 97-2007.xls format)
	//def book = GExcel.open("template.xls")
	def book = GExcel.create()

	// 1st sheet get
	def sheet = book[0] // 1st sheet

//==== loading Config.groovy ===
/*
	String str = new File("grails-app/conf/Config.groovy").getText("UTF-8")
	//println str
	def config  = new ConfigSlurper().parse(str)
	//println config
*/
	//compile()
	def config = createConfig()
//==== loading Config.groovy ===

	index = 3

	sheet.A2.value = "define"
	sheet.B2.value = "value"
	println config.flatten().each{k,v->
		if(k=="log4j"){
			sheet.getProperty("A${index}").value =(String)k
			sheet.getProperty("B${index}").value =(String)v.dump()
			index++

/*
			println config.log4j.appenders.flatten()
			config.log4j.appenders.flatten().each{kk,vv->
				sheet.getProperty("B${index}").value =(String)kk
				sheet.getProperty("C${index}").value =(String)vv
				index++
			}
			println config.log4j.root.flatten()
			config.log4j.root.flatten().each{kk,vv->
				sheet.getProperty("B${index}").value =(String)kk
				sheet.getProperty("C${index}").value =(String)vv
				index++
			}
*/
		}
		else{
			sheet.getProperty("A${index}").value =(String)k
			sheet.getProperty("B${index}").value =(String)v
		}
		//println k
		//println v
		index++
	}
	//new File("${grailsAppName}_define_config.xls").withOutputStream { book.write(it) } 
	book.save "${grailsAppName}_define_config.xls"

	//def desktop = java.awt.Desktop.getDesktop()
	//desktop.open new File("${grailsAppName}_define_config.xls")
}
setDefaultTarget(main)

