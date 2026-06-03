package r01f.model.security.login.apikey;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.AppVersion;
import r01f.types.Range;


@NoArgsConstructor(access=AccessLevel.PUBLIC)
public abstract class ApiKeyLoginBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class ApiKeyBuilderOwnerStep<A extends ApiKeyLogin> {
		private final A _apiKeyLogin;

		public ApiKeyBuilderVersionStep<A> ownedBy(final UserOID ownerOid) {
			_apiKeyLogin.setApiKeyOwnerOid(ownerOid);
			return new ApiKeyBuilderVersionStep<>(_apiKeyLogin);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class ApiKeyBuilderVersionStep<A extends ApiKeyLogin> {
		private final A _apiKeyLogin;

		public ApiKeyBuilderReadOnlyStep<A> inVersion(final AppVersion version) {
			_apiKeyLogin.setApiKeyVersion(version);
			return new ApiKeyBuilderReadOnlyStep<>(_apiKeyLogin);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class ApiKeyBuilderReadOnlyStep<A extends ApiKeyLogin> {
		private final A _apiKeyLogin;

		public ApiKeyBuilderKeyPairStep<A> forReadOnly() {
			return this.readOnly(true);
		}
		public ApiKeyBuilderKeyPairStep<A> forReadAndWrite() {
			return this.readOnly(false);
		}
		public ApiKeyBuilderKeyPairStep<A> readOnly(final boolean readOnly) {
			_apiKeyLogin.setReadOnly(readOnly);
			return new ApiKeyBuilderKeyPairStep<>(_apiKeyLogin);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class ApiKeyBuilderKeyPairStep<A extends ApiKeyLogin> {
		private final A _apiKeyLogin;

		public ApiKeyBuilderBuildStep<A> withKeyPair(final ApiKeyPair keyPair) {
			_apiKeyLogin.setApiKeyPair(keyPair);
			_apiKeyLogin.setLoginId(keyPair.asLoginId());	// VERY IMPORTANT! APIKEY PUBLICK KEY AS LOGIN_ID!
			return new ApiKeyBuilderBuildStep<>(_apiKeyLogin);
		}
		public ApiKeyBuilderBuildStep<A> withPublicKey(final SecurityToken publicKey) {
			return this.withKeyPair(new ApiKeyPair(publicKey,
										  		   null,null));	// no private key
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class ApiKeyBuilderBuildStep<A extends ApiKeyLogin> {
		private final A _key;

		public A build() {
			_key.setApiKeyUsageRange(Range.greaterThan(Instant.now()));	// usable from now on
			_key.setApiKeyOid(LoginOID.supply());
			return _key;
		}
	}
}
