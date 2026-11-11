package r01f.services.delegates.pairing;


import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.keystore.CRUDServicesForKeystoreBase;
import r01f.api.interfaces.pairing.PairingService;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.keystore.KeystoreBase;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.model.security.pairing.IsPairingRequest;
import r01f.model.security.pairing.IsPairingRequestValidationResult;
import r01f.model.security.pairing.IsPairingResult;
import r01f.model.security.pairing.IsUnpairingRequest;
import r01f.model.security.pairing.IsUnpairingResult;
import r01f.model.security.pairing.operations.IsPairingOperationExecResult;
import r01f.model.security.pairing.operations.PairingOperation;
import r01f.model.security.pairing.operations.PairingOperationExecResultBuilder;
import r01f.patterns.FactoryFrom;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.ServicesDelegateBase;

/**
 * Service layer delegated type for Pairing Operations
 */
@Slf4j
@Accessors(prefix="_")
public abstract class PairingServiceDelegateBase<P extends IsPairingRequest,U extends IsUnpairingRequest,
												 K extends KeystoreBase<KeystoreOID, K>>
			  extends ServicesDelegateBase
		   implements PairingService<P,U> {
	
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Class<P> _pairingRequestClassType;
	protected final CRUDServicesForKeystoreBase<K> _crudServiceForKeystore;	
	protected final FactoryFrom<K,IsPairingResult> _factoryFromKeystoreToPairingResult;	
	protected final FactoryFrom<K,IsUnpairingResult> _factoryFromKeystoreToUnpairingResult;	

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairingServiceDelegateBase(final ServicesCoreModuleConfig coreCfg,
									  final Class<P> pairingRequestClassType,
									  final FactoryFrom<K,IsPairingResult>  resultFactory,
									  final FactoryFrom<K,IsUnpairingResult> unpairingFactory,
									  final CRUDServicesForKeystoreBase<K> crudServiceForKeystore,
									  final EventBus eventBus) {
		super(coreCfg, 
			  eventBus);
		_crudServiceForKeystore = crudServiceForKeystore;
		_factoryFromKeystoreToPairingResult = resultFactory;
		_factoryFromKeystoreToUnpairingResult = unpairingFactory;
		_pairingRequestClassType = pairingRequestClassType;
	}

////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
////////////////////////////////////////////////////////////////////////////////////////
	public abstract IsPairingRequestValidationResult<P> validatePairingRequest(final SecurityContext securityContext,
																			   final P pairingRequest);	
	public abstract CRUDResult<K> doPairing(final SecurityContext securityContext,
											final P pairingRequest);
	public abstract CRUDResult<K> doUnpairing(final SecurityContext securityContext, 
											  final U unpairRequest);
	
////////////////////////////////////////////////////////////////////////////////////////
//  PAIR
////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public IsPairingOperationExecResult<IsPairingResult> pair(final SecurityContext securityContext,
														  	  final P pairingRequest) {	
		 log.warn("....pairing request .....");		
		 IsPairingRequestValidationResult<P> requestValidation = validatePairingRequest(securityContext,
																					  pairingRequest);
		if (requestValidation.isNOTValid() ) {
			log.error("... pairing request not valid");
			return PairingOperationExecResultBuilder.notExecuted(PairingOperation.PAIR)
													.becauseClientBadRequest("The pairing request cannot be validated {}", 
																			 requestValidation.asNOKValidationResult().getReason());
		}
		log.warn("... pairing request  valid");
		CRUDResult<K> crudResultForKeystore = doPairing(securityContext,
														pairingRequest);
		if (crudResultForKeystore.hasFailed() ) {
			return PairingOperationExecResultBuilder.notExecuted(PairingOperation.PAIR)
													.becauseClientBadRequest("The pairing request cannot be executed because : {}",
																			 crudResultForKeystore.asCRUDError().debugInfo() );
		}
		return PairingOperationExecResultBuilder.executed(PairingOperation.PAIR)
												.returning(_factoryFromKeystoreToPairingResult.from(crudResultForKeystore.getOrThrow()));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UNPAIR
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public IsPairingOperationExecResult<IsUnpairingResult> unpair(final SecurityContext securityContext,
															  	  final U unpairingRequest) {
		log.warn("....unpairing request....");
		CRUDResult<K> crudResultForKeystore = doUnpairing(securityContext, unpairingRequest);
		if (crudResultForKeystore.hasFailed() ) {
			return PairingOperationExecResultBuilder.notExecuted(PairingOperation.UNPAIR)
													.becauseClientBadRequest("The pairing request cannot be executed because : {}",
																		     crudResultForKeystore.asCRUDError().debugInfo() );
		}
		return PairingOperationExecResultBuilder.executed(PairingOperation.UNPAIR)
												.returning(_factoryFromKeystoreToUnpairingResult.from(crudResultForKeystore.getOrThrow()));
	}
	
////////////////////////////////////////////////////////////////////////////////////////
// CRUD AS
////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	protected  <C extends CRUDServicesForKeystoreBase<K> > C _crudServiceForKeystoreAs (final Class<C> theClass) {
		return (C) _crudServiceForKeystore;
	}
	
}
