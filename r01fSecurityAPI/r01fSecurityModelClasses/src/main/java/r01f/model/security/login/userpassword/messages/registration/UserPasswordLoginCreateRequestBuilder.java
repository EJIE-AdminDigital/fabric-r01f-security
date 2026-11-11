package r01f.model.security.login.userpassword.messages.registration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.model.security.IsSecurityPrincipal;
import r01f.oids.SecurityIDs.LoginAndPassword;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserPasswordLoginCreateRequestBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static <P extends IsSecurityPrincipal> UserPasswordLoginCreateRequestBuilderLoginStep<P> forSecurityPrincipal(final P principal) {
		UserPasswordLoginCreateRequest<P> req = new UserPasswordLoginCreateRequest<>();
		req.setSecurityPrincipal(principal);
		return new UserPasswordLoginCreateRequestBuilder() { /* nothing */ }
						.new UserPasswordLoginCreateRequestBuilderLoginStep<>(req);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginCreateRequestBuilderLoginStep<P extends IsSecurityPrincipal> {
		private final UserPasswordLoginCreateRequest<P> _request;

		public BuilderBuildStep<UserPasswordLoginCreateRequest<P>> withLoginAndPassword(final LoginAndPassword loginAndPassword) {
			_request.setLoginAndPassword(loginAndPassword);
			return new BuilderBuildStep<UserPasswordLoginCreateRequest<P>>(_request);
		}
	}
}