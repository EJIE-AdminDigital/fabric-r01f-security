package r01f.services.delegates.security;



import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.principal.SecurityPrincipalValidator;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.delegates.persistence.ValidatesModelObjectBeforeCreateOrUpdate;
import r01f.types.contact.EMail;
import r01f.validation.ObjectValidationResult;

public abstract class CRUDServicesDelegateForSecurityPrincipalBase<P extends IsSecurityPrincipal>
			  extends CRUDServicesForModelObjectDelegateBase<SecurityPrincipalOID,P>
		   implements CRUDServicesForPrincipal<P>,
		   			  ValidatesModelObjectBeforeCreateOrUpdate<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////

	public CRUDServicesDelegateForSecurityPrincipalBase(final ServicesCoreModuleConfig coreCfg,
										   				final Class<P> modelObjectType,
										   				final CRUDServicesForPrincipal<P> dbCrud,
										   				final Marshaller marshaller,
										   				final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final IdentityProvider identityProviderId,final SecurityPrincipalID principalId) {
		if (identityProviderId == null || principalId == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The [identity provider] and [login] MUST not be null")
									.build();
		}
		CRUDResult<P> outResult = this.getServiceImplAs(CRUDServicesForPrincipal.class)
									  .loadBy(securityContext,
											  identityProviderId,principalId);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final EMail principalEmail) {
		if (principalEmail == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The [email] MUST not be null")
									.build();
		}
		
		return this.getServiceImplAs(CRUDServicesForPrincipal.class)
				   .loadBy(securityContext,
						   principalEmail);
	}

/////////////////////////////////////////////////////////////////////////////////////////
//	PARAMS VALIDATION ON CREATION / UPDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<P> validateModelObjBeforeCreateOrUpdate(final SecurityContext securityContext,
																		  final PersistenceRequestedOperation requestedOp,
																		  final P owner) {
		// [1]: Do base validations
		ObjectValidationResult<P> outValid = new SecurityPrincipalValidator<P>().validate(owner);


		// [99]: Return
		return outValid;
	}
}
