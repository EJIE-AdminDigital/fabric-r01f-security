package r01f.model.security.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.OID;
import r01f.model.security.user.User;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.types.AppVersion;
import r01f.types.contact.Phone;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SecurityContextAuthenticatedActorBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	SYSTEM
/////////////////////////////////////////////////////////////////////////////////////////
	public static SecurityContextAuthenticatedActorForSystem forSystem() {
		return new SecurityContextAuthenticatedActorForSystem();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	API KEY
/////////////////////////////////////////////////////////////////////////////////////////
	public static SecurityContextAuthenticatedActorForApiKeyNotUserBacked forApiKeyNotUserBacked(final LoginOID apiKeyOid,
																								 final LoginID loginId,final AppVersion version) {
		return new SecurityContextAuthenticatedActorForApiKeyNotUserBacked(apiKeyOid,
																		   loginId,version);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER BACKED
/////////////////////////////////////////////////////////////////////////////////////////
	public static <U extends User> SecurityContextAuthenticatedActorForUserBuilerProviderStep<U> forUser(final U user) {

		return new SecurityContextAuthenticatedActorForUserBuilerProviderStep<>(user);
	}
	public static <U extends User> SecurityContextAuthenticatedActorForUserBuilerProviderStep<U> forUserBackedApiKey(final U user) {
		return new SecurityContextAuthenticatedActorForUserBuilerProviderStep<>(user);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class SecurityContextAuthenticatedActorForUserBuilerProviderStep<U extends User> {
		private final U _user;

		public SecurityContextAuthenticatedActorForUserBuilerLoginStep<U> using(final SecurityProviderID securityProviderId) {
			if (securityProviderId != null  // allow passkey
		     && securityProviderId.isNOTContainedIn(SecurityProviderID.USER_PASSWORD,
												    SecurityProviderID.GOOGLE,
												    SecurityProviderID.GILTZA,
												    SecurityProviderID.SAML,
												    SecurityProviderID.PAIRED_PHONE,
												    SecurityProviderID.XLNETS,
												    SecurityProviderID.OSAKIDETZA_AZURE_AD)) throw new IllegalArgumentException("Use " + SecurityContextAuthenticatedActorForUser.class.getSimpleName() + " for USER-related providers: " +
												  																			    CollectionUtils.toStringCommaSeparated(SecurityProviderID.USER_PASSWORD,
												  																					  								   SecurityProviderID.GOOGLE,
												  																					  								   SecurityProviderID.SAML,
												  																					  								   SecurityProviderID.PAIRED_PHONE,
												  																					  								   SecurityProviderID.XLNETS,
												  																					  								   SecurityProviderID.OSAKIDETZA_AZURE_AD));
			return new SecurityContextAuthenticatedActorForUserBuilerLoginStep<>(_user,securityProviderId);
		}
		public SecurityContextAuthenticatedActorForApiKeyUserBacked<U> usingApiKey(final LoginOID apiKeyOid,
																				   final LoginID loginId,final AppVersion version) {
			return new SecurityContextAuthenticatedActorForApiKeyUserBacked<>(apiKeyOid,
																			  loginId,version,
																	 		  _user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class SecurityContextAuthenticatedActorForUserBuilerLoginStep<U extends User> {
		private final U _user;
		private final SecurityProviderID _securityProviderId;

		public SecurityContextAuthenticatedActorForUser<U> loggedInWith(final LoginID loginId) {
			return new SecurityContextAuthenticatedActorForUser<>(_securityProviderId,loginId,
																  _user);
		}
		public SecurityContextAuthenticatedActorForUser<U> loggedInWith(final Phone phone) {
			return this.loggedInWith(LoginID.fromPhone(phone));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	APP
/////////////////////////////////////////////////////////////////////////////////////////
	public static SecurityContextAuthenticatedActorForApp forApp(final AppCode appCode) {
		return new SecurityContextAuthenticatedActorForApp(appCode);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTERED DEVICE
/////////////////////////////////////////////////////////////////////////////////////////
	public static <O extends OID> SecurityContextAuthenticatedActorForRegisteredDevice<O> forRegisteredDevice(final O deviceOid) {
		return new SecurityContextAuthenticatedActorForRegisteredDevice<>(deviceOid);
	}
}
