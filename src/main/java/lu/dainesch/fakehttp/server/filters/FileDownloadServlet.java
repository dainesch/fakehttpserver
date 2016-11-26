
package lu.dainesch.fakehttp.server.filters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.glassfish.grizzly.http.util.MimeType;


public class FileDownloadServlet extends PathServlet {

    private final FileDownloadFilter filter;

    public FileDownloadServlet(FileDownloadFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Path file = checksAndHeaders(req, resp);
        if (file != null) {

            byte[] buff = new byte[8 * 1024];
            try (InputStream in = Files.newInputStream(file);
                    OutputStream out = resp.getOutputStream()) {
                int read;
                while ((read = in.read(buff)) != -1) {
                    out.write(buff, 0, read);
                }
            }
        }

    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checksAndHeaders(req, resp);
    }

    private Path checksAndHeaders(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String filePath = req.getPathInfo();
        if (filePath == null || filePath.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } else {
            // remove slash
            filePath = filePath.substring(1);
        }
        // resolve file and basic checks
        Path file = filter.getBasePath().resolve(filePath).normalize();
        if (!file.startsWith(filter.getBasePath())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } else if (Files.notExists(file) || !Files.isReadable(file) || !Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        // modified
        long mod = Files.getLastModifiedTime(file).toMillis();
        String modSince = req.getHeader("If-Modified-Since");
        try {
            if (modSince != null && mod <= Long.parseLong(modSince)) {
                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return null;
            }
        } catch (NumberFormatException ex) {
            // ignore
        }
        
        // content type
        String contentType = Files.probeContentType(file);
        if (contentType == null) {
            contentType = MimeType.getByFilename(file.getFileName().toString());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        }

        long size = Files.size(file);
        resp.setContentLengthLong(size);
        resp.setContentType(contentType);
        resp.setHeader("last-modified", String.valueOf(mod));

        return file;
    }

}
