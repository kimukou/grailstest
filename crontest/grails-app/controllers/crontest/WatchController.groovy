package crontest
import util.*


class WatchController {
    def beforeInterceptor ={log.info "action:$actionUri"}

    def watchService
    def tellService

    def index = { 
        watchService.serviceMethod()
        tellService.serviceMethod()
        redirect(controller: "logout")
    }
}
