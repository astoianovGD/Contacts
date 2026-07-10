package contacts.Exceptions;

/**
 * Exception thrown when an invalid index is provided.
 */
public final class BadIndexException extends Exception {

    /**
     * Gets the exception message.
     *
     * @return the detailed error message string
     */
    @Override
    public String getMessage() {
        return "Bad index!";
    }
}
