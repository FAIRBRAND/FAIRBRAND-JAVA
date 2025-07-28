package ca.coltip.security.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;

public class ModifiedRequestWrapper extends HttpServletRequestWrapper {

    private final String modifiedBody;

    public ModifiedRequestWrapper(HttpServletRequest request, String modifiedBody) {
        super(request);
        this.modifiedBody = modifiedBody;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final InputStream is = new java.io.ByteArrayInputStream(modifiedBody.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return is.read();
            }

            @Override
            public boolean isFinished() {
                try {
                    return is.available() == 0;
                } catch (IOException e) {
                    return true;
                }
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // no-op
            }
        };
    }
}
