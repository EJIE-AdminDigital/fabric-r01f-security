package r01f.db.transfer.security.otp.persistence;


import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.otp.persistence.DBEntityForOTPTask;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.services.core.persistence.db.DBEntity;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@RequiredArgsConstructor
public class OTPTaskToDBEntityTransfer
  implements TransfersModelObjectStateToDBEntity<OTPTask,DBEntityForOTPTask> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final OTPTask modelObj,
												 final DBEntityForOTPTask dbEntity) {
		// .. the task oid
		dbEntity.setOid(modelObj.getOid() != null ? modelObj.getOid().asString()
												  : null);
		// ...the app code
		dbEntity.setAppCode(modelObj.getAppCode() != null ? modelObj.getAppCode().asString()
														  : null);
		dbEntity.setAppComponent(modelObj.getAppComponent() != null ? modelObj.getAppComponent().asString()
																	: null);
		dbEntity.setSecret(modelObj.getSecret() != null ? modelObj.getSecret().asString()
														: null);
		dbEntity.setRemainingRetryNum(modelObj.getRemainingRetryNum());

		dbEntity.setContactMean(modelObj.getContactMean() != null ? modelObj.getContactMean().asString()
													  			  : null);
		dbEntity.setContactMeanType(modelObj.getContactMean() != null ? modelObj.getContactMean().getType()
															  		  : null);
		
		dbEntity.setExpiresAt(modelObj.getExpiresAt() != null ? modelObj.getExpiresAt()
															  : null);
	}
}
