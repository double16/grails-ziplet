grails-ziplet
=============

Grails plugin for ziplet JEE stream compression. The [ziplet](https://github.com/ziplet/ziplet) filter
compresses output to improve client performance and reduce network bandwidth. This is very similar to the
[Compress Plugin](http://grails.org/plugin/compress) that is deprecated.

This plugin is normally used without configuration, the defaults are intended to be
sufficient for most production applications. The configuration influences the ziplet
filter, see the [ziplet](https://github.com/ziplet/ziplet) documentation for information
on the configuration.

The supported options follow:

```
// just in case for some reason you want to disable the filter
grails.ziplet.enabled = true
grails.ziplet.debug = false
grails.ziplet.compressionThreshold = 1024
// filter's url-patterns, replaces the defaults
grails.ziplet.urlPatterns = ["/*"]
// include and exclude are mutually exclusive
grails.ziplet.includePathPatterns = []
grails.ziplet.excludePathPatterns = [".*\\.gif", ".*\\.ico", ".*\\.jpg", ".*\\.swf"] // adds to the defaults
// include and exclude are mutually exclusive
grails.ziplet.includeContentTypes = []
grails.ziplet.excludeContentTypes = ["image/png","image/gif","image/png","image/tiff"] // adds to the defaults
// include and exclude are mutually exclusive, replaces the defaults
grails.ziplet.includeUserAgentPatterns = []
grails.ziplet.excludeUserAgentPatterns = [".*MSIE 4.*"]
// replaces the defaults
grails.ziplet.noVaryHeaderPatterns = []
```

The default configuration follows. Missing values use the default from the ziplet filter, i.e. the
parameter is not included in web.xml.
```
grails.ziplet.urlPatterns = ["/*"]
grails.ziplet.excludePathPatterns = [".*/assets/.*"]
grails.ziplet.excludeContentTypes = ["image/png", "image/gif", "image/png", "image/tiff"] 
```
