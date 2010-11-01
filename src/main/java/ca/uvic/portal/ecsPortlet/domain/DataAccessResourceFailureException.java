package ca.uvic.portal.ecsPortlet.domain;

public class DataAccessResourceFailureException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public DataAccessResourceFailureException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataAccessResourceFailureException(Throwable cause) {
        super(cause);
    }
    
}
