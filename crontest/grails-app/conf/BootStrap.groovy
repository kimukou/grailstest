import crontest.*

class BootStrap {

	def springSecurityService

    def init = { servletContext ->
		  if(User.count()==0){
		        def adminRole = new Authority(authority: 'ROLE_ADMIN',name:'管理者').save(flush: true)
		        def userRole = new Authority(authority: 'ROLE_USER',name:'一般ユーザ').save(flush: true)
		        assert Authority.count() == 2

                
                def adminuser =new Expando()
                adminuser.name = 'admin'
                adminuser.pass = 'admin'//springSecurityService.encodePassword('admin')
                
                def user01 =new Expando()
                user01.name = 'user01'
                user01.pass = 'user01'//springSecurityService.encodePassword('user01')
    
                
		        def admin = new User(username:adminuser.name, enabled:true, password:adminuser.pass,userRealName:'管理者').save(flush: true)
		        def user = new User(username:user01.name, enabled:true, password: user01.pass ,userRealName:'一般ユーザ').save(flush: true)
		        assert User.count() == 2

		        UserAuthority.create admin, adminRole, true
		        UserAuthority.create admin, userRole, true
		        UserAuthority.create user, userRole, true
		        assert UserAuthority.count() == 3
		        
		        //see http://blog.springsource.com/2010/08/11/simplified-spring-security-with-grails/
		        new Requestmap(url: '/quartz/**', configAttribute: 'ROLE_USER').save()
		    }
    }
    def destroy = {
    }
}
