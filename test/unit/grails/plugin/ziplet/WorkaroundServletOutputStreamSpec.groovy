package grails.plugin.ziplet

import org.springframework.mock.web.DelegatingServletOutputStream
import spock.lang.Specification

import javax.servlet.ServletOutputStream

class WorkaroundServletOutputStreamSpec extends Specification {
    WorkaroundServletOutputStream outputStream
    byte[] data

    def setup() {
        outputStream = new WorkaroundServletOutputStream(new DelegatingServletOutputStream(new ByteArrayOutputStream()))
        data = new byte[3]
        data[0] = 66
        data[1] = 67
        data[2] = 68
    }

    def noOutput() {
        expect:
        !outputStream.outputSeen
    }

    def writeByte() {
        when: outputStream.write(data[0])
        then: outputStream.outputSeen
    }

    def writeByteArray() {
        when: outputStream.write(data)
        then: outputStream.outputSeen
    }

    def writeByteArrayRange() {
        when: outputStream.write(data, 0, 3)
        then: outputStream.outputSeen
    }
}
