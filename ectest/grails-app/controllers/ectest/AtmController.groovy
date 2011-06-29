package ectest

import org.codehaus.groovy.grails.web.util.WebUtils
import org.codehaus.groovy.grails.commons.GrailsClassUtils


class AtmController {
	def atm1Service
	def atm2Service
	def atm3Service


	def rootDir
	AtmController(){
		super()
		rootDir = WebUtils.retrieveGrailsWebRequest().getServletContext().getRealPath("/")
	}

    def index = { 
		def id = params?.id==null ? array[0]:params?.id
		def idx = params?.idx==null ? 1:params?.idx

		def c = GrailsClassUtils.getPropertyOrStaticPropertyOrFieldValue(this,"atm${idx}Service")
		if (!c?.data) {
			c?.data = new Random().nextInt(150)
		}
		println "[$idx]id=${id}/c=${c.dump()}}"
		render(view:"index",model:[id:id,idx:idx])
	}

	def stop = {
		println "id=${params?.id}"
		println "idx=${params?.idx}"

		def c = GrailsClassUtils.getPropertyOrStaticPropertyOrFieldValue(this,"atm${params.idx}Service")
		//println "(${params?.id}) atm${params?.idx}Service: ${c.dump()}"
		c?.data = 0
		render(view:"index",model:[id:params.id,idx:params.idx])
	}


	def array=['test01','test02','test03','test04','test05']
	def getRandomId = {
	  array.with {
	    Collections.shuffle(it)
	    head()
	  }
	}

	def push ={
		//println broadcaster.dump()
		def c = GrailsClassUtils.getPropertyOrStaticPropertyOrFieldValue(this,"atm${params.idx}Service")
		println "atm${params.idx}Service:${c.dump()}}"
		10.times{
			if(c?.data ==0)return
			sleep 1000
			def burl = "/atmosphere/atmonitor${params.idx}"
			def broadth = broadcaster[burl.toString()].broadcast(getRandomId())
			println "$burl : ${broadth.dump()}"
		}
		render "success"
	}
}
