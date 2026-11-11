package r01f.model.security.login;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.google.common.collect.Ordering;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsLogin;
import r01f.model.security.IsLoginData;
import r01f.model.security.LoginStatus;
import r01f.model.security.SecurityPersistableObjectWithIDBase;
import r01f.model.security.login.data.apikey.LoginDataForApiKey;
import r01f.model.security.login.data.oauthflow.LoginDataForOAUTHFlow;
import r01f.model.security.login.data.pairedphone.LoginDataForPairedPhone;
import r01f.model.security.login.data.userpassword.LoginDataForUserPassword;
import r01f.model.security.login.data.xlnets.LoginDataForXLNets;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.LoginIDFormat;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.types.Range;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

/**
 * login
 */
@Accessors(prefix="_")
public class Login
	 extends SecurityPersistableObjectWithIDBase<LoginOID,LoginID,
			  									 Login>
  implements IsLogin {

	private static final long serialVersionUID = -2731874965196060239L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="idFormat",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected LoginIDFormat _idFormat;
	
////////// --- STATUS
	@MarshallField(as="status",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected LoginStatus _status;	

////////// --- SECURITY PRINCIPAL
    @MarshallField(as="securityPrincipalSourceIdentityProvider",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected IdentityProvider _securityPrincipalSourceIdentityProvider;
    
	@MarshallField(as="securityPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected SecurityPrincipalOID _securityPrincipalOid;
	
	@MarshallField(as="securityPrincipalid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected SecurityPrincipalID _securityPrincipalId;
	
////////// --- AUTHENTICATION METHOND 
	@MarshallField(as="authenticationMethod",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected AuthenticationMethod _authenticationMethod;
	
////////// --- VALID RANGE
	@MarshallField(as="validRange")
	@Getter @Setter protected Range<Instant> _validRange;
	
////////// --- DATA THAT DEPENDS ON THE LOGIN METHOD
	@MarshallField(as="data")
	@Getter @Setter protected IsLoginData _data;

////////// --- LINKAGE HISTORY TO SECURITY PRINCIPAL
	/**
	 * A [login] (ie google login) is linked to a certain [security principal] 
	 * but the [login] can be unlinked from this [security principal] and linked to another [security principal]
	 */
	@MarshallField(as="securityPrincipalLinkageHistory",
				   whenXml=@MarshallFieldAsXml(collectionElementName="entry"))
	@Getter @Setter protected Collection<LoginWithSecurityPrincipalLinkageEntry> _securityPrincipalLinkageHistory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public Login() {
		// default no-args constructor
	}
	public Login(final LoginID loginId,final LoginIDFormat type) {
		this(null,loginId,type);
	}
	public Login(final LoginID loginId) {
		this(null,loginId,null);
	}
	public Login(final LoginOID loginOid,final LoginID loginId,final LoginIDFormat type) {
		this(loginOid,loginId,type,null,null);
	}
	public Login(final LoginOID loginOid,final LoginID loginId) {
		this(loginOid,loginId,null);
	}
	public Login(final LoginID loginId,
			     final SecurityPrincipalOID securityPrincipalOid,final SecurityPrincipalID securityPrincipalid) {
		this(null,loginId,null,
			 securityPrincipalOid,securityPrincipalid);
	}
	public Login(final LoginID loginId,final LoginIDFormat type,
			     final SecurityPrincipalOID securityPrincipalOid,final SecurityPrincipalID securityPrincipalid) {
		this(null,loginId,type,
			 securityPrincipalOid,securityPrincipalid);
	}
	public Login(final LoginOID loginOid,final LoginID loginId,final LoginIDFormat type,
			     final SecurityPrincipalOID securityPrincipalOid,final SecurityPrincipalID securityPrincipalid) {
		_oid = loginOid;
		_id = loginId;
		_idFormat = type != null ? type 
								 : loginId != null ? LoginIDFormat.fromLogin(loginId) : null;
		_securityPrincipalOid = securityPrincipalOid;
		_securityPrincipalId = securityPrincipalid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public <D extends IsLoginData> D getDataAs(final Class<D> type) {
		return (D)_data;
	}
	public LoginDataForOAUTHFlow getDataAsForOAUTHFlow() {
		return this.getDataAs(LoginDataForOAUTHFlow.class);
	}
	public LoginDataForApiKey getDataAsForApiKey() {
		return this.getDataAs(LoginDataForApiKey.class);
	}
	public LoginDataForUserPassword getDataAsForUserPassword() {
		return this.getDataAs(LoginDataForUserPassword.class);
	}
	public LoginDataForPairedPhone getDataAsForPairedPhone() {
		return this.getDataAs(LoginDataForPairedPhone.class);
	}
	public LoginDataForXLNets getDataAsForXLNets() {
		return this.getDataAs(LoginDataForXLNets.class);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LINKAGE TO SECURITY PRINCIPAL
/////////////////////////////////////////////////////////////////////////////////////////
	public void linkToSecurityPrincipal(final SecurityPrincipalOID securityPrincipalOid) {
		if (_securityPrincipalOid != null && _securityPrincipalOid.isNOT(securityPrincipalOid)) this.unlinkFromSecurityPrincipal(securityPrincipalOid);
		_securityPrincipalOid = securityPrincipalOid;
		this.addSecurityPrincipalLinkageHistoryEntry(LoginWithSecurityPrincipalLinkageEntry.createForLinkageToSecurityPrincipal(securityPrincipalOid));
	}
	public void unlinkFromSecurityPrincipal(final SecurityPrincipalOID securityPrincipalOid) {
		if (_securityPrincipalOid != null && _securityPrincipalOid.is(securityPrincipalOid)) _securityPrincipalOid = null;
		this.addSecurityPrincipalLinkageHistoryEntry(LoginWithSecurityPrincipalLinkageEntry.createForUnLinkageFromSecurityPrincipal(securityPrincipalOid));
	}
	public void addSecurityPrincipalLinkageHistoryEntry(final LoginWithSecurityPrincipalLinkageEntry entry) {
		if (_securityPrincipalLinkageHistory == null) _securityPrincipalLinkageHistory = Lists.newArrayList();
		_securityPrincipalLinkageHistory.add(entry);		
	}
	/**
	 * Returns the [login] <-> [security principal] linkage history ordered by date
	 * @return
	 */
	public Collection<LoginWithSecurityPrincipalLinkageEntry> getSecurityPrincipalLinkageHistoryOrderedByDate() {
		return CollectionUtils.hasData(_securityPrincipalLinkageHistory) 
						? Ordering.from(new Comparator<LoginWithSecurityPrincipalLinkageEntry>() {
												@Override
												public int compare(final LoginWithSecurityPrincipalLinkageEntry e1,final LoginWithSecurityPrincipalLinkageEntry e2) {
													return e1 != null && e2 != null
																? e1.getAt() != null && e2.getAt() != null
																		? e1.getAt().compareTo(e2.getAt())
																		: e1.getAt() != null && e2.getAt() == null
																					? 1
																					: e1.getAt() == null && e2.getAt() == null
																							? -1
																							: 0		// both null
																: e1 != null && e2 == null
																		? 1
																		: e1 == null && e2 != null
																				? -1
																				: 0;		// both null
												}
										})
								   .sortedCopy(_securityPrincipalLinkageHistory)
						: null;
	}
	/**
	 * Returns any [login] <-> [security principal] linkage history
	 * @param securityPrincipalOid
	 * @return
	 */
	public Collection<LoginWithSecurityPrincipalLinkageEntry> getSecurityPrincipalLinkageHistoryEntriesRelatedTo(final SecurityPrincipalOID securityPrincipalOid) {
		return CollectionUtils.hasData(_securityPrincipalLinkageHistory)
					? _securityPrincipalLinkageHistory.stream()
													  .filter(entry -> entry.getSecurityPrincipalOid() != null
																    && entry.getSecurityPrincipalOid().is(securityPrincipalOid))
													  .collect(Collectors.toList())
					: null;
	}
	/**
	 * Returns true if there exists any [login] <-> [security principal] linkage history related to the given [security principal]
	 * @param securityPrincipalOid
	 * @return
	 */
	public boolean hasAnySecurityPrincipalLinkageHistoryEntryRelatedTo(final SecurityPrincipalOID securityPrincipalOid) {
		return CollectionUtils.hasData(this.getSecurityPrincipalLinkageHistoryEntriesRelatedTo(securityPrincipalOid));
	}
}
