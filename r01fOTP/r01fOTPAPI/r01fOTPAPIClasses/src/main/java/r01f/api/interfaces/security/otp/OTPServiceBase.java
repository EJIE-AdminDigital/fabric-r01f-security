package r01f.api.interfaces.security.otp;

import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

/**
 * Usage of base with some task loading services
  * </pre>
 */

public interface OTPServiceBase   
	     extends ServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
// TASK LOADERS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads a task with the given oid
	 * @param taskOid
	 * @return
	 */
	public OTPTask getTaskByOID(final SecurityContext securityContext,
			                    final OTPTaskOID taskOid);
	/**
	 * Loads a task with the given phone
	 * @param appCode
	 * @param appComponent
	 * @param phone
	 * @return
	 */
	public OTPTask getTaskByPhone(final SecurityContext securityContext,
			                      final AppCode appCode,final AppComponent appComponent,
							   	  final Phone phone);
	/**
	 * Loads a task with the given email
	 * @param appCode
	 * @param appComponent
	 * @param eMail
	 * @return
	 */
	public OTPTask getTaskByEMail(final SecurityContext securityContext,
			                      final AppCode appCode,final AppComponent appComponent,
							   	  final EMail eMail);
}
