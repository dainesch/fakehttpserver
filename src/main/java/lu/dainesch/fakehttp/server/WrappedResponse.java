package lu.dainesch.fakehttp.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


class WrappedResponse extends HttpServletResponseWrapper {

    private final ByteArrayServletOutputStream out;

    public WrappedResponse(HttpServletResponse response) {
        super(response);
        out = new ByteArrayServletOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return out;
    }

    public void writeToResponse() throws IOException {
        super.getOutputStream().write(out.getOutput());
    }

    public byte[] getOutput() {
        return out.getOutput();
    }

    class ByteArrayServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream output;

        public ByteArrayServletOutputStream() {
            this.output = new ByteArrayOutputStream();
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) throws IOException {
            output.write(b);
        }

        public byte[] getOutput() {
            return output.toByteArray();
        }

    }

}
