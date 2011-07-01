package ectest

import sun.util.logging.resources.logging;
import grails.converters.JSON

class Atm1Service {

	static transactional = false
	static atmosphere = [mapping: '/atmosphere/atmonitor1']
	def data = 0

	def onRequest = { event ->
        def request = event.request
        def response = event.response
		log.info "onRequest, method: ${event?.request?.method}"
		event.suspend()
	}

	def onStateChange = { event ->
		log.info "onStateChange, message: ${event?.message}"

		if (!event.message)return

		if (event.isResuming() || event.isResumedOnTimeout()) {
			// TODO : Traiter ce cas de figure ?
			return
		}
		//else {
			def data = event.message
			event.resource.response.writer.with {
				write "<script>parent.callback('${event.message}');</script>"
				flush()
			}
		//}
	}
}