package grails.plugin.ziplet

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper

class WorkaroundServletOutputStream extends ServletOutputStream {
    @Delegate final ServletOutputStream underlying
    boolean outputSeen = false

    WorkaroundServletOutputStream(ServletOutputStream underlying) {
        this.underlying = underlying
    }

    @Override
    void write(int b) throws IOException {
        outputSeen = true
        underlying.write(b)
    }

    @Override
    void write(byte[] b) throws IOException {
        outputSeen = true
        underlying.write(b)
    }

    @Override
    void write(byte[] b, int off, int len) throws IOException {
        outputSeen = true
        underlying.write(b, off, len)
    }
}

class WorkaroundPrintWriter extends PrintWriter {
    boolean outputSeen = false

    WorkaroundPrintWriter(PrintWriter underlying) {
        super(underlying)
    }

    @Override
    void write(char[] buf, int off, int len) {
        outputSeen = true
        super.write(buf, off, len)
    }

    @Override
    void write(char[] buf) {
        outputSeen = true
        super.write(buf)
    }

    @Override
    void write(String s, int off, int len) {
        outputSeen = true
        super.write(s, off, len)
    }

    @Override
    void write(String s) {
        outputSeen = true
        super.write(s)
    }

    @Override
    void write(int c) {
        outputSeen = true
        super.write(c)
    }

}

class WorkaroundServletResponse extends HttpServletResponseWrapper {
    private WorkaroundServletOutputStream servletOutputStream
    private WorkaroundPrintWriter printWriter

    WorkaroundServletResponse(HttpServletResponse response) {
        super(response)
    }

    @Override
    boolean isCommitted() {
        return super.isCommitted() || servletOutputStream?.outputSeen || printWriter?.outputSeen
    }

    @Override
    ServletOutputStream getOutputStream() throws IOException {
        if (!servletOutputStream) {
            servletOutputStream = new WorkaroundServletOutputStream(super.getOutputStream())
        }
        servletOutputStream
    }

    @Override
    PrintWriter getWriter() throws IOException {
        if (!printWriter) {
            printWriter = new WorkaroundPrintWriter(super.getWriter())
        }
        printWriter
    }
}
