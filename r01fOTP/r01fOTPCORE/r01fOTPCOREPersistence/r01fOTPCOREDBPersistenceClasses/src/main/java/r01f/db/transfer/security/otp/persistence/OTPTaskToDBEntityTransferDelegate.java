package r01f.db.transfer.security.otp.persistence;


import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.otp.persistence.DBEntityForOTPTask;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.ModelObject;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.services.core.persistence.db.DBEntity;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@RequiredArgsConstructor
public class OTPTaskToDBEntityTransferDelegate
  implements TransfersModelObjectStateToDBEntity<OTPTask,DBEntityForOTPTask>,
			   TransformsDBEntityIntoModelObject<DBEntityForOTPTask,OTPTask> {
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

		dbEntity.setPhone(modelObj.getPhone() != null ? modelObj.getPhone().asString()
													  : null);
		dbEntity.setEmail(modelObj.getEmail() != null ? modelObj.getEmail().asString()
													  : null);
		dbEntity.setExpirationDate(modelObj.getExpirationDate() != null ? modelObj.getExpirationDate()
																		: null);
	}
	@Override
	public OTPTask dbEntityToModelObject(final SecurityContext securityContext,
										 final DBEntityForOTPTask dbEntity) {
		OTPTask modelObj = new OTPTask();

		modelObj.setOid(OTPTaskOID.forId(dbEntity.getOid()));

		modelObj.setAppCode(dbEntity.getAppCode() != null ? AppCode.forId(dbEntity.getAppCode()) 
														  : null);
		modelObj.setAppComponent(dbEntity.getAppComponent() != null ? AppComponent.forId(dbEntity.getAppComponent())
																	: null);
		modelObj.setSecret(Password.forId(dbEntity.getSecret()));
		modelObj.setRemainingRetryNum(dbEntity.getRemainingRetryNum());

		modelObj.setPhone(dbEntity.getPhone() != null ? Phone.create(dbEntity.getPhone())
													  : null);
		modelObj.setEmail(dbEntity.getEmail() != null ? EMail.create(dbEntity.getEmail())
													  : null);
		
		modelObj.setExpirationDate(dbEntity.getExpirationDate() != null ? dbEntity.getExpirationDate()
																		: null);
		
		return modelObj;
	}
}
