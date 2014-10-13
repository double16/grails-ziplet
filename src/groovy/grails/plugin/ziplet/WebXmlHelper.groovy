package grails.plugin.ziplet

import groovy.util.slurpersupport.GPathResult
import org.codehaus.groovy.grails.commons.GrailsApplication

class WebXmlHelper {
	void updateWebXml(GrailsApplication application, def xml) {
		if (!isEnabled(application)) {
			return
		}
		
		def config = application.config.grails.ziplet
		
		def includePathPatterns = config.includePathPatterns
		def excludePathPatterns = ['/assets/.*']
		if (config.excludePathPatterns) {
			if (includePathPatterns) {
				throw new IllegalArgumentException('Cannot specify both excludePathPatterns and includePathPatterns')
			}
			excludePathPatterns.addAll(config.excludePathPatterns)
		}
		
		def includeContentTypes = config.includeContentTypes
		def excludeContentTypes = ['image/png','image/gif','image/jpeg','image/tiff']
		if (config.excludeContentTypes) {
			if (includeContentTypes) {
				throw new IllegalArgumentException('Cannot specify both excludeContentTypes and includeContentTypes')
			}
			excludeContentTypes.addAll(config.excludeContentTypes)
		}
		
		def includeUserAgentPatterns = config.includeUserAgentPatterns
		def excludeUserAgentPatterns = []
		if (config.excludeUserAgentPatterns) {
			if (includeUserAgentPatterns) {
				throw new IllegalArgumentException('Cannot specify both excludeUserAgentPatterns and includeUserAgentPatterns')
			}
			excludeUserAgentPatterns.addAll(config.excludeUserAgentPatterns)
		}
		
		def contextParam = xml.'context-param'
		contextParam[contextParam.size() - 1] + {
			filter {
				'filter-name'('CompressingFilter')
				'filter-class'(com.github.ziplet.filter.compression.CompressingFilter.name)
				if (config.debug) {
					'init-param' {
					  'param-name'('debug')
					  'param-value'('true')
					}					
				}
				if (config.compressionThreshold) {
					'init-param' {
					  'param-name'('compressionThreshold')
					  'param-value'(config.compressionThreshold)
					}					
				}
				if (includePathPatterns) {
					'init-param' {
					  'param-name'('includePathPatterns')
					  'param-value'([includePathPatterns].flatten().findAll().join(','))
					}					
				} else if (excludePathPatterns) {
					'init-param' {
					  'param-name'('excludePathPatterns')
					  'param-value'([excludePathPatterns].flatten().findAll().join(','))
					}					
				}
				if (includeContentTypes) {
					'init-param' {
					  'param-name'('includeContentTypes')
					  'param-value'([includeContentTypes].flatten().findAll().join(','))
					}					
				} else if (excludeContentTypes) {
					'init-param' {
					  'param-name'('excludeContentTypes')
					  'param-value'([excludeContentTypes].flatten().findAll().join(','))
					}					
				}
				if (includeUserAgentPatterns) {
					'init-param' {
					  'param-name'('includeUserAgentPatterns')
					  'param-value'([includeUserAgentPatterns].flatten().findAll().join(','))
					}					
				} else if (excludeUserAgentPatterns) {
					'init-param' {
					  'param-name'('excludeUserAgentPatterns')
					  'param-value'([excludeUserAgentPatterns].flatten().findAll().join(','))
					}					
				}
				if (config.noVaryHeaderPatterns) {
					'init-param' {
					  'param-name'('noVaryHeaderPatterns')
					  'param-value'([config.noVaryHeaderPatterns].flatten().findAll().join(','))
					}					
				}
			}
		}

		def urlPatterns = [config.urlPatterns].flatten().findAll()
		if (!urlPatterns) {
			urlPatterns = ['/*']
		}
		def filter = xml.'filter' // this will put the compression filter near the first to be processed
		urlPatterns.each { pattern ->
			filter[filter.size() - 1] + {
					'filter-mapping' {
						'filter-name'('CompressingFilter')
						'url-pattern'(pattern)
					}
			}				
		}
	}

	private boolean isEnabled(GrailsApplication application) {
		def enabled = application.config.grails.ziplet.enabled
		enabled == null || enabled != false
	}
}

