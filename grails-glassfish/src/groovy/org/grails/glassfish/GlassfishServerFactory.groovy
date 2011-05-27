package org.grails.glassfish

import grails.web.container.*

class GlassfishServerFactory implements EmbeddableServerFactory {

	def pluginSettings
	
	EmbeddableServer createInline(String basedir, String webXml, String contextPath, ClassLoader classLoader) {
		return new GlassfishServer(basedir, webXml, contextPath, classLoader)
	}

	EmbeddableServer createForWAR(String warPath, String contextPath) {
		return new GlassfishServer(warPath, contextPath)		
	}
}