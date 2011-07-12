package ectest

import org.codehaus.groovy.grails.commons.ApplicationHolder

//@Typed(TypePolicy.MIXED)
class MailController{
		
		def mailService

    def index = { 
			def sendflag = ApplicationHolder.application.config.grails.mail.sendflag
			if(sendflag==false){
				render(text:"""
				<xml>
						<value>CANCEL</value>
				</xml>""",contentType:"text/xml",encoding:"UTF-8")
				return
			}
			
      Map mail = [message:'こんにちは赤ちゃん']

      mailService.sendMail {
          to 'kimukou.buzz@gmail.com'
          from ApplicationHolder.application.config.grails.mail.from 
          subject ApplicationHolder.application.config.grails.mail.subject
          body g.render(template:"/mail/myMailTemplate", model:[mail:mail])
      }

			render(text:"""
				<xml>
						<value>OK</value>
				</xml>""",contentType:"text/xml",encoding:"UTF-8")
		}

}
