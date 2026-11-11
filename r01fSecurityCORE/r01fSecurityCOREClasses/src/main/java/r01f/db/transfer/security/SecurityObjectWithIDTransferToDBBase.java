package r01f.db.transfer.security;

import r01f.db.entities.security.IsDBEntityForSecurityObjectWithID;
import r01f.model.security.IsSecurityPersistableObjectWithID;
import r01f.model.security.context.SecurityContext;

public abstract class SecurityObjectWithIDTransferToDBBase<M extends  IsSecurityPersistableObjectWithID<?,?>,DB extends IsDBEntityForSecurityObjectWithID>
		      extends SecurityObjectTransferToDBBase<M,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityObjectWithIDTransferToDBBase() {
		// default no-args constructor
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final M modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
		dbEntity.setId(modelObj.getId() != null ? modelObj.getId().asString() : null);
	}
}

