package r01f.services.delegates.sub.security.otp;


import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.core.services.notifier.NotifierServiceResponse;
import r01f.eventbus.EventBus;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPNotifierTaskOID;
import r01f.model.security.otp.operations.OTPErrorTypes;
import r01f.model.security.otp.operations.OTPOperation;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.operations.OTPOperationExecResultBuilder;
import r01f.model.security.otp.task.dispatch.IsOTPDispatchRequest;
import r01f.model.security.otp.task.dispatch.OTPDispatchRequestByEMail;
import r01f.model.security.otp.task.dispatch.OTPDispatchRequestBySMS;
import r01f.model.security.otp.task.dispatch.OTPDispatchResponse;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.Secret;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

@Slf4j
public class OTPServicesSubDelegateForDispatch
	 extends OTPServicesSubDelegateBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final NotifierServiceForEMail _mailNotifier;
	protected final NotifierServiceForSMS _smsNotifier;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public OTPServicesSubDelegateForDispatch(final ServicesCoreModuleConfig coreConfig,
											 final Marshaller modelObjectsMarshaller, final EventBus eventBus,
											 final CRUDServicesForOTPTask taskPersistenceService,
											 // Notifiers
											 final NotifierServiceForEMail mailNotifier,final NotifierServiceForSMS smsNotifier) {
		super(coreConfig, 
			  modelObjectsMarshaller, 
			  eventBus, 
			  taskPersistenceService);
		_mailNotifier = mailNotifier;
		_smsNotifier  = smsNotifier;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public <R extends IsOTPDispatchRequest<?,?,?>> OTPOperationExecResult<OTPDispatchResponse> dispatch(final SecurityContext securityContext,
			                                                                                          final R dispatchReq) {
		// [1] - Load the task
		log.warn("[1][OTPDispatcher] > load otp task with oid={}",
				  dispatchReq.getTaskOid());
	    OTPTask task = null;
		try {
			 task =	_taskPersistenceService.load(securityContext,
						                         dispatchReq.getTaskOid()).getOrThrow();

		} catch (final PersistenceException pers) {
			// do nothing
		}
		if (task == null) {
			return OTPOperationExecResultBuilder.notExecuted(OTPOperation.DISPATCH)
											 	.because(OTPErrorTypes.OTP_DISPATCHING_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED,
											 			 "OTP is expired or does not exists!");
		}

		// [2] - Dispatch
		log.warn("[1][OTPDispatcher] > dispatch by {} to={}",
				  dispatchReq.getMedium(),dispatchReq.getTo());
		NotifierServiceResponse<?> notifierResponse = null;
		try {
			Secret otpSecret = task.getSecret();
			if (dispatchReq instanceof OTPDispatchRequestByEMail) {
				if (_mailNotifier == null) {
					log.error("[99][OTPDispatcher] > NO mail notifier configured! cannot dispatch the OTP via EMail!");
					return OTPOperationExecResultBuilder.notExecuted(OTPOperation.DISPATCH)
												 		.because(OTPErrorTypes.SERVER_ERROR,
												 				 "NO mail notifier configured! cannot dispatch the OTP via EMail!");
				}
				OTPDispatchRequestByEMail  dReqByMail =  dispatchReq.as(OTPDispatchRequestByEMail.class);
				notifierResponse = _mailNotifier.notify(dReqByMail.getFrom(),dReqByMail.getTo(),
														() -> dReqByMail.createMessageForSecret(otpSecret));

				if (notifierResponse.hasFailed() ) {
					return OTPOperationExecResultBuilder.notExecuted(OTPOperation.DISPATCH)
					 		.because(OTPErrorTypes.SERVER_ERROR,
					 				notifierResponse.asResponseError().getErrorDetail());
				}
			} else if (dispatchReq instanceof OTPDispatchRequestBySMS) {
				if (_smsNotifier == null) {
					log.error("[99][OTPDispatcher] > NO SMS notifier configured! cannot dispatch the OTP via SMS!");
					return OTPOperationExecResultBuilder.notExecuted(OTPOperation.DISPATCH)
												 		.because(OTPErrorTypes.SERVER_ERROR,
												 				 "NO SMS notifier configured! cannot dispatch the OTP via SMS!");
				}
				OTPDispatchRequestBySMS  dReqBSMS =  dispatchReq.as(OTPDispatchRequestBySMS.class);
				notifierResponse = _smsNotifier.notify(dReqBSMS.getFrom(),dReqBSMS.getTo(),
													   () -> dReqBSMS.createMessageForSecret(otpSecret));

				if (  notifierResponse.hasFailed() ) {
					return OTPOperationExecResultBuilder.notExecuted(OTPOperation.DISPATCH)
					 		.because(OTPErrorTypes.SERVER_ERROR,
					 				notifierResponse.asResponseError().getErrorDetail());
				}
			} else {
				return OTPOperationExecResultBuilder.notExecuted(OTPOperation.DISPATCH)
													.because(OTPErrorTypes.UNKNOWN_METHOD_OF_OTP_DISPATCHING,
															 "Unknown otp dipataching method for " + dispatchReq.getMedium() + " > " + dispatchReq.getClass());
			}
		} catch (Throwable th ) {
			log.error("[99][OTPDispatcher] > Dispath error: {}",
					  th.getMessage(),th);
			return OTPOperationExecResultBuilder.notExecuted(OTPOperation.DISPATCH)
										 		.because(OTPErrorTypes.SERVER_ERROR,
										 				 th);
		}
		log.warn("[2][OTPDispatcher] > OTP SUCCESSFULLY dispatched by {} to={}",
				  dispatchReq.getMedium(),dispatchReq.getTo());

		// Store mail in cache, but before doing so, check if there exists another
		// not expired task with this email; if so, replace te previous task with this one
		log.warn("[3][OTPDispatcher] > Check if there exists any other task for {}={}",
				 dispatchReq.getMedium(),dispatchReq.getTo());

		/// this should be in another transactionl method into persitence /delete if exists
		/// ...otherwhise required to makes trasnacionals loadser methos.
		OTPTask otherWithSameTo  = this.loadTaskBy(securityContext,
											  	   dispatchReq.getAppCode(),dispatchReq.getAppComponent(),
											  	   dispatchReq.getTo());
		if (otherWithSameTo != null) {
			log.info("... a previous non-expired [OTP task] exists for {}={}: remove it!",
					 dispatchReq.getMedium(),dispatchReq.getTo());
			_taskPersistenceService.delete(securityContext,
					                       otherWithSameTo.getOid()).getOrThrow();
		} else {
			log.info("...NO previous non-expired [OTP task] exists for {}={}",
					 dispatchReq.getMedium(),dispatchReq.getTo());
		}

		log.info("[4][OTPDispatcher] > Cache task");

		// Ensure the task has the email or phone info
		if (dispatchReq.getTo() instanceof Phone) {
			Phone phone = dispatchReq.getTo()
									 .asPhone();
			task.setContactMean(phone);
		} else if (dispatchReq.getTo() instanceof EMail) {
			EMail email = dispatchReq.getTo()
									 .asEMail();
			task.setContactMean(email);
		} else if (dispatchReq.getTo() instanceof EMailRFC822Address) {
			EMail email = dispatchReq.getTo()
									 .as(EMailRFC822Address.class)
									 .getEmail();
			task.setContactMean(email);
		} else {
			throw new IllegalArgumentException(dispatchReq.getTo().getClass() + " is NOT an instance of " + EMailRFC822Address.class + " or " + Phone.class);
		}
		_taskPersistenceService.update(securityContext,
				                       task).getOrThrow();

		// Compose Response
		OTPDispatchResponse response = new OTPDispatchResponse(task.getOid(),
			                                                   OTPNotifierTaskOID.forId( notifierResponse.getTaskOid().getId()));	// notifier task oid

		log.info("[99][OTPDispatcher] > Return otp dispatch result: OK");
		return OTPOperationExecResultBuilder.executed(OTPOperation.DISPATCH)
										    .returning(response);
	}
}
