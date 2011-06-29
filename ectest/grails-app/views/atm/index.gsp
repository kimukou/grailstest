<html>
  <head>
    <title>AtmTest</title>
    <meta name="layout" content="atmosphere" />	    
	<atmosphere:resources />
	<g:javascript src="magneticPoetry/jquery-1.4.2.js" />
	<g:javascript src="magneticPoetry/jquery-ui-1.8.1.min.js" />
	<g:javascript src="magneticPoetry/jquery.atmosphere.js" />
	<g:javascript src="excanvas.js" />
	<script type="text/javascript">
		$(function(){

			function callback(response)
            {
                $.atmosphere.log('info', ["response.state: " + response.state]);
                $.atmosphere.log('info', ["response.transport: " + response.transport]);
                if (response.transport != 'polling' && response.state != 'connected' && response.state != 'closed') {
                    $.atmosphere.log('info', ["response.responseBody: " + response.responseBody]);
                    if (response.status == 200) {
                        var data = response.responseBody;
						if (data) { 
							$('#counter').text(data);
						}
                    }
                }
            }
						
			<% 
				def val="/atmosphere/atmonitor$idx"  
				println val
			%>
			$.atmosphere.subscribe('${resource(dir:val)}',			
					callback,
					$.atmosphere.request = {transport: 'streaming'});
			var connectedEndpoint = $.atmosphere.response;

	        $.get('${createLink(action: "push",params:[id:"$id",idx:"$idx"])}');
		});
	</script>
  </head>
  <body>
   	<g:link action="index" params='[id:"$id",idx:"$idx"]' >start</g:link>
	<g:link action="stop" params='[id:"$id",idx:"$idx"]'>stop</g:link></br>
	<div id="counter"/>
	<!--canvas id="counter" width="100" height="50"/-->
  </body>
</html>
