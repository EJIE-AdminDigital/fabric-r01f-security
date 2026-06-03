package r01f.services.delegates.pairing;


import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.keystore.CRUDServicesForKeystoreBase;
import r01f.api.interfaces.pairing.PairingService;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.keystore.KeystoreBase;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.model.security.pairing.PairingRequest;
import r01f.model.security.pairing.PairingRequestValidationResult;
import r01f.model.security.pairing.PairingResult;
import r01f.model.security.pairing.UnpairingRequest;
import r01f.model.security.pairing.UnpairingResult;
import r01f.model.security.pairing.operations.PairingOperation;
import r01f.model.security.pairing.operations.PairingOperationExecResult;
import r01f.model.security.pairing.operations.PairingOperationExecResultBuilder;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.ServicesDelegateBase;

/**
 * Service layer delegated type for Pairing Operations
 */
@Slf4j
@Accessors(prefix="_")
public abstract class PairingServiceDelegateBase<P extends PairingRequest,
												 U extends UnpairingRequest,
												 K extends KeystoreBase<KeystoreOID, K>>
			  extends ServicesDelegateBase
		   implements PairingService<P, U> {
	
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Class<P> _pairingRequestClassType;
	protected final CRUDServicesForKeystoreBase<K> _crudServiceForKeystore;	
	protected final FactoryFrom<K, PairingResult>  _factoryFromKeystoreToPairingResult;	
	protected final FactoryFrom<K, UnpairingResult>  _factoryFromKeystoreToUnpairingResult;	

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairingServiceDelegateBase(final ServicesCoreModuleConfig coreCfg,
									  final Class<P> pairingRequestClassType,
									  final FactoryFrom<K,PairingResult>  resultFactory,
									  final FactoryFrom<K,UnpairingResult> unpairingFactory,
									  final CRUDServicesForKeystoreBase<K> crudServiceForKeystore,
									  final EventBus eventBus) {
		super(coreCfg, 
			  eventBus);
		_crudServiceForKeystore  = crudServiceForKeystore;
		_factoryFromKeystoreToPairingResult  = resultFactory;
		_factoryFromKeystoreToUnpairingResult = unpairingFactory;
		_pairingRequestClassType = pairingRequestClassType;
	}

////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
////////////////////////////////////////////////////////////////////////////////////////
	public abstract PairingRequestValidationResult<P> validatePairingRequest(final SecurityContext securityContext,
																			 final P pairingRequest);	
	public abstract CRUDResult<K> doPairing(final SecurityContext securityContext,
											final P pairingRequest);
	public abstract CRUDResult<K> doUnpairing(final SecurityContext securityContext, 
											  final U unpairRequest);
	
////////////////////////////////////////////////////////////////////////////////////////
//  PAIR
////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public PairingOperationExecResult<PairingResult> pair(final SecurityContext securityContext,
														  final P pairingRequest) {	
		 log.warn("....pairing request .....");		
		 PairingRequestValidationResult<P> requestValidation = validatePairingRequest(securityContext,
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
	
	@Override
	public PairingOperationExecResult<UnpairingResult> unpair(final SecurityContext securityContext,
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
