package r01f.api.interfaces.security.otp;

import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.task.dispatch.IsOTPDispatchRequest;
import r01f.model.security.otp.task.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.task.generation.OTPGenerationRequest;
import r01f.model.security.otp.task.generation.OTPGenerationResponse;
import r01f.model.security.otp.task.validation.IsOTPValidationRequest;
import r01f.model.security.otp.task.validation.OTPValidationResponse;
import r01f.services.interfaces.ExposedServiceInterface;

/**
 * Usage:
 * [0] - Create the {@link OTPService}
 * <pre class='brush:java'>
 *		// a) - Create the OTP task persistence service
 *		Config hzConfig = new Config();
 *		hzConfig.getNetworkConfig().setPort( 5900 )
 *		        .setPortAutoIncrement(false);
 *		DistributedCacheHazelcastConfig cacheCfg = DistributedCacheHazelcastConfig.createFrom(AppCode.forId("r01f"),AppComponent.forId("test"),
 *																				  	 		  hzConfig);
 *		DistributedCacheService cacheService = new DistributedCacheServiceHazelcastImpl(cacheCfg);
 *		OTPTaskPersistenceService taskPersistSrvc = new OTPTaskPersistenceServiceDistributedCacheImpl(cacheService);
 *
 *		// b) - Create the notifier
 *		AWSSNSClient snsCli = AWSSNSClientBuilder.region(Region.EU_WEST_1)
 *									.using(AWS_SNS_ACCESS_KEY,
 *										   AWS_SNS_ACCESS_SECRET)
 *									.build();
 *		NotifierServiceForSMS smsNotifier = new AWSSNSNotifierServices(snsCli);
 *
 *		c) Create the otp service
 *		OTPService otpService = OTPServiceBuilder.persistOTPTasksUsing(taskPersistSrvc)
 *												 .notNotifyingByEmail()
 *												 .useSMSNotifier(smsNotifier)
 *												 .build();
 * </pre>
 *
 * [1] - OTP Generation
 * <pre class='brush:java'>
 *		OTPGenerationRequest genReq = OTPGenerationRequestBuilder.usingSecretOfType(OTPSecretType.ALPHANUMERIC)
 *																 .withLength(10)
 *																 .aliveDuringSeconds(600)
 *																 .allowUnlimitedRetryAttempts()
 *																 .buildFor(AppCode.forId("r01f"),AppComponent.forId("test"));
 *		OTPOperationExecResult<OTPGenerationResponse> genResult = otpSrvc.generate(genReq);
 *		OTPGenerationResponse genResponse = result.getOrThrow();
 * </pre>
 *
 * [2] - OTP Dispatch
 * <pre class='brush:java'>
 *		// a) create a factory that creates the message
 *		OTPSMSDispatchedMessageFactory messageFactory = new OTPSMSDispatchedMessageFactory() {
 *																		@Override
 *																		public String createMessageUsing(final OwnedContactMean<Phone> from,final Phone to,
 *																										 final Password password) {
 *																			return Strings.customized("Your OTP is: {}",
 *																									  password);
 *																		}
 *															 };
 *		OTPDispatchRequestBySMS smsDispatch = OTPDispatchRequestBuilder.forTaskWithOid(genTaskOid)
 *																	   .usingSMS()
 *																	   .from(Phone.of("012"))
 *																	   .to(PHONE)
 *																	   .buildMessageUsing(messageFactory)
 *																	   .buildFor(APP_CODE,APP_COMP);
 *		OTPOperationExecResult<OTPDispatchResponse> dispResult = otpSrvc.dispatch(smsDispatch);
 *		OTPDispatchResponse dispResponse = dispResult.getOrThrow();
 * </pre>
 *
 * [3] - OTP Validate
 * <pre class='brush:java'>
 *		OTPValidationRequest<Phone> validReq = OTPValidationRequestBuilder.using(PHONE)
 *																		  .validate(secret)
 *																		  .buildFor(APP_CODE,APP_COMP);
 *		OTPOperationExecResult<OTPValidationResponse> validResult = otpSrvc.validate(validReq);
 *		OTPValidationResponse validResp = validResult.getOrThrow();
 * </pre>
 */
@ExposedServiceInterface
public interface OTPService
		 extends OTPServiceBase,
				 IsOTPServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
// OTP SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	
	public OTPOperationExecResult<OTPGenerationResponse> generate(final SecurityContext securityContext,
																  final OTPGenerationRequest otpRequest);
	
	public <R extends IsOTPDispatchRequest<?,?,?>> OTPOperationExecResult<OTPDispatchResponse> dispatch(final SecurityContext securityContext,
																									  final R otpRequest);
	
	public <R extends IsOTPValidationRequest<?>> OTPOperationExecResult<OTPValidationResponse> validate(final SecurityContext securityContext,
																									    final R validationReq);
}
