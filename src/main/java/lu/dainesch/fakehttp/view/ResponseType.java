package lu.dainesch.fakehttp.view;


public enum ResponseType {

    HTML("text/html", "html"),
    JAVASCRIPT("text/javascript", "js"),
    CSS("text/css", "css"),
    XML("application/xml", "xml"),
    JSON("application/json", "json"),
    TEXT("text/plain", "text"),
    CUSTOM(null, "?");

    private final String value;
    private final String display;

    ResponseType(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

    public static ResponseType fromType(String value) {
        if (value==null) {
            return ResponseType.CUSTOM;
        }
        for (ResponseType t : values()) {
            if (t.getValue()!=null && value.toLowerCase().contains(t.getValue().toLowerCase())) {
                return t;
            }
        }
        return ResponseType.CUSTOM;
    }

}
