'log4j:configuration'("xmlns:log4j": "http://jakarta.apache.org/log4j/", debug: "false") {

	appender(name: 'async_dailyLog', 'class': "org.apache.log4j.AsyncAppender") {
		param name: "BufferSize", value: "64"
		'appender-ref'('ref':"dailyLog")
	}

	appender(name: 'async_stacktraceLog', 'class': "org.apache.log4j.AsyncAppender") {
		param name: "BufferSize", value: "64"
		'appender-ref'('ref':"stacktraceLog")
	}

	appender(name: 'async_CONSOLE', 'class': "org.apache.log4j.AsyncAppender") {
		param name: "BufferSize", value: "64"
		'appender-ref'('ref':"CONSOLE")
	}

  appender(name: 'dailyLog', 'class': "org.apache.log4j.DailyRollingFileAppender") {
    errorHandler 'class': "org.apache.log4j.helpers.OnlyOnceErrorHandler"
    param name: "File", value: "D:/workspace/test13/logs/run.log"
    param name: "Append", value: "false"
    param name: "DatePattern", value: "'.'yyyy-MM-dd"
    layout('class': "org.apache.log4j.PatternLayout") {
      param name: "ConversionPattern", value: "%d{yyyy/MM/dd HH:mm:ss.SSS},%p,%c{2},%M,%m%n"
    }
  }

  appender(name: 'stacktraceLog', 'class': "org.apache.log4j.DailyRollingFileAppender") {
    errorHandler 'class': "org.apache.log4j.helpers.OnlyOnceErrorHandler"
    param name: "File", value: "D:/workspace/test13/logs/stacktrace.log"
    param name: "Append", value: "false"
    param name: "DatePattern", value: "'.'yyyy-MM-dd"
    layout('class': "org.apache.log4j.PatternLayout") {
      param name: "ConversionPattern", value: "[%r] %c{2} %m%n"
    }
  }

  appender(name: "CONSOLE", 'class': "org.apache.log4j.ConsoleAppender") {
    errorHandler 'class': "org.apache.log4j.helpers.OnlyOnceErrorHandler"
    param name: "Target", value: "System.out"
    param name: "Threshold", value: "TRACE"
    layout('class': "org.apache.log4j.PatternLayout") {
      param name: "ConversionPattern", value: "[%r] %c{2} %m%n"
    }
  }


  'category'(name: 'org.codehaus.groovy.grails.web.servlet', additivity: 'false') {
	    'priority'(value: 'INFO'); 'appender-ref'('ref': "async_dailyLog"); 'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.codehaus.groovy.grails.web.pages', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.codehaus.groovy.grails.web.sitemesh', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'web.mapping.filter', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.codehaus.groovy.grails."web.mapping.filter', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.codehaus.groovy.grails."web.mapping', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.codehaus.groovy.grails.commons', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.codehaus.groovy.grails.plugins', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.codehaus.groovy.grails.orm.hibernate', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.springframework', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'org.hibernate', additivity: 'false') {
	    'priority'(value: 'INFO');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'grails.app', additivity: 'false') {
    'priority'(value: 'DEBUG');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'test13', additivity: 'false') {
    'priority'(value: 'DEBUG');'appender-ref'('ref': "async_dailyLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'category'(name: 'StackTrace', additivity: 'false') {
	    'priority'(value: 'DEBUG');'appender-ref'('ref': "async_stacktraceLog");'appender-ref'('ref': "async_CONSOLE")
  }

  'root' {
    'priority'(value: "WARN");'appender-ref'('ref': "async_dailyLog");'appender-ref'(ref: "async_CONSOLE")
  }
}
