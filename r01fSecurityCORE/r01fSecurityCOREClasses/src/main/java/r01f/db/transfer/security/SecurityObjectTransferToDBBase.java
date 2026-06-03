package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForSecurityObject;
import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;

public abstract class SecurityObjectTransferToDBBase<M extends  PersistableSecurityObject<?>,
												 	 DB extends DBEntityForSecurityObject>
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

