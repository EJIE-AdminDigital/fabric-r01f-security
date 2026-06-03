package r01f.model.security.login.userpassword.factors;

public interface LoginAuthFactorDispatchResponse {
	
	public boolean wasDispatched() ;

	public LoginAuthFactorDispatchResponseNOK asNOK();
	public LoginAuthFactorDispatchResponseOK asOK();
}
