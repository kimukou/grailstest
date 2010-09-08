package org.grails.plugins.log4jxml

import org.apache.log4j.xml.DOMConfigurator
import org.apache.log4j.Logger
import org.apache.log4j.LogManager

public class Log4jConfigurationInitializer {
  Logger log = Logger.getLogger(Log4jConfigurationInitializer.class); //directly use Log4j! 
  static boolean initialized = false;

  def Log4jConfigurationInitializer() {
    if (!initialized) init()
  }

  def init() {
    if (log.isInfoEnabled()) log.info "Log4j XML Plugin - init():"
    org.apache.log4j.LogManager.resetConfiguration()

    // override Log4j DSL
    def log4jConfigHolder = org.grails.plugins.log4jxml.Log4jConfigurationHolder
    def log4jConfigClass = org.codehaus.groovy.grails.plugins.logging.Log4jConfig
    def configure = log4jConfigClass.metaClass.pickMethod('configure', [Closure] as Class[])
    log4jConfigClass.metaClass.configure = {Closure closure ->
      if (log4jConfigHolder.enabled) { //to override configure
        //org.apache.log4j.LogManager.resetConfiguration()
        println "Log4j XML Plugin - disabled Grails Log4j DSL"
      } else {
        configure.invoke(delegate, closure)
      }
    }
    if (log.isInfoEnabled()) log.info "Log4j XML Plugin - patched Log4j DSL"

    def log4j = [groovy: new File('./grails-app/conf/log4j.groovy'), xml: new File('./grails-app/conf/log4j.xml')]
    log4jConfigHolder.log4j = log4j;

    if (log.isDebugEnabled()) log.debug "Log4j XML Plugin - set log4j variable - log4j: ${log4j.collect {k, v -> k + ':' + v?.exists()}}. ./:${new File('./').absolutePath}"

    if (!log4j.groovy.exists() && !log4j.xml.exists()) {
      //TODO figure out how to fall back to the default Grails Log4j DSL
      //println "both log4j.groovy or log4j.xml are not available. Default Grails Log4j DSL will be used. Please define your logging configuration at Config.groovy"
      println "Log4j XML Plugin - please put a log4j.groovy or log4j.xml under /grails-app/conf, or uninstall this plugin"
      log.warn "Log4j XML Plugin - please put a log4j.groovy or log4j.xml under /grails-app/conf, or uninstall this plugin" 
    } else {
      if (log4j.groovy.exists()) {
        if (log4j.xml.exists()) {
          log4j.bak = new File('./grails-app/conf/log4j.xml.bak')
          if (log4j.bak.exists()) log4j.bak.delete()
          println "log4j.xml existed already, original file will be back up as log4j.xml.bak, existed bak will be overwritten"
          log.warn "log4j.xml existed already, original file will be back up as log4j.xml.bak, existed bak will be overwritten"
          log4j.xml.renameTo(log4j.bak)
          log4j.xml = new File('./grails-app/conf/log4j.xml')
        }

        log4j.string = new StringWriter()
        log4j.string << '<?xml version="1.0" encoding="UTF-8"?>' << '\n'
        log4j.string << '<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">' << '\n'
        Binding binding = new Binding(builder: new groovy.xml.MarkupBuilder(log4j.string))
        new GroovyShell(binding).evaluate('builder.' + log4j.groovy.text)
        log4j.xml.withOutputStream {out -> out << log4j.string.toString()}
        println "Log4j XML Plugin - log4j.groovy is found and transformed to log4j.xml. log4j.groovy.size(): ${log4j.groovy.size()}, log4j.xml.size(): ${log4j.xml.size()}"
        if (log.isInfoEnabled()) log.info "Log4j XML Plugin - log4j.groovy is found and transformed to log4j.xml. log4j.groovy.size(): ${log4j.groovy.size()}, log4j.xml.size(): ${log4j.xml.size()}"
      }

      if (log4j.xml.exists()) {
        println "Log4j XML Plugin - DOMConfigurator.configure(${log4j.xml.absolutePath}). log4j.xml.size: ${log4j.xml.size()}"
        if (log.isInfoEnabled()) log.info "Log4j XML Plugin - DOMConfigurator.configure(${log4j.xml.absolutePath}). log4j.xml.size: ${log4j.xml.size()}" 
        LogManager.resetConfiguration()
        DOMConfigurator.configure(log4j.xml.absolutePath)
        log4jConfigHolder.enabled = true
      } else {
        println "Log4j XML Plugin - failed to generate log4j.xml from log4j.groovy, please check exception log'}"
        log.error("Log4j XML Plugin - failed to generate log4j.xml from log4j.groovy, please check exception log'}")
      }
    }

    initialized = true
  }
}
