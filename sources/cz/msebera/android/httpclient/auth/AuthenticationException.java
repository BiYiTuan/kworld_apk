package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.ProtocolException;
import cz.msebera.android.httpclient.annotation.Immutable;

@Immutable
public class AuthenticationException extends ProtocolException {
    private static final long serialVersionUID = -6794031905674764776L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
