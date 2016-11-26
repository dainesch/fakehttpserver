package lu.dainesch.fakehttp.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


class WrappedRequest extends HttpServletRequestWrapper {

    private final byte[] all;
    private ByteArrayServletInputStream stream = null;

    public WrappedRequest(HttpServletRequest request) throws IOException {
        super(request);
        if (request.getContentLength() > 0) {
            try (InputStream in = request.getInputStream()) {
                all = new byte[request.getContentLength()];
                in.read(all);
                stream = new ByteArrayServletInputStream(all);
            }

        } else {
            all = null;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (all == null) {
            return super.getInputStream();
        } else {
            return stream;
        }
    }

    public byte[] getInput() {
        return all;
    }

    class ByteArrayServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream input;

        public ByteArrayServletInputStream(byte[] input) {
            this.input = new ByteArrayInputStream(input);
        }

        @Override
        public boolean isFinished() {
            return input.available() <= 0;
        }

        @Override
        public boolean isReady() {
            return input.available() > 0;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return input.read();
        }

    }

}
