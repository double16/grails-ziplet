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
		build ':release:3.0.1', ':rest-client-builder:2.0.3', {
			export = false
		}

		provided (':codenarc:0.22') {
			export = false
		}
	}
}

codenarc.extraIncludeDirs=['']
