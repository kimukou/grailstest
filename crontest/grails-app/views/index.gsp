<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils"%>
<html>
    <head>
  <title>Welcome to Grails</title>
        <meta name="layout" content="main" />
        <style type="text/css" media="screen">

        #nav {
            margin-top:20px;
            margin-left:30px;
            width:228px;
            float:left;

        }
        .homePagePanel * {
            margin:0px;
        }
        .homePagePanel .panelBody ul {
            list-style-type:none;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody h1 {
            text-transform:uppercase;
            font-size:1.1em;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody {
            background: url(images/leftnav_midstretch.png) repeat-y top;
            margin:0px;
            padding:15px;
        }
        .homePagePanel .panelBtm {
            background: url(images/leftnav_btm.png) no-repeat top;
            height:20px;
            margin:0px;
        }

        .homePagePanel .panelTop {
            background: url(images/leftnav_top.png) no-repeat top;
            height:11px;
            margin:0px;
        }
        h2 {
            margin-top:15px;
            margin-bottom:15px;
            font-size:1.2em;
        }
        #pageBody {
            margin-left:280px;
            margin-right:20px;
        }
        </style>
    </head>
    <body>

		<g:form url="${request.contextPath}${SpringSecurityUtils.securityConfig.apf.filterProcessesUrl}" id='loginForm' class='cssform' autocomplete='off' method="post">
			<div>
				<label>ユーザー名 <br /> 
					<g:textField class="text-field"	name="j_username" value="${new org.apache.commons.codec.net.URLCodec().decode(request?.getCookie('username'))}" /> <br /> 
				</label> 
				<label>
					<br />パスワード <br /> 
					<g:passwordField class="text-field"	name="j_password" value="" /> <br /> </label> <br /> <br />
						<g:actionSubmitImage alt="ログイン" value="auth" class="over"	src="${resource(dir:'images',file:'springsource.png')}" />
				<br />
			</div>
		</g:form>
		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>

   </body>
</html>

