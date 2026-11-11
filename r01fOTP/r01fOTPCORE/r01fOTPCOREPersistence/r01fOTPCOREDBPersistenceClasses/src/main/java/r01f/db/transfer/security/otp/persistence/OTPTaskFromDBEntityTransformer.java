package r01f.db.transfer.security.otp.persistence;


import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.otp.persistence.DBEntityForOTPTask;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;
import r01f.services.core.persistence.db.DBEntity;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@RequiredArgsConstructor
public class OTPTaskFromDBEntityTransformer
  implements TransformsDBEntityIntoModelObject<DBEntityForOTPTask,OTPTask> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public OTPTask dbEntityToModelObject(final SecurityContext securityContext,
										 final DBEntityForOTPTask dbEntity) {
		OTPTask modelObj = new OTPTask();

		modelObj.setOid(OTPTaskOID.forId(dbEntity.getOid()));

		modelObj.setAppCode(dbEntity.getAppCode() != null ? AppCode.forId(dbEntity.getAppCode()) 
														  : null);
		modelObj.setAppComponent(dbEntity.getAppComponent() != null ? AppComponent.forId(dbEntity.getAppComponent())
																	: null);
		modelObj.setSecret(Secret.forId(dbEntity.getSecret()));
		modelObj.setRemainingRetryNum(dbEntity.getRemainingRetryNum());

		modelObj.setContactMean(dbEntity.getContactMean() != null ? switch(dbEntity.getContactMeanType() ) {
																		case PHONE -> Phone.of(dbEntity.getContactMean());
																		case EMAIL -> EMail.of(dbEntity.getContactMean());
																		default -> throw new IllegalStateException("Unknown contact mean type " + dbEntity.getContactMeanType());
																	}
													  			  : null);
		
		modelObj.setExpiresAt(dbEntity.getExpiresAt() != null ? dbEntity.getExpiresAt()
															  : null);
		
		return modelObj;
	}
}
