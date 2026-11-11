package r01f.security.authn.handler;

public class AuthenticationProviderException
	 extends RuntimeException  {
	private static final long serialVersionUID = 1896910308664494325L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthenticationProviderException(final String msg) {
		super(msg);
	}
	public AuthenticationProviderException(final String msg,
								  		   final Throwable other) {
		super(msg,other);
	}
}
