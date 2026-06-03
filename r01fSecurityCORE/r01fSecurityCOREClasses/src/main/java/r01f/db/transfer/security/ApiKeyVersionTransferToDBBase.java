package r01f.db.transfer.security;


import r01f.db.entities.security.DBEntityForApiKeyVersion;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;


public abstract class ApiKeyVersionTransferToDBBase<DB extends DBEntityForApiKeyVersion>
     	   implements TransfersModelObjectStateToDBEntity<ApiKeyVersion,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiKeyVersionTransferToDBBase() {
		// default no-args constructor
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final ApiKeyVersion modelObj,final DB dbEntity) {		
		// code
		dbEntity.setId(modelObj.getId() != null ? modelObj.getId().asString() : null);
		
		// name & description
		dbEntity.setName(modelObj.getName());
		dbEntity.setDescription(modelObj.getDescription());
		
		// active
		dbEntity.setActive(modelObj.isValidNow());
		
		// usage range
		dbEntity.setValidFrom(modelObj.getUsageRange().getLowerBound());
		dbEntity.setValidTo(modelObj.getUsageRange().hasUpperBound() ? modelObj.getUsageRange().getUpperBound()
																	 : null);
	}
}