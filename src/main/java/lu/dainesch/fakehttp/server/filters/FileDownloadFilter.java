package lu.dainesch.fakehttp.server.filters;

import java.nio.file.Path;


public class FileDownloadFilter extends PathFilter<FileDownloadServlet>{
    
    private Path basePath;
    

    public FileDownloadFilter() {
        super(FilterType.FileDownload);
    }

    @Override
    void initServlet() {
        this.servlet = new FileDownloadServlet(this);
    }

    public synchronized Path getBasePath() {
        return basePath;
    }

    public synchronized void setBasePath(Path basePath) {
        this.basePath = basePath;
    }
    
    
    
}
