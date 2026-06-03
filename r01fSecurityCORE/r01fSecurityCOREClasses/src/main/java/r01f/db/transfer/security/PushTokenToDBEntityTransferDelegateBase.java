package r01f.db.transfer.security;

import com.google.common.reflect.TypeToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.DBEntityForPushToken;
import r01f.model.ModelObject;
import r01f.model.security.pushtoken.PushToken;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@RequiredArgsConstructor
public class PushTokenToDBEntityTransferDelegateBase<T extends PushToken,DB extends DBEntityForPushToken>
  implements TransfersModelObjectStateToDBEntity<T,DB>,
  		     TransformsDBEntityIntoModelObject<DB,T> {	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter final Marshaller _marshaler;
	@Getter final Class<T> _modelObjType;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final T modelObj,
												 final DB dbEntity) {
		dbEntity.setOid(modelObj.getOid() != null ? modelObj.getOid().asString() : null);
		
		
		dbEntity.setPushTokenId(modelObj.getPushTokenId() != null ? modelObj.getPushTokenId().asString() : null);
		dbEntity.setPushTokenIdType(modelObj.getPushTokenId().getTypeId() != null ? modelObj.getPushTokenId().getTypeId().asString() : null);
		
		
		dbEntity.setSystemId(modelObj.getSystemId() != null ? modelObj.getSystemId().asString() : null);
		dbEntity.setToken(modelObj.getToken() != null ? modelObj.getToken().asString() : null);
		
		dbEntity.setPlataform(modelObj.getPlataform().getCode());
		
	}
	@Override
	public T dbEntityToModelObject(final SecurityContext securityContext,
			                       final DB dbEntity) {
		
		T token = _marshaler.forReading().fromXml(dbEntity.getDescriptor(), TypeToken.of(_modelObjType));
		/*if (dbEntity.getSystemId() != null) {
			token.setSystemId(SystemID.forId(dbEntity.getSystemId()));
		}		
		if (dbEntity.getPhone() != null) {
			token.setPhone(Phone.create(dbEntity.getPhone()));
		}
		if (dbEntity.getToken() != null) {
			token.setToken(SecurityToken.forId(dbEntity.getToken()));
		} else {
			log.error(" [Error ]The db token registry {} has NO token. " +
					  "			This a serious mistake and could respond to a business error {}>> Othercase this registry should be DELETED ",
					dbEntity.getOid());
		}
		token.setOid(PhoneTokenOID.forId(dbEntity.getOid()));*/		
		return token;
	}
}
