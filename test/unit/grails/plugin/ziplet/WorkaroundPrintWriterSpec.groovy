package grails.plugin.ziplet

import spock.lang.Specification

class WorkaroundPrintWriterSpec extends Specification {
    WorkaroundPrintWriter printWriter
    char[] data

    def setup() {
        printWriter = new WorkaroundPrintWriter(new PrintWriter(new StringWriter()))
        data = new char[3]
        data[0] = 'a'
        data[1] = 'b'
        data[2] = 'c'
    }

    def noOutput() {
        expect:
        !printWriter.outputSeen
    }

    def writeCharArray() {
        when: printWriter.write(data)
        then: printWriter.outputSeen
    }

    def writeCharArrayRange() {
        when: printWriter.write(data, 0, 3)
        then: printWriter.outputSeen
    }

    def writeString() {
        when: printWriter.write("abc")
        then: printWriter.outputSeen
    }

    def writeStringRange() {
        when: printWriter.write("abc", 0, 3)
        then: printWriter.outputSeen
    }

    def writeChar() {
        when: printWriter.write(data[0])
        then: printWriter.outputSeen
    }
}
