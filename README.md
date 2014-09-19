grails-ziplet
=============

Grails plugin for ziplet JEE stream compression. The [ziplet](https://github.com/ziplet/ziplet) filter
compresses output to improve client performance and reduce network bandwidth.

At the moment none of the configuration options of ziplet are supported. The filter is applied to all URLs
with default values. This should make sense for most production environments.
