package r01f.security.provider;

public class SecurityProviderException
	 extends RuntimeException  {
	private static final long serialVersionUID = 1896910308664494325L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityProviderException(final String msg) {
		super(msg);
	}
	public SecurityProviderException(final String msg,
								  final Throwable other) {
		super(msg,other);
	}
}
