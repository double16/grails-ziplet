grails.project.work.dir = 'target'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	dependencies {
		compile('com.github.ziplet:ziplet:2.0.0') {
			excludes 'slf4j-nop'
		}
	}

	plugins {
		build ':release:3.1.1', ':rest-client-builder:2.1.1', {
			export = false
		}

		provided (':codenarc:0.23') {
			export = false
		}
        provided (':code-coverage:2.0.3-3')
	}
}

codenarc.extraIncludeDirs=['']

coverage {
    enabledByDefault = false
    xml = true
}
