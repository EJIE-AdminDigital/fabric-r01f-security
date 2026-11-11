package r01f.security.handler.webauthn;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;

import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.login.entry.FindServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.api.interfaces.security.webauthn.FindServicesForAuthenticatorCredential;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.handler.webauthn.utils.WebAuthnByteStreamUtils;
import r01f.types.StringBase64;

@Slf4j
@RequiredArgsConstructor
public class CredentialRepositoryHandlerBase<P extends IsSecurityPrincipal,C extends AuthenticatorCredential>
  implements CredentialRepository {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// system-internal security context provider (used to query the CRUD services)
	protected final Provider<SecurityContext> _sysSecurityContextProvider;

	// marshaller
	protected final Marshaller _marshaller;

	// user login CRUD
	protected final CRUDServicesForPrincipal<P> _principalCrud;

	// user login CRUD
	protected final CRUDServicesForLoginEntry _loginEntryCrud;
	protected final FindServicesForLoginEntry _loginEntryFind;

	protected final CRUDServicesForAuthenticatorCredential<C> _authenticatorCredentialCrud;
	protected final FindServicesForAuthenticatorCredential<C> _authenticatorCredentialFind;
/////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
	@Override
	public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(final String username) {
		ByteArray userHandle = this.getUserHandleForUsername(username)
								   .get();
		if (userHandle == null) {
			return null;
		}
		SecurityPrincipalOID userOid = SecurityPrincipalOID.forId(WebAuthnByteStreamUtils.from(userHandle));
		Collection<C> credentials = _authenticatorCredentialFind.findBy(_sysSecurityContextProvider.get(),
				                                                      userOid)
				                                                .getOrNull();
		return credentials.stream()
				          .map(credential -> PublicKeyCredentialDescriptor.builder()
												                    	  .id(ByteArray.fromBase64(credential.getCredentialId().asString()))
												                    	  .build())
				          .collect(Collectors.toSet());
	}
	
	@Override
	public Optional<ByteArray> getUserHandleForUsername(final String username) {
		log.warn("##############################################################################");
		log.warn( "CredentialRepositoryHandlerBase.getUserHandleForUsername('{}')",username    );
		log.warn("##############################################################################");

		LoginEntry loginEntry = _loginEntryCrud.loadLastLoginEntryFor(_sysSecurityContextProvider.get(),
										                              null, 	// any IdentityProvider
										                              //  WARNING!! Suppose "username" is LogiID in any IdProvider
										                              //  --------------------------------------------------------------
										                              LoginID.forId(username),
										                              //  WARNING!! Suppose that with that "username"/LoginID  has logged any moment
										                              //  -------------------------------------------------------------------------
										                              LoginEntryEvent.LOGIN_OK)
										       .getOrNull();
		if (loginEntry == null) {
			log.warn(" User for usename {} has never been logged!!" , username);
			return Optional.empty();
		}

		SecurityPrincipalOID principalOid = loginEntry.getSecurityPrincipalOid();
		log.warn("... [security principal] oid={}",
				 principalOid);
	    return Optional.of(WebAuthnByteStreamUtils.from(principalOid));
	}

	@Override
	public Optional<String> getUsernameForUserHandle(final ByteArray userHandle) {
		log.warn("##############################################################################");
		log.warn( "CredentialRepositoryHandlerBase.getUsernameForUserHandle('{}')",userHandle    );
		log.warn("##############################################################################");
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<RegisteredCredential> lookup(final ByteArray credentialIdAsByteArray, final ByteArray userHandleAsByteArray) {

		SecurityPrincipalOID principalOid = SecurityPrincipalOID.forId(WebAuthnByteStreamUtils.from(userHandleAsByteArray));
		CredentialID credId = CredentialID.fromBase64String(StringBase64.fromBase64EncodedString(credentialIdAsByteArray.getBase64()));

		log.warn("##############################################################################");
		log.warn(" [*] CredentialRepositoryHandlerBase.lookup('{}, {}')",
                 credId,
                 principalOid    );
		log.warn("##############################################################################");
		AuthenticatorCredential credential = null;
		try {
			credential = _authenticatorCredentialCrud.loadBy(_sysSecurityContextProvider.get(),
				                                             credId,principalOid)
												     .getOrNull();
		} catch (final Throwable th) {
			th.printStackTrace();
		}
		if (credential == null) {
			log.error("not found any credential!");
			return Optional.empty();
		}
		log.warn("\n\n" +
				 "found credential for {}/{} ", 
				 principalOid,credId);
	    ByteArray pk  = WebAuthnByteStreamUtils.from(credential.getPublicKey());
	    RegisteredCredential  rgc = RegisteredCredential.builder()
												        .credentialId(credentialIdAsByteArray)
												        .userHandle(userHandleAsByteArray)
												        .publicKeyCose(pk)
												        .signatureCount(credential.getSignatureCount())
												        .build();
	    return Optional.of(rgc);
	}

	@Override
	public Set<RegisteredCredential> lookupAll(final ByteArray credentialIdAsByteArray) {
		CredentialID credId = CredentialID.fromBase64String(StringBase64.fromBase64EncodedString(credentialIdAsByteArray.getBase64()));
		log.warn("##############################################################################");
		log.warn( " [*] CredentialRepositoryHandlerBase.lookupAll('{}')",credId);
		log.warn("##############################################################################");
		AuthenticatorCredential credential = null;
		try {
			credential =_authenticatorCredentialCrud.loadBy(_sysSecurityContextProvider.get(),
                                                            credId)
												    .getOrNull();
		} catch (final Throwable th) {
			th.printStackTrace();
		}
		if (credential == null) {
			log.error("not found any credential!");
			return Sets.newHashSet();
		}
		log.warn("found credential for {}",
				 credId);
		ByteArray pk  = WebAuthnByteStreamUtils.from(credential.getPublicKey());
		ByteArray userHandleAsByteArray = WebAuthnByteStreamUtils.from(credential.getSecurityPrincipalOid());
		RegisteredCredential  rgc = RegisteredCredential.builder()
														.credentialId(credentialIdAsByteArray)
														.userHandle(userHandleAsByteArray)
														.publicKeyCose(pk)
														.signatureCount(credential.getSignatureCount())
													 .build();
		return Sets.newHashSet(rgc);
	}
//////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////
}
