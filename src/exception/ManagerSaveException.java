package exception;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final String message, IOException e) {
        super(message);
        e.printStackTrace();
    }

    public ManagerSaveException(final String message) {
        super(message);
    }

}
