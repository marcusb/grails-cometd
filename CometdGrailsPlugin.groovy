import org.cometd.Bayeux

class CometdGrailsPlugin {
    def version = 0.1
    def dependsOn = [:]

    def author = "Mingfai Ma"
    def authorEmail = "mingfai.ma@gmail.com"
    def title = "Enable your Grails application with Comet functions"
    def description = '''\\
Cometd is a scalable HTTP-based event routing bus that uses a Ajax Push technology pattern known as Comet. The term
'Comet' was coined by Alex Russell in his post Comet: Low Latency Data for the Browser.

This plugin provides a SpringCometdServlet and SpringBayeux on top of the Cometd release, and pre-configure the servlet,
bayeux instance, and provide all demo programs from the cometd-jetty release.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Cometd+Plugin"

    static namespace =System.getProperty("cometd.namespace")?:'cometd'

    def doWithWebDescriptor = { xml ->
      xml.'servlet' + {
        servlet{
          'servlet-name'('cometd')
          'servlet-class'('org.grails.plugins.cometd.SpringCometdServlet')
          'load-on-startup'(2)
        }
      }
      xml.'servlet-mapping' + {
       'servlet-mapping'{
          'servlet-name'('cometd')
          'url-pattern'("*.${namespace}")
          'url-pattern'("/${namespace}")
          'url-pattern'("/${namespace}/")
          'url-pattern'("/${namespace}/*")
          'url-pattern'("/plugins/cometd-${version}/${namespace}/*")
        }
      }
    }

    def doWithSpring = {
      bayeux(org.grails.plugins.cometd.SpringBayeux){ bean ->
        bean.initMethod = 'initialize'
        timeout = 180000
        interval = 0
        maxInterval = 10000
        multiFrameInterval = 1500
        //requestAvailable = false
        directDeliver = true
        JSONCommented = false
        //refsThreshold = 1000
        logLevel = 1
      }
    }


    def doWithApplicationContext = { applicationContext ->
        def bayeux = applicationContext.getBean('bayeux')
        applicationContext.servletContext.setAttribute(Bayeux.DOJOX_COMETD_BAYEUX, bayeux)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
