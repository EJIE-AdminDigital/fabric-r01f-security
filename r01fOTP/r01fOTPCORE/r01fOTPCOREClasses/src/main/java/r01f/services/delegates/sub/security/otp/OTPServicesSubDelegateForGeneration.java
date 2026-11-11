package r01f.services.delegates.sub.security.otp;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.operations.OTPOperation;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.operations.OTPOperationExecResultBuilder;
import r01f.model.security.otp.task.generation.OTPGenerationRequest;
import r01f.model.security.otp.task.generation.OTPGenerationResponse;
import r01f.model.security.otp.task.generation.OTPSecretFactory;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;

@Slf4j
public class OTPServicesSubDelegateForGeneration
	extends OTPServicesSubDelegateBase {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPServicesSubDelegateForGeneration(final ServicesCoreModuleConfig coreConfig,
											   final Marshaller modelObjectsMarshaller, final EventBus eventBus,
											   final CRUDServicesForOTPTask taskPersistenceService) {
		super(coreConfig, modelObjectsMarshaller, eventBus, taskPersistenceService);
	}

/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPOperationExecResult<OTPGenerationResponse> generate(final SecurityContext scx,
																  final OTPGenerationRequest req) {
		if (req.getAppCode() == null) {
			return OTPOperationExecResultBuilder.notExecuted(OTPOperation.GENERATE)
												.becauseClientBadRequest("Check the otp request: NO appCode provided");
		}
		//////////// 1  Generate OTP Task
		OTPTask task = new OTPTask();		// new task oid
		task.setAppCode(req.getAppCode());
		task.setAppComponent(req.getAppComponent());
		task.setRemainingRetryNum(req.getMaxRetries());

		// Generate the OTP VALUE
		if  ( req.getFixedValue() != null) {
			log.info("[ Using  externaly provided fixed value for OTP ]");
			task.setSecret(req.getFixedValue());
		} else {
			task.setSecret(OTPSecretFactory.createSecret(req.getSecretType(),
														 req.getSecretLength()));
		}
		//////////// 2  Store at persistence layer
		CRUDResult<OTPTask> crudResult = _taskPersistenceService.create(scx,
																        task,
																        req.getTTLSeconds(),TimeUnit.SECONDS);
		OTPTask createdTask = crudResult.getOrThrow(); 
		//////// 3 Generate Response
		log.warn("[OTPGenerator] > Created otp task with oid={} for from app/component={}/{}",createdTask.getOid(),
																							  req.getAppCode(),req.getAppComponent());

		OTPGenerationResponse response = new OTPGenerationResponse(createdTask.getOid());
		return OTPOperationExecResultBuilder.executed(OTPOperation.GENERATE)
											.returning(response);
	}

}
