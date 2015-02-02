package grails.plugin.ziplet

import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse

class WorkaroundServletResponseSpec extends Specification {
    HttpServletResponse underlying
    WorkaroundServletResponse response

    def setup() {
        underlying = new MockHttpServletResponse()
        response = new WorkaroundServletResponse(underlying)
    }

    def "isCommitted is false with no output"() {
        expect:
        !response.isCommitted()
    }

    def "isCommitted is false when output stream is retrieved but not written"() {
        when: response.getOutputStream()
        then: !response.isCommitted()
    }

    def "isCommitted is false when writer is retrieved but not written"() {
        when: response.getWriter()
        then: !response.isCommitted()
    }

    def "isCommitted is true when output stream is written"() {
        when: response.getOutputStream().write((byte) 65)
        then: response.isCommitted()
    }

    def "isCommitted is true when writer is written"() {
        when: response.getWriter().write('a')
        then: response.isCommitted()
    }

    def 'isCommitted is true when underlying response isCommitted is true'() {
        given: 'the writer'
        def writer = underlying.getWriter()
        when: 'a string is written and flushed'
        writer.write("abc")
        writer.flush()
        then: 'isCommitted returns true'
        response.isCommitted()
    }

    def 'getOutputStream returns the same object for multiple calls'() {
        when:'getOutputStream is called twice'
        def first = response.getOutputStream()
        def second = response.getOutputStream()
        then:'results are identical'
        first.is(second)
    }

    def 'getWriter returns the same object for multiple calls'() {
        when:'getWriter is called twice'
        def first = response.getWriter()
        def second = response.getWriter()
        then:'results are identical'
        first.is(second)
    }
}
