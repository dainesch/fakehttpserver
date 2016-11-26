package lu.dainesch.fakehttp.view;

import java.util.ResourceBundle;

public enum ResponseCode {

    OK(200),
    NO_CONTENT(204),
    MOVED_PERM(301),
    FOUND(302),
    SEE_OTHER(303),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    SERVER_ERROR(500);

    private static final ResourceBundle CODE_BUNDLE = ResourceBundle.getBundle("HttpCodes");

    private final int code;

    private ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return CODE_BUNDLE.getString(getCode() + ".key");
    }

    public String getDesc() {
        return CODE_BUNDLE.getString(getCode() + ".desc");
    }

    public static ResponseCode fromCode(int code) {
        for (ResponseCode r : values()) {
            if (r.getCode() == code) {
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

}
