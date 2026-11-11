package r01f.security.login.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.jwt.JWTDecoded;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.login.Login;
import r01f.model.security.login.entry.LoginEntry;


/**
 * To handle the result of ApiKeyLoginStep.
 * @param <P>
 * @param <L>
 */

public  interface ApiKeyJWTAuthenticationStepResult<P extends IsSecurityPrincipal> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
		public boolean hasFailed();
		public boolean hasSucceeded();
		public ApiKeyJWTAuthenticationStepResultOK<P> asOK();
		public ApiKeyJWTAuthenticationStepResultError<P> asError();
/////////////////////////////////////////////////////////////////////////////////////////
//	BASE
/////////////////////////////////////////////////////////////////////////////////////////		
		@RequiredArgsConstructor
		@Accessors(prefix="_")
		public static abstract class ApiKeyJWTAuthenticationStepResultBase<P extends IsSecurityPrincipal>
		    			  implements ApiKeyJWTAuthenticationStepResult<P> {

				@Getter final JWTDecoded _jwtDecoded;
				@Getter final P _principal;
				@Getter final Login _apiKeyLogin;
				@Getter final LoginEntry _apiKeyLoginEntry;

				@Override
				public ApiKeyJWTAuthenticationStepResultOK<P> asOK() {
					if (this instanceof ApiKeyJWTAuthenticationStepResultOK) return (ApiKeyJWTAuthenticationStepResultOK<P>)this;
					throw  new IllegalStateException("Cannot cast an OK resul to ERROR");
				}

				@Override
				public ApiKeyJWTAuthenticationStepResultError<P> asError() {
					if (this instanceof ApiKeyJWTAuthenticationStepResultError) return (ApiKeyJWTAuthenticationStepResultError<P>)this;
					throw new IllegalStateException("Cannot cast an ERRor resul to OK");
				}
		}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
		@Accessors(prefix="_")
		public static class ApiKeyJWTAuthenticationStepResultOK<P extends IsSecurityPrincipal>
					extends ApiKeyJWTAuthenticationStepResultBase<P>
		         implements ApiKeyJWTAuthenticationStepResult<P> {

			public ApiKeyJWTAuthenticationStepResultOK(final JWTDecoded jwtDecoded,
					                                   final P principal,
					                                   final Login apiKeyLogin,final LoginEntry apiKeyLoginEntry) {
				super(jwtDecoded,
					  principal, 
					  apiKeyLogin,apiKeyLoginEntry);
			}
			@Override
			public boolean hasFailed() {
				return false;
			}
			@Override
			public boolean hasSucceeded() {
				return true;
			}
		}
		@Accessors(prefix="_")
		public static class ApiKeyJWTAuthenticationStepResultError<P extends IsSecurityPrincipal>
				    extends ApiKeyJWTAuthenticationStepResultBase<P>
		         implements ApiKeyJWTAuthenticationStepResult<P> {

			@Getter ApiKeyJWTAuthenticationError _error;
			@Getter String _detail;

			public ApiKeyJWTAuthenticationStepResultError(final JWTDecoded jwtDecoded,
			                                              final P principal,
			                                              final Login apiKeyLogin,final LoginEntry apiKeyLoginEntry) {
	              super(jwtDecoded,
	            		principal, 
	            		apiKeyLogin,apiKeyLoginEntry);
			}
			public ApiKeyJWTAuthenticationStepResultError<P> with(final ApiKeyJWTAuthenticationError error,
				                                            	  final String detail) {
				_error =  error;
				_detail = detail;
				return this;
			}
			@Override
			public boolean hasFailed() {
				return true;
			}
			@Override
			public boolean hasSucceeded() {
				return false;
			}
		}

}


