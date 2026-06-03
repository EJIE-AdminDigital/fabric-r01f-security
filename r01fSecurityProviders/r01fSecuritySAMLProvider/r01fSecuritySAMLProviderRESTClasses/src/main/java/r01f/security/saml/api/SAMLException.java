package r01f.security.saml.api;

public class SAMLException 
	 extends RuntimeException {
	
	private static final long serialVersionUID = 6515385766646019139L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public SAMLException(final String message) {
		super(message);
	}
	public SAMLException(final String message,
						 final Throwable cause) {
		super(message, cause);
	}
}
