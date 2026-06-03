package r01f.model.security.login;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.google.common.collect.Ordering;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.PersistableSecurityObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

/**
 * login
 */
@Accessors(prefix="_")
public abstract class LoginBase<SELF_TYPE extends LoginBase<SELF_TYPE>>
	 		  extends PersistableSecurityObjectBase<LoginOID,SELF_TYPE>
  	       implements Login {

	private static final long serialVersionUID = -2731874965196060239L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * The login id 
	 */
	@MarshallField(as="loginId",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected LoginID _loginId;
	/**
	 * The [user] linked to this [login]
	 */
	@MarshallField(as="userOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected UserOID _userOid;
	/**
	 * A login (ie google login) is linked to a certain user
	 * but the login can be unlinked from this user and linked to another user
	 */
	@MarshallField(as="userLinkageHistory",
				   whenXml=@MarshallFieldAsXml(collectionElementName="entry"))
	@Getter @Setter protected Collection<LoginWithUserLinkageEntry> _userLinkageHistory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginBase() {
		// default no-args constructor
	}
	public LoginBase(final LoginOID loginOid) {
		_oid = loginOid;
	}
	public LoginBase(final UserOID userOid,final LoginID loginId) {
		this(null,
			 userOid,loginId);
	}
	public LoginBase(final LoginOID loginOid,
					 final UserOID userOid,final LoginID loginId) {
		_oid = loginOid;
		_userOid = userOid;
		_loginId = loginId;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LINKAGE TO USER
/////////////////////////////////////////////////////////////////////////////////////////
	public void linkToUser(final UserOID userOid) {
		if (_userOid != null && _userOid.isNOT(userOid)) this.unlinkFromUser(userOid);
		_userOid = userOid;
		this.addUserLinkageHistoryEntry(LoginWithUserLinkageEntry.createForLinkageToUser(userOid));
	}
	public void unlinkFromUser(final UserOID userOid) {
		if (_userOid != null && _userOid.is(userOid)) _userOid = null;
		this.addUserLinkageHistoryEntry(LoginWithUserLinkageEntry.createForUnLinkageFromUser(userOid));
	}
	public void addUserLinkageHistoryEntry(final LoginWithUserLinkageEntry entry) {
		if (_userLinkageHistory == null) _userLinkageHistory = Lists.newArrayList();
		_userLinkageHistory.add(entry);		
	}
	/**
	 * Returns the login-user linkage history ordered by date
	 * @return
	 */
	public Collection<LoginWithUserLinkageEntry> getUserLinkageHistoryOrderedByDate() {
		return CollectionUtils.hasData(_userLinkageHistory) ? Ordering.from(new Comparator<LoginWithUserLinkageEntry>() {
																					@Override
																					public int compare(final LoginWithUserLinkageEntry e1,final LoginWithUserLinkageEntry e2) {
																						return e1 != null && e2 != null
																									? e1.getDate() != null && e2.getDate() != null
																											? e1.getDate().compareTo(e2.getDate())
																											: e1.getDate() != null && e2.getDate() == null
																														? 1
																														: e1.getDate() == null && e2.getDate() == null
																																? -1
																																: 0		// both null
																									: e1 != null && e2 == null
																											? 1
																											: e1 == null && e2 != null
																													? -1
																													: 0;		// both null
																					}
																			})
																	   .sortedCopy(_userLinkageHistory)
															: null;
	}
	/**
	 * Returns any login-user linkage history
	 * @param userOid
	 * @return
	 */
	public Collection<LoginWithUserLinkageEntry> getUserLinkageHistoryEntriesRelatedTo(final UserOID userOid) {
		return CollectionUtils.hasData(_userLinkageHistory)
					? _userLinkageHistory.stream()
										 .filter(entry -> entry.getUserOid() != null
													   && entry.getUserOid().is(userOid))
										 .collect(Collectors.toList())
					: null;
	}
	/**
	 * Returns true if there exists any login-user linkage history related to the given user
	 * @param userOid
	 * @return
	 */
	public boolean hasAnyUserLinkageHistoryEntryRelatedTo(final UserOID userOid) {
		return CollectionUtils.hasData(this.getUserLinkageHistoryEntriesRelatedTo(userOid));
	}
}
