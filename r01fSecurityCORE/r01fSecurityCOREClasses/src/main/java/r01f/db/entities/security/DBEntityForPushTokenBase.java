package r01f.db.entities.security;

//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.pushtoken.IsPushDestinationID.PushDestinationType;
import r01f.model.security.pushtoken.PushDestinationPlatform;
import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;

@MappedSuperclass
//@Multitenant(MultitenantType.SINGLE_TABLE)
@Accessors(prefix = "_")
@NoArgsConstructor
public abstract class DBEntityForPushTokenBase
			  extends DBEntityForSecurityObjectBase
		   implements DBEntityHasModelObjectDescriptor {

	private static final long serialVersionUID = 1280257537786910664L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- SOURCE SYSTEM
	@Column(name="SOURCE_SYSTEM_ID",nullable=true) @Basic
    @Getter @Setter protected String _sourceSystemId;
	
////////// --- DESTINATION
	@Column(name="DESTINATION_ID",nullable=false) @Basic
	@Getter @Setter protected String _destinationId;
	
	@Column(name="DESTINATION_TYPE",nullable=false) @Basic
	@Enumerated(EnumType.STRING)
	@Getter @Setter protected PushDestinationType _destinationType;	
	
	@Column(name="DESTINATION_PLATFORM",nullable=true) @Basic
    @Getter @Setter protected PushDestinationPlatform _destinationPlatform;

////////// --- TOKEN	
	@Column(name="TOKEN",nullable=true) @Basic
    @Getter @Setter protected String _token;
	
}
