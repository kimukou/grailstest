package com.odelia.grails.plugins.atmosphere

import org.atmosphere.cpr.AtmosphereServlet
import javax.servlet.ServletConfig
import javax.servlet.ServletException


class StratosphereServlet extends AtmosphereServlet  {
	
	public final static def ATMOSPHERE_PLUGIN_ATMOSPHERE_SERVLET = 'com.odelia.grails.plugins.atmosphere.atmosphere.servlet'
	public final static def ATMOSPHERE_PLUGIN_HANDLERS_CONFIG = 'com.odelia.grails.plugins.atmosphere.handlers.config'
	public final static def ATMOSPHERE_PLUGIN_SERVICE_HANDLERS = 'com.odelia.grails.plugins.atmosphere.service.handlers'
	
	@Override
    protected void loadConfiguration(ServletConfig sc) throws ServletException {
		super.loadConfiguration sc		
		
		sc.servletContext.setAttribute(ATMOSPHERE_PLUGIN_ATMOSPHERE_SERVLET, this)
		sc.servletContext.setAttribute(ATMOSPHERE_PLUGIN_HANDLERS_CONFIG, atmosphereConfig.handlers())
		
		// Add services handlers
		def handlers = sc.servletContext.getAttribute(ATMOSPHERE_PLUGIN_SERVICE_HANDLERS)
		handlers.each {
			it.handler.setServletContext(sc.servletContext) 
			addAtmosphereHandler("${it.mapping}", it.handler)
		}			
    }
	
}
