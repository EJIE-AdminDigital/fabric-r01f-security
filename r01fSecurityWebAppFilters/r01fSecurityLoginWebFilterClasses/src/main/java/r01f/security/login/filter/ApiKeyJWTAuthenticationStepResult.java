package r01f.security.login.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.jwt.JWTDecoded;
import r01f.model.security.login.Login;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.user.User;


/**
 * To handle the result of ApiKeyLoginStep.
 * @param <U>
 * @param <L>
 */

public  interface ApiKeyJWTAuthenticationStepResult<U extends User,L extends Login,
                                                    E extends UserLoginEntry>{

//////////////////////////////////////////////////////////////////////////////////////////
// INTERFACE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
		public boolean hasFailed();
		public boolean hasSucess();
		public ApiKeyJWTAuthenticationStepResultOK<U,L,E> asOK();
		public ApiKeyJWTAuthenticationStepResultError<U,L,E> asError();
//////////////////////////////////////////////////////////////////////////////////////////
// BASE CLASE
/////////////////////////////////////////////////////////////////////////////////////////

		@RequiredArgsConstructor
		@Accessors(prefix="_")
		public static abstract class ApiKeyJWTAuthenticationStepResultBase<U extends User,L extends Login,
		                                                                   E extends UserLoginEntry>
		    implements ApiKeyJWTAuthenticationStepResult<U,L,E> {

				@Getter final JWTDecoded _jwtDecoded;
				@Getter final U _user;
				@Getter final L _apiKeyLogin;
				@Getter final E _apiKeyLoginEntry;


				@Override
				public ApiKeyJWTAuthenticationStepResultOK<U, L, E> asOK() {
					if ( this instanceof ApiKeyJWTAuthenticationStepResultOK )
						return (ApiKeyJWTAuthenticationStepResultOK<U, L, E>) this;
					 throw  new IllegalStateException("Cannot cast an OK resul to ERROR");
				}

				@Override
				public ApiKeyJWTAuthenticationStepResultError<U,L,E> asError() {
					if ( this instanceof ApiKeyJWTAuthenticationStepResultError )
						return (ApiKeyJWTAuthenticationStepResultError<U,L,E>)this;
					 throw  new IllegalStateException("Cannot cast an ERRor resul to OK");
				}
		}
//////////////////////////////////////////////////////////////////////////////////////////
// OK & ERROR  Impl.
/////////////////////////////////////////////////////////////////////////////////////////

		@Accessors(prefix="_")
		public static class ApiKeyJWTAuthenticationStepResultOK<U extends User,L extends Login,
		                                                        E extends UserLoginEntry>
					extends ApiKeyJWTAuthenticationStepResultBase<U,L,E>
		    implements ApiKeyJWTAuthenticationStepResult<U,L,E> {

			public ApiKeyJWTAuthenticationStepResultOK(final JWTDecoded jwtDecoded,
					                                   final U user, final L apiKeyLogin, final E apiKeyLoginEntry) {
				super(jwtDecoded, user, apiKeyLogin, apiKeyLoginEntry);
			}

			@Override
			public boolean hasFailed() {
				return false;
			}

			@Override
			public boolean hasSucess() {
				return true;
			}


		}

		@Accessors(prefix="_")
		public static class ApiKeyJWTAuthenticationStepResultError<U extends User,L extends Login,
		                                                           E extends UserLoginEntry>
				extends ApiKeyJWTAuthenticationStepResultBase<U,L,E>
		    implements ApiKeyJWTAuthenticationStepResult<U,L,E> {

			@Getter  ApiKeyJWTAuthenticationError _error;
			@Getter  String _detail;


			public ApiKeyJWTAuthenticationStepResultError(final JWTDecoded jwtDecoded,
			                                              final U user, final L apiKeyLogin, final E apiKeyLoginEntry) {
	              super(jwtDecoded, user, apiKeyLogin, apiKeyLoginEntry);
			}

			public ApiKeyJWTAuthenticationStepResultError<U,L,E> with(final ApiKeyJWTAuthenticationError error,
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
			public boolean hasSucess() {
				return false;
			}

		}

}


