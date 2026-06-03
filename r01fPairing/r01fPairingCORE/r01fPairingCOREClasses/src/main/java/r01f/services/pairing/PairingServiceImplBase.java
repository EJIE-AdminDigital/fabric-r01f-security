package r01f.services.pairing;


import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.pairing.PairingService;
import r01f.eventbus.EventBus;
import r01f.model.security.pairing.PairingRequest;
import r01f.model.security.pairing.PairingResult;
import r01f.model.security.pairing.UnpairingRequest;
import r01f.model.security.pairing.UnpairingResult;
import r01f.model.security.pairing.operations.PairingOperationExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreServicesBase;
import r01f.services.delegates.pairing.PairingServiceDelegateBase;

/**
 * Implements the persistence-related services which in turn are
 * delegated to {@link PairingServiceDelegateBase}
 */
@Singleton
@Accessors(prefix="_")
public abstract class PairingServiceImplBase<P extends PairingRequest,
										     U extends UnpairingRequest>
			  extends CoreServicesBase
		   implements PairingService<P,U>,
		   			  PairingServiceInterfaceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairingServiceImplBase(final ServicesCoreModuleConfig coreCfg,
								  final Marshaller modelObjectsMarshaller,
								  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	@Transactional @SuppressWarnings("unchecked")
	public PairingOperationExecResult<PairingResult> pair(final SecurityContext securityContext,
			                                              final P pairingRequest) {
		return  this.forSecurityContext(securityContext)
						.createDelegateAs(PairingService.class)
							.pair(securityContext,
								  pairingRequest);
	}
	@Override
	@Transactional @SuppressWarnings("unchecked")
	public PairingOperationExecResult<UnpairingResult> unpair(final SecurityContext securityContext,
			                                                  final UnpairingRequest unpairingRequest) {
		return  this.forSecurityContext(securityContext)
						.createDelegateAs(PairingService.class)
							.unpair(securityContext,
									unpairingRequest);
	}
}
