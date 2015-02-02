import grails.plugin.ziplet.WebXmlHelper

class ZipletGrailsPlugin {
	def version = "0.4"
	def grailsVersion = "2.0 > *"
	def loadAfter = ['controllers']

	def title = "HTTP Compression Plugin"
	def author = "Patrick Double"
	def authorEmail = "pat@patdouble.com"
	def description = '''\
Integrates the ziplet compression filter into Grails for controller content. This plugin replaces the deprecated compress
plugin that used the same filter under a different name. Use the asset-pipeline or resources plugin for
compressing static resources.
'''
	def documentation = "https://github.com/double16/grails-ziplet/blob/master/README.md"
	def license = "APACHE"
	def issueManagement = [ system: "GitHub", url: "https://github.com/double16/grails-ziplet/issues" ]
	def scm = [ url: "https://github.com/double16/grails-ziplet" ]

	def doWithWebDescriptor = { xml ->
		new WebXmlHelper().updateWebXml(application, xml)
	}
}
