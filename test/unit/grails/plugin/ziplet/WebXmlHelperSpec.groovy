package grails.plugin.ziplet

import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Specification

class WebXmlHelperSpec extends Specification {
	GrailsApplication application
	WebXmlHelper helper
	def xml
	
	def setup() {
		helper = new WebXmlHelper()
		xml = new XmlSlurper().parseText('''\
<web-app xmlns="http://java.sun.com/xml/ns/javaee" version="3.0">
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/applicationContext.xml</param-value>
	</context-param>
	<filter>
		<filter-name>urlMapping</filter-name>
		<filter-class>org.codehaus.groovy.grails.web.mapping.filter.UrlMappingsFilter</filter-class>
		<async-supported>true</async-supported>
	</filter>
	<filter-mapping>
		<filter-name>urlMapping</filter-name>
		<url-pattern>/*</url-pattern>
		<dispatcher>FORWARD</dispatcher>
		<dispatcher>REQUEST</dispatcher>
	</filter-mapping>
</web-app>
''')
	}
	
	private def applyConfig(String config) {
		application = [
			getConfig: { new ConfigSlurper().parse(config) }
		] as GrailsApplication
	}

	private GPathResult reparse() {
		String str = new StreamingMarkupBuilder().bindNode(xml).toString()
		xml = new XmlSlurper().parseText(str.toString())
	}
	
	private String param(String name) {
		String value
		xml.filter.each { filter ->
			filter.'init-param'.each { initParam ->
				if (initParam.'param-name'[0].text() == name) {
					value = initParam.'param-value'[0].text()
				}
			}
		}
		return value
	}
	
	private List urlPatterns() {
		List value = []
		xml.'filter-mapping'.each { mapping ->
			if (mapping.'filter-name'[0].text() == 'CompressingFilter') {
				value.add(mapping.'url-pattern')
			}
		}
		return value
	}
	
	void "disabled plugin"() {
		when:'config disabled plugin'
		applyConfig('''
grails.ziplet.enabled = false
''')
		then:'isEnabled returns false'		
		helper.isEnabled(application) == false
		
		when:'web.xml is processed'
		helper.updateWebXml(application, xml)
		then:'xml is not changed'
		reparse().filter.size() == 1
	}
	
	void "defaults"() {
		when:'config has no values'
		applyConfig('')
		then:'isEnabled returns true'
		helper.isEnabled(application) == true
		
		when:'web.xml is processed'
		helper.updateWebXml(application, xml)
		then:'filter is added'
		reparse().filter.size() == 2
		and:'debug is left at default'
		!param('debug')
		and:'compressionThreshold is left at default'
		!param('compressionThreshold')
		and:'includePathPatterns is left at default'
		!param('includePathPatterns')
		and:'excludePathPatterns exclude assets and static resources'
		param('excludePathPatterns') == '.*/assets/.*'
		and:'includeContentTypes is left at default'
		!param('includeContentTypes')
		and:'excludeContentTypes exclude assets and static resources'
		param('excludeContentTypes') == 'image/png,image/gif,image/jpeg,image/tiff'
		and:'includeUserAgentPatterns is left at default'
		!param('includeUserAgentPatterns')
		and:'excludeUserAgentPatterns is left at default'
		!param('excludeUserAgentPatterns')
		and:'url-pattern is set to default'
		urlPatterns() == ['/*']
	}
	
	void "debug enabled"() {
		when:'config has debug enabled'
		applyConfig('''
grails.ziplet.debug = true
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has init-param debug = true'
		reparse()
		param('debug') == 'true'
	}

	void "compressionThreshold set"() {
		when:'config has compressionThreshold set'
		applyConfig('''
grails.ziplet.compressionThreshold = 4096
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has init-param compressionThreshold = 4096'
		reparse()
		param('compressionThreshold') == '4096'
	}
	
	void "urlPatterns set to list"() {
		when:'config has urlPatterns set to list'
		applyConfig('''
grails.ziplet.urlPatterns = [ '/controller/method1', '/controller/method2' ]
''')
		helper.updateWebXml(application, xml)
		then:'web.xml maps filter to all patterns'
		reparse()
		urlPatterns() == [ '/controller/method1', '/controller/method2' ]
	}

	void "urlPatterns set to string"() {
		when:'config has urlPatterns set to list'
		applyConfig('''
grails.ziplet.urlPatterns = '/controller/method1'
''')
		helper.updateWebXml(application, xml)
		then:'web.xml maps filter to all patterns'
		reparse()
		urlPatterns() == [ '/controller/method1' ]
	}

	void "includePathPatterns set"() {
		when:'config has includePathPatterns set'
		applyConfig('''
grails.ziplet.includePathPatterns = ['/path/1', '/path/2']
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has includePathPatterns'
		reparse()
		param('includePathPatterns') == '/path/1,/path/2'
		and:'web.xml does not have excludePathPatterns'
		!param('excludePathPatterns')
	}

	void "excludePathPatterns set"() {
		when:'config has excludePathPatterns set'
		applyConfig('''
grails.ziplet.excludePathPatterns = ['/path/3', '/path/4']
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has excludePathPatterns'
		reparse()
		param('excludePathPatterns') == '.*/assets/.*,/path/3,/path/4'
		and:'web.xml does not have includePathPatterns'
		!param('includePathPatterns')
	}

	void "includePathPatterns and excludePathPatterns not allowed"() {
		when:'config has includePathPatterns and excludePathPatterns set'
		applyConfig('''
grails.ziplet.includePathPatterns = ['/path/1', '/path/2']
grails.ziplet.excludePathPatterns = ['/path/3', '/path/4']
''')
		helper.updateWebXml(application, xml)
		then:'plugin fails'
		thrown(Exception)
	}

	void "includeContentTypes set"() {
		when:'config has includeContentTypes set'
		applyConfig('''
grails.ziplet.includeContentTypes = ['image/bmp', 'image/raw']
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has includeContentTypes'
		reparse()
		param('includeContentTypes') == 'image/bmp,image/raw'
		and:'web.xml does not have excludeContentTypes'
		!param('excludeContentTypes')
	}

	void "excludeContentTypes set"() {
		when:'config has excludeContentTypes set'
		applyConfig('''
grails.ziplet.excludeContentTypes = ['image/bmp', 'image/raw']
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has excludeContentTypes'
		reparse()
		param('excludeContentTypes') == 'image/png,image/gif,image/jpeg,image/tiff,image/bmp,image/raw'
		and:'web.xml does not have includeContentTypes'
		!param('includeContentTypes')
	}

	void "includeContentTypes and excludeContentTypes not allowed"() {
		when:'config has includeContentTypes and excludeContentTypes set'
		applyConfig('''
grails.ziplet.includeContentTypes = ['image/bmp', 'image/raw']
grails.ziplet.excludeContentTypes = ['image/png', 'image/gif']
''')
		helper.updateWebXml(application, xml)
		then:'plugin fails'
		thrown(Exception)
	}
	
	void "includeUserAgentPatterns set"() {
		when:'config has includeUserAgentPatterns set'
		applyConfig('''
grails.ziplet.includeUserAgentPatterns = ['.*MSIE 4.*', '.*Gecko.*']
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has includeUserAgentPatterns'
		reparse()
		param('includeUserAgentPatterns') == '.*MSIE 4.*,.*Gecko.*'
		and:'web.xml does not have excludeUserAgentPatterns'
		!param('excludeUserAgentPatterns')
	}

	void "excludeUserAgentPatterns set"() {
		when:'config has excludeUserAgentPatterns set'
		applyConfig('''
grails.ziplet.excludeUserAgentPatterns = ['.*MSIE 4.*', '.*Gecko.*']
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has excludeUserAgentPatterns'
		reparse()
		param('excludeUserAgentPatterns') == '.*MSIE 4.*,.*Gecko.*'
		and:'web.xml does not have includeUserAgentPatterns'
		!param('includeUserAgentPatterns')
	}

	void "includeUserAgentPatterns and excludeUserAgentPatterns not allowed"() {
		when:'config has includeUserAgentPatterns and excludeUserAgentPatterns set'
		applyConfig('''
grails.ziplet.includeUserAgentPatterns = ['.*MSIE 5.*', '.*WebKit.*']
grails.ziplet.excludeUserAgentPatterns = ['.*MSIE 4.*', '.*Gecko.*']
''')
		helper.updateWebXml(application, xml)
		then:'plugin fails'
		thrown(Exception)
	}

	void "noVaryHeaderPatterns set"() {
		when:'config has noVaryHeaderPatterns set'
		applyConfig('''
grails.ziplet.noVaryHeaderPatterns = ['.*MSIE 4.*', '.*Gecko.*']
''')
		helper.updateWebXml(application, xml)
		then:'web.xml has noVaryHeaderPatterns'
		reparse()
		param('noVaryHeaderPatterns') == '.*MSIE 4.*,.*Gecko.*'
	}
}
