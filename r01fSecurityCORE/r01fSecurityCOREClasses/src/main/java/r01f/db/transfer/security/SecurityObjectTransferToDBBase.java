package r01f.db.transfer.security;

import r01f.db.entities.security.IsDBEntityForSecurityObject;
import r01f.model.security.IsSecurityPersistableObject;
import r01f.model.security.context.SecurityContext;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;

public abstract class SecurityObjectTransferToDBBase<M extends  IsSecurityPersistableObject<?>,DB extends IsDBEntityForSecurityObject>
		   implements TransfersModelObjectStateToDBEntity<M,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityObjectTransferToDBBase() {
		// default no-args constructor
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final M modelObj,final DB dbEntity) {
		dbEntity.setOid(modelObj.getOid() != null ? modelObj.getOid().asString() : null);
	}
}

