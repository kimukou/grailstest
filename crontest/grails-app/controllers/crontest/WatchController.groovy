package crontest
import util.*


class WatchController {
    def beforeInterceptor ={
      log.info "[PRE]action:$actionUri params=${params}"
    }
    def afterInterceptor  ={
      log.info "[AFT]action:$actionUri"
    }

    def watchService
    def tellService

    def index = { 
        watchService.serviceMethod()
        tellService.serviceMethod()
        redirect(controller: "logout")
    }
}
