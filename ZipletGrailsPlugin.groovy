
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.plugin.ziplet.WebXmlHelper

class ZipletGrailsPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [ ]
	def loadAfter = ['controllers']

    def title = "HTTP Compression Plugin"
    def author = "Patrick Double"
    def authorEmail = "pat@patdouble.com"
    def description = '''\
Integrates the ziplet compression filter into Grails for controller content. This plugin replaces the deprecated compress
plugin that used the same filter under a different name. Use the asset-pipeline or resources plugin for
compressing static resources.
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/double16/grails-ziplet/blob/master/README.md"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GitHub", url: "https://github.com/double16/grails-ziplet/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/double16/grails-ziplet" ]

    def doWithWebDescriptor = { xml ->
		new WebXmlHelper().updateWebXml(application, xml)
    }
}
