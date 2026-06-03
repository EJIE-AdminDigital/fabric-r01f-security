package r01f.hz.crud.base.security.otp.persistence;

import java.time.Instant;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.cache.DistributedCacheService;
import r01f.exceptions.Throwables;
import r01f.guids.PersistableObjectOID;
import r01f.model.PersistableModelObject;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceOperationResult;
import r01f.model.persistence.PersistencePerformedOperation;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.callback.spec.COREServiceMethodCallbackSpec;
import r01f.services.delegate.security.otp.persistence.base.hz.CRUDHazelcastServicesForModelObjectDelegateBase;
import r01f.services.interfaces.CRUDServicesForModelObject;

/**
 * Base type for every persistence layer type
 * @param <O>
 * @param <M>
 * @param <PK>
 * @param <DB>
 * 
 */
@Slf4j
@Accessors(prefix="_")
public  class HZCRUDForModelObjectBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>>		
	extends HZBaseForModelObjectBase<O,M>
	       implements CRUDServicesForModelObject<O,M> {	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
////////////////////////////////////////////////////////////////////////////////////////
    public HZCRUDForModelObjectBase(final Class<M> modelObjectType, 
    								final DistributedCacheService cacheService) {
		super(modelObjectType, cacheService);		
	}    
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public PersistenceOperationResult<Boolean> exists(final SecurityContext securityContext, 
													  final O oid) {
		return this.doCheckExistence(securityContext,
				                     oid);
	}

	@Override
	public PersistenceOperationResult<Instant> getLastUpdateDate(final SecurityContext securityContext,
			                                                  final O oid) {		
		return this.lastUpdateTime(securityContext, oid);
	}

	@Override
	public CRUDResult<M> load(final SecurityContext securityContext, 
			                  final O oid) {	
		return this.doLoad(securityContext, oid);
	}

	@Override
	public CRUDResult<M> create(final SecurityContext securityContext, 
			                    final M modelObj) {
		return this.doCreateOrUpdateEntity(securityContext,
                                           modelObj,
                                           PersistenceRequestedOperation.CREATE);// it's a create
	}

	@Override
	public CRUDResult<M> create(final SecurityContext securityContext,
			                    final M modelObj,
			                    final COREServiceMethodCallbackSpec callbackSpec) {
		throw new IllegalStateException(Throwables.message("Implemented at service level (see {}",
                                                        CRUDHazelcastServicesForModelObjectDelegateBase.class));
	}

	@Override
	public CRUDResult<M> update(final SecurityContext securityContext, 
			                    final M modelObj) {
		return this.doCreateOrUpdateEntity(securityContext,
				                           modelObj,
			  	                           PersistenceRequestedOperation.UPDATE);// it's an update
	}

	@Override
	public CRUDResult<M> update(final SecurityContext securityContext, M modelObj,
			                    final COREServiceMethodCallbackSpec callbackSpec) {
		throw new IllegalStateException(Throwables.message("Implemented at service level (see {}",
                                                                 CRUDHazelcastServicesForModelObjectDelegateBase.class));
	}

	@Override
	public PersistenceOperationResult<Boolean> touch(final SecurityContext securityContext, final  O oid, 
			                                         final Instant date) {
		return this.doTouch(securityContext, oid, date);
	}

	@Override
	public CRUDResult<M> delete(final SecurityContext securityContext, 
							    final O oid) {		
		return this.doDeleteEntity(securityContext,
								   oid);
	}

	@Override
	public CRUDResult<M> delete(final SecurityContext securityContext, 
			                    final O oid, final COREServiceMethodCallbackSpec callbackSpec) {
		throw new IllegalStateException(Throwables.message("Implemented at service level (see {}",
				                                              CRUDHazelcastServicesForModelObjectDelegateBase.class));
	}
/////////////////////////////////////////////////////////////////////////////////////////
	protected CRUDResult<M> doCreateOrUpdateEntity(final SecurityContext securityContext,
												   final M modelObj,
												   final PersistenceRequestedOperation requestedOp ) {	
		log.warn(" ...doCreateOrUpdateEntity  ");
		M previousModelObject = null;
		
		// [0]: Try to find a previously existing entity	
		if (modelObj.getOid() != null) {
			// load if exists a previous version if exists	
			previousModelObject = load(securityContext, 
					                   modelObj.getOid()).getOrNull();
			
			// If the entity exists BUT it's supposed to be new... it's an error
			if (previousModelObject != null
			 && requestedOp.is(PersistenceRequestedOperation.CREATE)) {
				return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.notCreated()
										.becauseClientRequestedEntityAlreadyExists()
										 		.about(modelObj).build();
			}
		} else if (requestedOp == PersistenceRequestedOperation.UPDATE) {
			throw new IllegalStateException("An UPDATE operation cannot be performed if the object has oid=null");
		}

		// [1]: Depending on the existence of the entity create or update
		PersistencePerformedOperation performedOp = null;		
		if (previousModelObject != null) {
			// Update		
			log.warn("... update operation for model object of oid '{}'" , modelObj.getOid());
			performedOp = PersistencePerformedOperation.UPDATED;
			try {
				 _cacheService.getOrCreateCacheFor(_modelObjectType).replace(modelObj.getOid(), modelObj);
			} catch (final Throwable th ) {
				th.printStackTrace();
				return  CRUDResultBuilder.using(securityContext)
								.on(_modelObjectType)
								.not(requestedOp)
							    .becauseServerError("Cannot UPDATE operation cannot be performed because of {}", 
							    								th.getLocalizedMessage())
							    .build();
			} 
		}
		else {
			// Create
			log.warn("------------------------------------------------------------------------");
			log.warn("... create operation for model object of oid '{}'" , modelObj.getOid());
			log.warn("------------------------------------------------------------------------");
			performedOp = PersistencePerformedOperation.CREATED;
			try {							
				_cacheService.getOrCreateCacheFor(_modelObjectType).put(modelObj.getOid(), modelObj);
			} catch (final Throwable th ) {
				th.printStackTrace();
				return  CRUDResultBuilder.using(securityContext)
								.on(_modelObjectType)
								.not(requestedOp)
							    .becauseServerError("Cannot CREATE operation cannot be performed because of {}", 
							    								th.getLocalizedMessage())
							    .build();
				
			}			
		}
		// [2]: Load created or updated : theoretically If the map previously contained a mapping for the key, the old value is replaced by the specified value. 
				//This method returns a clone of the previous value, not the original(identically equal) value previously put into the map. 	
		//	 :: > It seems thar in te CREATION the PREVIOUS VALUE IS null , so null is returned.
		//   ::> To ensure the integrity of the operation, the recently created or updated value will be returned.
		M  updatedOrCreatedModelObject = this.load(securityContext, 
												   modelObj.getOid()).getOrNull();		
		if ( updatedOrCreatedModelObject == null ) {
			return  CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.not(requestedOp)
									    .becauseServerError("Cannot CREATE/UPDATE , operation cannot be performed {}", 
									    						modelObj.getOid())
									    .build();
		}
		// [3]: Return crud result
		return CRUDResultBuilder.using(securityContext)
							   .on(_modelObjectType)
							   .executed(requestedOp,performedOp)
							 		.entity(updatedOrCreatedModelObject);		
	}	
	
	protected CRUDResult<M> doDeleteEntity(final SecurityContext securityContext,
			                               final O oid ) {
		M modelObjectToBeDeleted = null;
		if (oid != null) {
			// load if exists a previous version if exists	
			modelObjectToBeDeleted = load(securityContext, 
					                      oid).getOrNull();
			// If the entity exists BUT it's supposed to be new... it's an error
			if (modelObjectToBeDeleted == null) {
				return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.notCreated()
										.becauseRequiredRelatedEntityWasNOTFound(" required object with oid {} does not exist", oid)
										 		.about(oid).build();
			}
		} else {
			throw new IllegalStateException("An DELETE operation cannot be performed if the object has oid=null");
		}
		
		M deletedObject  = _cacheService.getOrCreateCacheFor(_modelObjectType).getAndRemove(oid);
		if ( deletedObject == null ) {
			return  CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.not(PersistenceRequestedOperation.DELETE)
									    .becauseServerError("Cannot DELETE operation cannot be performed {}", 
									    						oid)
									    .build();
		}
		// [2]: Return crud result
		return CRUDResultBuilder.using(securityContext)
									   .on(_modelObjectType)
									   .executed(PersistenceRequestedOperation.DELETE,PersistencePerformedOperation.DELETED)
									 		.entity(deletedObject);		
		
	}
}