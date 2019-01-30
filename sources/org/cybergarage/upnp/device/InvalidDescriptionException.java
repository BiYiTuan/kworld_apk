package org.cybergarage.upnp.device;

import java.io.File;

public class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException(Exception exception) {
        super(exception.getMessage());
    }

    public InvalidDescriptionException(String str) {
        super(str);
    }

    public InvalidDescriptionException(String str, File file) {
        super(str + " (" + file.toString() + ")");
    }
}
