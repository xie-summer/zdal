
package com.alipay.zdal.common.exception.runtime;

public class ZdalRunTimeException extends RuntimeException {

 
    /**
	 * 
	 */
	private static final long serialVersionUID = -4830011759435824330L;

	public ZdalRunTimeException(String arg) {
        super(arg);
    }

    public ZdalRunTimeException() {
        super();
    }

    public ZdalRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZdalRunTimeException(Throwable throwable) {
        super(throwable);
    }
}
