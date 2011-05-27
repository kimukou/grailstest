package org.grails.glassfish



import grails.web.container.EmbeddableServer
import grails.util.BuildSettingsHolder
import grails.util.BuildSettings

import sun.security.tools.KeyTool
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.util.FileCopyUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder


// ref     http://d.hatena.ne.jp/shin/20100207/p4
//         https://gist.github.com/851175
//
// javadoc http://javadoc.glassfish.org/v3/apidoc/index.html
//

//use 3.0
import org.glassfish.api.embedded.*
//use 3.1
import org.glassfish.internal.embedded.*

import org.glassfish.embeddable.*;
import org.glassfish.api.deployment.DeployCommandParameters

/**
 * An implementation of the EmbeddableServer interface for Glassfish.
 *
 * @see EmbeddableServer
 *
 * @author kimukou.buzz
 * @since 0.1
 *
 * Created: May 10, 2011
 */
public class GlassfishServer implements EmbeddableServer{

    BuildSettings buildSettings
    ConfigObject config

    def grailsServer
    def eventListener

    protected String keystore
    protected File keystoreFile
    protected String keyPassword

    //WebAppContext context
    def deployer
    protected boolean warRun = true
    String deployedApp
    String deployedHost

    /**
     * Creates a new GlassfishServer for the given war and context path
     */
    public GlassfishServer(String warPath, String contextPath) {
        super()
        initialize()
        grailsServer = new Server.Builder(deployedHost).build()
        grailsServer.addContainer(ContainerBuilder.Type.web)//all)
        deploy(warPath, contextPath)
    }

    /**
     * Constructs a Glassfish server instance for the given arguments. Used for inline, non-war deployment
     *
     * @basedir The web application root
     * @webXml The web.xml definition
     * @contextPath The context path to deploy to
     * @classLoader The class loader to use
     */
    public GlassfishServer(String basedir, String webXml, String contextPath, ClassLoader classLoader) {
        super();
        initialize()
        grailsServer = new Server.Builder(deployedHost).build()
        grailsServer.addContainer(ContainerBuilder.Type.web)//all)
        deploy(basedir, contextPath)
/*
        warRun = false
        def bootstrapProperties =[:]
        def glassfishProperties =[:]

        BootstrapProperties bootstrapOptions = new BootstrapProperties(bootstrapProperties)
        def gfr = GlassFishRuntime.bootstrap(bootstrapOptions, classLoader)
        GlassFishProperties gfOptions = new GlassFishProperties(glassfishProperties)
        grailsServer = gfr.newGlassFish(gfOptions);
        grailsServer.start()
        deploy(basedir, contextPath)
*/
    }


    void deploy(String war,String contextPath) {
        if(warRun){
          deployer = grailsServer.getDeployer()   
          DeployCommandParameters deployParams = new DeployCommandParameters()
          deployParams.name = deployedApp
          deployParams.contextroot = contextPath

	        File archive = new File(war)
          deployer.deploy(archive, deployParams)
        }
        else{
          deployer = grailsServer.getService(Deployer.class)
          deployedApp = deployer.deploy(new File(war).toURI(),"--contextroot=$contextPath".toString(), '--force=true')
        }
    }

    void undeploy() throws LifecycleException {
        if(warRun){
          deployer.undeploy(deployedApp, null)
        }
        else{
          deployer.undeploy(deployedApp)
        }
        grailsServer.stop()
    }



    /**
     * Initializes the GlassfishServer class
     */
    protected initialize() {
        this.buildSettings = BuildSettingsHolder.getSettings()
        this.config = ConfigurationHolder.getConfig()
        deployedApp = config.grails.project.groupId
        deployedHost = DEFAULT_HOST

        keystore = "${buildSettings.grailsWorkDir}/ssl/keystore"
        keystoreFile = new File("${keystore}")
        keyPassword = "123456"
        System.setProperty('org.mortbay.xml.XmlParser.NotValidating', 'true')
    }


    /**
     * @see EmbeddableServer#start()
     */
    void start() { start DEFAULT_PORT }

    /**
     * @see EmbeddableServer#start(int)
     */
    void start(int port) {
        start DEFAULT_HOST, port
    }

    /**
     * @see EmbeddableServer#start(String, int)
     */
    public void start (String host, int port) {
        host = host ?: 'localhost'
        if(warRun){
          grailsServer.createPort(port)
          grailsServer.start()
        }
        else{
/*
					//use 3.1
          CommandRunner commandRunner = grailsServer.getService(CommandRunner.class)
          // Run a command create 'my-http-listener' to listen at 8080
          CommandResult commandResult = commandRunner.run(
                  'create-http-listener', "--listenerport=$port".toString(),
                  '--listeneraddress=0.0.0.0', '--defaultvs=server',
                  'my-http-listener')

          // Run a command to create your own thread pool
          commandResult = commandRunner.run('create-threadpool',
                  '--maxthreadpoolsize=200', '--minthreadpoolsize=200',
                  'my-thread-pool')

          // Run a command to associate my-thread-pool with my-http-listener
          commandResult = commandRunner.run('set',
                  'server.network-config.network-listeners.network-listener.my-http-listener.thread-pool=my-thread-pool')
*/
        }
    }

    /**
     * @see EmbeddableServer#startSecure()
     */
    void startSecure() { startSecure DEFAULT_SECURE_PORT }

    /**
     * @see EmbeddableServer#startSecure(int)
     */
    void startSecure(int httpsPort) {
        startSecure DEFAULT_HOST, DEFAULT_PORT, httpsPort
    }

    public void startSecure (String host, int httpPort, int httpsPort) {
      start DEFAULT_HOST, httpPort
    }


    /**
     * @see EmbeddableServer#stop()
     */
    void stop() {
        grailsServer.stop()
        undeploy()
    }

    /**
     * @see EmbeddableServer#restart()
     */
    void restart() {
        stop()
        start()
    }

    /**
     * Starts the given Grails server
     */
    protected startServer(Server grailsServer) {
        eventListener?.event("ConfigureGlassfish", [grailsServer])
        if(warRun){
          grailsServer.start()
        }
    }


    /**
     * Creates the necessary SSL certificate for running in HTTPS mode
     */
    protected createSSLCertificate() {
        println 'Creating SSL Certificate...'
        if (!keystoreFile.parentFile.exists() &&
                !keystoreFile.parentFile.mkdir()) {
            def msg = "Unable to create keystore folder: " + keystoreFile.parentFile.canonicalPath
            throw new RuntimeException(msg)
        }
        String[] keytoolArgs = ["-genkey", "-alias", "localhost", "-dname",
                "CN=localhost,OU=Test,O=Test,C=US", "-keyalg", "RSA",
                "-validity", "365", "-storepass", "key", "-keystore",
                "${keystore}", "-storepass", "${keyPassword}",
                "-keypass", "${keyPassword}"]
        KeyTool.main(keytoolArgs)
        println 'Created SSL Certificate.'
    }


    private grailsResource(String path) {
        if (buildSettings.grailsHome) {
            return new FileSystemResource("${buildSettings.grailsHome}/$path")
        }
        else {
            return new ClassPathResource(path)
        }
    }

}
