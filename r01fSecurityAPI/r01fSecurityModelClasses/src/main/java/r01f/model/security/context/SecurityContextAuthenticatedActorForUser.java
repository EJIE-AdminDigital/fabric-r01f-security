package r01f.model.security.context;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.aspects.dirtytrack.DirtyTrackAdapter;
import r01f.aspects.interfaces.dirtytrack.DirtyStateTrackable;
import r01f.model.security.user.User;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;

@MarshallType(as="authenticatedActorForUser")
@Accessors(prefix="_")
public class SecurityContextAuthenticatedActorForUser<U extends User>
	 extends SecurityContextAuthenticatedActorBase {

	private static final long serialVersionUID = -7165100182686536360L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="user")
	@Getter private final U _user;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @see SecurityContextAuthenticatedActorBuilder
	 * @param securityProviderId
	 * @param loginId
	 * @param user
	 */
	public SecurityContextAuthenticatedActorForUser(final SecurityProviderID securityProviderId,final LoginID loginId,
											 		final U user) {
		super(securityProviderId,loginId);
		if (  securityProviderId != null  // allow passkey
							  	 && securityProviderId.isNOTContainedIn(SecurityProviderID.USER_PASSWORD,
																		SecurityProviderID.GOOGLE,
																		SecurityProviderID.GILTZA,
																		SecurityProviderID.SAML,
																		SecurityProviderID.PAIRED_PHONE,
																		SecurityProviderID.API_KEY,
																		SecurityProviderID.XLNETS,
																		SecurityProviderID.IDENTITY_BROKER,
																		
																		SecurityProviderID.OSAKIDETZA_AZURE_AD)) throw new IllegalArgumentException( "Not found any provider with ID "  + securityProviderId +
																																		" > Use " + SecurityContextAuthenticatedActorForUser.class.getSimpleName() + " for USER-related providers: " +
																																		SecurityProviderID.USER_PASSWORD + ", " +
																																	    SecurityProviderID.GOOGLE + ", " +
																																	    SecurityProviderID.GILTZA + ", " +
																																	    SecurityProviderID.SAML + ", " +
																																	    SecurityProviderID.PAIRED_PHONE + " or " +
																																	    SecurityProviderID.API_KEY + " or " +
																																	    SecurityProviderID.XLNETS + " or " +
																																	    SecurityProviderID.OSAKIDETZA_AZURE_AD);
		_user = user;

		// BEWARE!! The user object is NOT new
		// Sets loaded record tracking status (not new, not dirty...)
		if (_user != null
		 && _user instanceof DirtyStateTrackable) {
			DirtyStateTrackable trackable = DirtyTrackAdapter.adapt(_user);
			trackable.resetDirty();									// The object's state is clean... no modifications
			trackable.startTrackingChangesInState(true);			// Start tracking changes in state
			trackable.getTrackingStatus().setThisNew(false);		// NOT a new record
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a new instance of this security context with an updated user data
	 * @param user
	 * @return
	 */
	public SecurityContextAuthenticatedActorForUser<U> cloneWithUpdatedUser(final U user) {
		if (user == null) throw new IllegalArgumentException("user cannot be null!");
		if (_user.getOid().isNOT(user.getOid())) throw new IllegalArgumentException("cannot update the [authenticated actor]'s user: the user MUST be the same");
		return new SecurityContextAuthenticatedActorForUser<U>(this.getSecurityProviderId(),this.getLoginId(),
															   user);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER
/////////////////////////////////////////////////////////////////////////////////////////
	public UserOID getUserOid() {
		return _user.getOid();
	}
}
