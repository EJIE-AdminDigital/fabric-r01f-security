package r01f.model.security;

import lombok.Getter;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.types.Pair;
import r01f.util.types.Strings;

/**
 * A pair of [identity provider] + [authentication method]
 */
public record IdentityProviderAndAuthnMethod(@Getter IdentityProvider identityProvider,
											 @Getter AuthenticationMethod authnMethod) {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public String asString() {
		return IdentityProviderAndAuthnMethod.asString(this.identityProvider,this.authnMethod);
	}	
/////////////////////////////////////////////////////////////////////////////////////////
// 	Secondary Constructors
/////////////////////////////////////////////////////////////////////////////////////////	
	public IdentityProviderAndAuthnMethod(final String compositeId) {
        this(pairFromString(compositeId)); // This constructor is required by yaml config used by Spring
    }
	
	public IdentityProviderAndAuthnMethod(final Pair<IdentityProvider,AuthenticationMethod> pair) {
        this(pair.getA(),pair.getB());
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static String asString(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		return Strings.customized("{}/{}",
								  identityProvider.name(),authnMethod.name());
	}
	/**
	 * Parses a cookie value in the format "identityProvider/authnMethod" and returns an IdentityProviderAndAuthMethod object.
	 * @param cookieValue
	 * @return
	 */
	public static IdentityProviderAndAuthnMethod fromString(final String cookieValueWithCompositeId) {		
		return new IdentityProviderAndAuthnMethod(cookieValueWithCompositeId);
	}
	
	public static Pair<IdentityProvider,AuthenticationMethod> pairFromString(final String identityProviderAndAuthnMethodAString) {
		String[] parts = identityProviderAndAuthnMethodAString.split("/");
		if (parts.length != 2) throw new IllegalArgumentException(Strings.customized("Invalid  value {}. It must be 'identityProvider/authMethod'",identityProviderAndAuthnMethodAString));
		return new Pair<>(IdentityProvider.forId(parts[0]),
						  AuthenticationMethod.forId(parts[1]));
	}
	
}
