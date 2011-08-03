package crontest

class WatchService {

    static transactional = false

    def serviceMethod() {
        log.info "WatchService::serviceMethod"
    }
}
