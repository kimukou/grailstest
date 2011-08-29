import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class LogoutController {

	def beforeInterceptor ={
		log.info "[PRE]action:$actionUri params=${params}"
	}
	def afterInterceptor  ={
		log.info "[AFT]action:$actionUri"
	}

	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
		// TODO put any pre-logout code here
		redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
	}
}
