package r01f.security.keycloak.model.base;

import java.util.Collection;
import java.util.Optional;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.annotations.OidField;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.security.keycloak.model.KeycloakIDs.IsKeycloakID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakAttributeID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakOIDs.IsKeycloakOID;
import r01f.security.keycloak.model.base.KeycloackIdentityModelAtttribute.KeycloakAttributeValue;

@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public abstract class KeycloackIdentityModelObjectBase<O extends  IsKeycloakOID,ID extends IsKeycloakID,
											           SELF_TYPE extends KeycloackIdentityModelObjectBase<O,ID,SELF_TYPE>>
		   implements IsSecurityObject {	
	private static final long serialVersionUID = 6546937946507238664L;
/////////////////////////////////////////////////////////////////////////////////////////
//  SERIALIZABLE FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Model object unique identifier
	 */
	@MarshallField(as="oid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@OidField
	@Getter @Setter protected O _oid;
	/**
	 * Model object identifier [   ]
	 */
	@MarshallField(as="id",
				   whenXml=@MarshallFieldAsXml(attr=true))	
	@Getter @Setter protected ID _id;
	/**
	 * Realm [   ]
	 */
    @MarshallField(as="oid")
    @Getter @Setter private KeycloakRealm _realm;  
    
    @MarshallField(as="attributes")
    @Getter @Setter private Collection<KeycloackIdentityModelAtttribute> _attributes;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////    
    public void addAttribute(final KeycloakAttributeID id,final KeycloakAttributeValue of) {
		if (_attributes == null) {
			_attributes = Lists.newArrayList();
		}
		_attributes.add(new KeycloackIdentityModelAtttribute(id,of));
	}
    public KeycloakAttributeValue getAttributeValue(final KeycloakAttributeID id) {
    	Optional<KeycloackIdentityModelAtttribute> att = _attributes.stream()
		                                  							.filter(a -> a.getId().is(id) )
		                                  							.findAny();
    	return att.map(KeycloackIdentityModelAtttribute::getValue) 
                  .orElse(null);
    	
	}
}