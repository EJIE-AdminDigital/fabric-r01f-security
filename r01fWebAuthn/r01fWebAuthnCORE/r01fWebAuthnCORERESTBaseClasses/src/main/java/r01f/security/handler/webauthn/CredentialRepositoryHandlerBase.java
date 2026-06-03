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
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.entry.FindServicesForUserLoginEntry;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.api.interfaces.security.webauthn.FindServicesForAuthenticatorCredential;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.user.User;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.objectstreamer.Marshaller;
import r01f.security.handler.webauthn.utils.WebAuthnByteStreamUtils;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.StringBase64;

@Slf4j
@RequiredArgsConstructor
public class CredentialRepositoryHandlerBase<U extends User,
                                             E extends UserLoginEntry, C extends AuthenticatorCredential>
			implements CredentialRepository {

	// system-internal security context provider (used to query the CRUD services)
	protected final Provider<SecurityContext> _sysSecurityContextProvider;

	// marshaller
	protected final Marshaller _marshaller;

	// user login CRUD
	protected final CRUDServicesForUser<U> _userCrud;

	// user login CRUD
	protected final CRUDServicesForUserLoginEntry<E> _loginEntryCrud;
	protected final FindServicesForUserLoginEntry<E> _loginEntryFind;

	protected final CRUDServicesForAuthenticatorCredential<C> _authenticatorCredentialCrud;
	protected final FindServicesForAuthenticatorCredential<C> _authenticatorCredentialFind;
/////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
	@Override
	public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(final String username) {
		ByteArray userHandle = getUserHandleForUsername(username).get();
		if (userHandle == null) {
			return null;
		}
		UserOID userOid = UserOID.forId(WebAuthnByteStreamUtils.from(userHandle));
		Collection<C> credentials = _authenticatorCredentialFind.findBy(_sysSecurityContextProvider.get(),
				                                                      userOid)
				                                                .getOrNull();
		return credentials.stream()
					        .map(
					            credential ->
					                PublicKeyCredentialDescriptor.builder()
													                    .id(ByteArray.fromBase64(credential.getCredentialId().asString()))
													                    .build())
			                .collect(Collectors.toSet());

	}

	@Override
	public Optional<ByteArray> getUserHandleForUsername(final String username) {
		log.warn("##############################################################################");
		log.warn( "CredentialRepositoryHandlerBase.getUserHandleForUsername('{}')",username    );
		log.warn("##############################################################################");

		E  entry = _loginEntryCrud.loadLastLoginEntryFor(_sysSecurityContextProvider.get(),
							                              null, // any
							                              //  WARNING!! Suppose "username" is LogiID in any IdProvider
							                              //  --------------------------------------------------------------
							                              LoginID.forId(username),
							                              //  WARNING!! Supposethat with that "username"/LogiID  has logged any moment
							                              //  -------------------------------------------------------------------------
							                              UserLoginEntryID.forLoginOk()).getOrNull();
		if ( entry == null) {
			log.warn(" User for usename {} has never been logged!!" , username);
			return Optional.empty();
		}

		UserOID userOid = entry.getUserOid();
		log.warn(" ... user oid {}", userOid);
	    return Optional.of(WebAuthnByteStreamUtils.from(userOid));
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

		UserOID userOid = UserOID.forId(WebAuthnByteStreamUtils.from(userHandleAsByteArray));
		CredentialID credId = CredentialID.fromBase64String(StringBase64.fromBase64EncodedString(credentialIdAsByteArray.getBase64()));

		log.warn("##############################################################################");
		log.warn( " [*] CredentialRepositoryHandlerBase.lookup('{} , {}')",
				                                      credId,
				                                      userOid    );
		log.warn("##############################################################################");
		AuthenticatorCredential credential = null;
		try {
			credential = _authenticatorCredentialCrud.loadBy(_sysSecurityContextProvider.get(),
				                                                 credId, userOid)
												          	.getOrNull();
		} catch (final Throwable th) {
			th.printStackTrace();
		}
		if (credential == null) {
			log.error(" not found any credential ! ");
			return Optional.empty();
		}
		log.warn(" \n \n found credential for {}/{} ", userOid, credId);
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
			log.error(" not found any credential ! ");
			return Sets.newHashSet();
		}
		log.warn(" found credential for {}", credId);
		ByteArray pk  = WebAuthnByteStreamUtils.from(credential.getPublicKey());
		ByteArray userHandleAsByteArray = WebAuthnByteStreamUtils.from(credential.getUserOid());
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
