package r01f.services.pairing;


import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.pairing.PairingService;
import r01f.eventbus.EventBus;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pairing.IsPairingRequest;
import r01f.model.security.pairing.IsPairingResult;
import r01f.model.security.pairing.IsUnpairingRequest;
import r01f.model.security.pairing.IsUnpairingResult;
import r01f.model.security.pairing.operations.IsPairingOperationExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreServicesBase;
import r01f.services.delegates.pairing.PairingServiceDelegateBase;

/**
 * Implements the persistence-related services which in turn are
 * delegated to {@link PairingServiceDelegateBase}
 */
@Singleton
@Accessors(prefix="_")
public abstract class PairingServiceImplBase<P extends IsPairingRequest,U extends IsUnpairingRequest>
			  extends CoreServicesBase
		   implements PairingService<P,U>,
		   			  IsPairingServiceInterfaceImpl {
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
	public IsPairingOperationExecResult<IsPairingResult> pair(final SecurityContext securityContext,
			                                              	  final P pairingRequest) {
		return  this.forSecurityContext(securityContext)
						.createDelegateAs(PairingService.class)
							.pair(securityContext,
								  pairingRequest);
	}
	@Override
	@Transactional @SuppressWarnings("unchecked")
	public IsPairingOperationExecResult<IsUnpairingResult> unpair(final SecurityContext securityContext,
			                                                  	  final IsUnpairingRequest unpairingRequest) {
		return  this.forSecurityContext(securityContext)
						.createDelegateAs(PairingService.class)
							.unpair(securityContext,
									unpairingRequest);
	}
}
