package grails.plugin.ziplet

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 * This filter provides work arounds for issues caused by the Ziplet CompresssingFilter and assumptions made by
 * Grails.
 *
 * The CompressingFilter buffers a small amount of initial data in order to determine if compression is needed. Grails
 * REST support uses HttpServletResponse.isCommitted() to determine if REST handled the request and if not looks for a
 * view to render. If the response is small, isCommitted() returns false. This filter will check if data was written
 * and return true for isCommitted(). IMHO this behavior of Grails is incorrect. The servlet container could reasonably
 * return false for isCommitted() if buffering is implemented by the server.
 */
class GrailsCompressingFilter implements Filter {
    @Override
    void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, new WorkaroundServletResponse(servletResponse))
    }

    @Override
    void destroy() {
        // nothing to do
    }
}
