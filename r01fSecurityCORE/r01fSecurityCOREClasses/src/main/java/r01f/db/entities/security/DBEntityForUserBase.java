package r01f.db.entities.security;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.model.security.user.UserStatus;

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForUserBase
			  extends DBEntityForSecurityObjectBase {

	private static final long serialVersionUID = -6628025715362798888L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
    @Column(name="STATUS",nullable=true) @Enumerated(EnumType.STRING)
	@Getter	@Setter	protected UserStatus _status;

	@Column(name="PREF_LANG") @Enumerated(EnumType.STRING)
	@Getter @Setter protected Language _prefLang;

	@Column(name="SYS_WIDE_ROLE") @Basic
	@Getter @Setter protected boolean _hasSystemWideRole;

	@Column(name="SEARCHED_COLS") @Basic
	@Getter @Setter protected String _searchedColumns;

}
