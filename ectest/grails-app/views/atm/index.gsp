<html>
  <head>
    <title>AtmTest</title>

	<g:javascript src="magneticPoetry/jquery-1.4.2.js" />
	<g:javascript src="magneticPoetry/jquery-ui-1.8.1.min.js" />
	<g:javascript src="magneticPoetry/jquery.atmosphere.js" />
	<g:javascript src="excanvas.js" />
	<g:javascript src="smoothie.js" />

<script type="text/javascript">
//Smoothie Chart add start
		var line = new TimeSeries();

		function initData(){
				var smoothie = new SmoothieChart();

				//data init
				addData(0);

				// Add to SmoothieChart
				smoothie.addTimeSeries(line);

				//slow add 1sec
				smoothie.streamTo(document.getElementById("mycanvas"),1000);
		}

		function addData(data){
				line.append(new Date().getTime(), data);
		}
//Smoothie Chart add end

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
													//window.alert(data);
													data_arr = data.split(",");
													$('#counter').text(data_arr[0]);
													addData(data_arr[1]);
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

<table border=0>
	<tr>
		<td>
			<canvas id="mycanvas" width="400" height="100"></canvas>
			<script type="text/javascript">
				initData()
			</script>
		</td>
	</tr>
	<tr>
		<td>
			<div id="counter"/>
		</td>
	</tr>
  </body>
</html>
