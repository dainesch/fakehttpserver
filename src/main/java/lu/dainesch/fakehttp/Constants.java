package lu.dainesch.fakehttp;

import java.util.regex.Pattern;


public class Constants {
    
    private Constants(){}
    
    public static final Pattern VALID_PATH = Pattern.compile("(^/[/.a-zA-Z0-9-{}]+(/\\*)*$)|(^/\\*$)");
    
    public static final String DEFAULT_RESPONSE = "No filter defined";
    
    public static final String MAPPING_ALL = "/*";
    
    public static final String SCENEX = "scene.x";
    public static final String SCENEY = "scene.y";
    public static final String SCENEW = "scene.w";
    public static final String SCENEH = "scene.h";
    
    public static final String SERVERSTARTED = "server.started";
    public static final String SERVERPORT = "server.port";
    public static final String PREVIEWSIZE = "preview.size";
    
    public static final String NO_BODY="[NO BODY]";
    
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_TYPE = "Content-Type";
    
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String HOST = "host";
    public static final String GZIP = "gzip";
    
}
