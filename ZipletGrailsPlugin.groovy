class ZipletGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [ ]

    def title = "Ziplet Plugin" // Headline display name of the plugin
    def author = "Patrick Double"
    def authorEmail = "pat@patdouble.com"
    def description = '''\
Integrates the ziplet compression filter into grails for controller content. Use the asset-pipeline or resources plugin for
compressing static resources.
'''

    // URL to the plugin's documentation
    def documentation = "http://github.com/double16/grails-ziplet/blob/master/README.md"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "MIT"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "GitHub", url: "https://github.com/double16/grails-ziplet/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/double16/grails-ziplet" ]

    def doWithWebDescriptor = { xml ->
        def mappingElement = xml.'filter-mapping'

        def lastMapping = mappingElement[mappingElement.size() - 1]
        lastMapping + {
            'filter' {
                'filter-name'("CompressingFilter")
                'filter-class'("com.github.ziplet.filter.compression.CompressingFilter")
                'init-param' {
                  'param-name'("excludePathPatterns")
                  'param-value'(".*/assets/.*,.*/static/.*")
                }
            }
            'filter-mapping' {
                'filter-name'("CompressingFilter")
                'url-pattern'("/*")
            }
        }      
    }
}
