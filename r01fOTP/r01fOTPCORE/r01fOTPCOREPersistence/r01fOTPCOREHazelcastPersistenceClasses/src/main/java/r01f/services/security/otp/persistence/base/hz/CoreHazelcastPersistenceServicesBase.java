package r01f.services.security.otp.persistence.base.hz;



import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.cache.DistributedCacheService;
import r01f.eventbus.EventBus;
import r01f.objectstreamer.HasMarshaller;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreServicesBase;


@Accessors(prefix="_")
public abstract class CoreHazelcastPersistenceServicesBase
 	   		  extends CoreServicesBase
 	   	   implements HasMarshaller {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * {@link DistributedCacheService} 
	 */
	@Getter protected final DistributedCacheService  _distributedCacheService;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param cfg core config
	 * @param modelObjectsMarshaller annotated with @ModelObjectsMarshaller
	 * @param eventBus
	 * @param distributedCacheService
	 */
	public CoreHazelcastPersistenceServicesBase(final ServicesCoreModuleConfig cfg,
									            final Marshaller modelObjectsMarshaller,
									            final EventBus eventBus,
									            final DistributedCacheService  distributedCacheService) {
		super(cfg,
			  modelObjectsMarshaller,
			  eventBus);
		_distributedCacheService = distributedCacheService;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
}
