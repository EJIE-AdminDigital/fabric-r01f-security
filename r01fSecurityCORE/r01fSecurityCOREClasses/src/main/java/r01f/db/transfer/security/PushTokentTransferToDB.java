package r01f.db.transfer.security;

import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.DBEntityForPushTokenBase;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@RequiredArgsConstructor
public class PushTokentTransferToDB<T extends IsPushToken,DB extends DBEntityForPushTokenBase>
     extends SecurityObjectTransferToDBBase<T,DB> {	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final T modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, 
											   modelObj,dbEntity);
		
		dbEntity.setOid(modelObj.getOid() != null ? modelObj.getOid().asString() : null);
		
		dbEntity.setSourceSystemId(modelObj.getSourceSystemId() != null ? modelObj.getSourceSystemId().asString() : null);
		
		dbEntity.setDestinationId(modelObj.getDestinationId() != null ? modelObj.getDestinationId().asString() : null);
		dbEntity.setDestinationType(modelObj.getDestinationId() != null ? modelObj.getDestinationId().getType() : null);
		
		dbEntity.setDestinationPlatform(modelObj.getDestinationPlatform());
		
		dbEntity.setToken(modelObj.getToken() != null ? modelObj.getToken().asString() : null);
	}
}
