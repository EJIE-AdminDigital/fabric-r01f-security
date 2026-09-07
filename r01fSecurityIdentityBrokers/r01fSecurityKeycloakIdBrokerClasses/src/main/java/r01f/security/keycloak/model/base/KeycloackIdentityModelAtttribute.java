package r01f.security.keycloak.model.base;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakAttributeID;

@Accessors(prefix="_")
public class KeycloackIdentityModelAtttribute {
	
	@Getter private final KeycloakAttributeID _id;
    @Getter private final KeycloakAttributeValue _value;
////////////////////////////////////////////////////////
 /// CONSTRCUTOR
 ////////////////////////////////////////////////////////////  
    public KeycloackIdentityModelAtttribute(final KeycloakAttributeID forId, final KeycloakAttributeValue value) {
    	_id  = forId;
    	_value = value;
	}
	////////////////////////////////////////////////////////
/// ATTRIBUTTE VALUE
////////////////////////////////////////////////////////////
    public static final class KeycloakAttributeValue {

        private final List<String> values;
     
        public KeycloakAttributeValue(final List<String> values) {
            this.values = values != null ? List.copyOf(values) : Collections.emptyList();
        }

        public static KeycloakAttributeValue of(final String singleValue) {
            return new KeycloakAttributeValue(Collections.singletonList(singleValue));
        }

        public static KeycloakAttributeValue of(final List<String> values) {
            return new KeycloakAttributeValue(values);
        }

        public List<String> asList() {
            return this.values;
        }

        public String asSingleString() {
            return this.values.isEmpty() ? null : this.values.get(0);
        }

        public boolean isMultivalued() {
            return this.values.size() > 1;
        }
        
        public boolean isEmpty() {
            return this.values.isEmpty();
        }

        @Override
        public String toString() {
            return values.toString();
        }
    }
}
