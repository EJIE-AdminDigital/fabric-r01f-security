package r01f.db.entities.security.webauthn;

import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;
import r01f.services.core.persistence.db.entities.DBEntityForModelObject;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

public interface DBEntityForAuthenticatorCredential
	     extends  DBEntityForModelObject<DBPrimaryKeyForModelObject>,
	              DBEntityHasModelObjectDescriptor {

	public String getUserOid();
	public void setUserOid(final String userOid);

	public String getCredentialId();
	public void setCredentialId(final String Id);
}
