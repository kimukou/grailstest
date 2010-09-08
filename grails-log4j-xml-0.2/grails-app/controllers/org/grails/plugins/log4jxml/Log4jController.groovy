package org.grails.plugins.log4jxml

import grails.util.GrailsUtil

class Log4jController {

  def xml = {
    if (GrailsUtil.isDevelopmentEnv()) {
      log.info("show() - retrieved log4j.string")
      render( """
<html>
<head><title>log4j.xml</title></head>
<body>
<textarea style="width: 100%; height: 800px;">
${Log4jConfigurationHolder.log4j.string}
</textarea>
</body>
</html>
              """)
    } else {
      response.sendError 500
    }

  }
}
