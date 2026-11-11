package r01f.hz.crud.base.security.otp.persistence;

import java.time.Instant;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.cache.DistributedCacheService;
import r01f.model.PersistableModelObject;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceOperationExecError;
import r01f.model.persistence.PersistenceOperationExecOK;
import r01f.model.persistence.PersistenceOperationResult;
import r01f.model.persistence.PersistenceServiceErrorTypes;
import r01f.model.security.context.SecurityContext;
import r01f.model.services.COREServiceMethod;
import r01f.oids.PersistableObjectOID;
import r01f.util.types.Strings;
import r01f.util.types.datetime.DateTimeMarshaller;

/**
 * Base type for every persistence layer type
 * @param <O>
 * @param <M>
 * @param <PK>
 * @param <DB>
 */
@Slf4j
@Accessors(prefix="_")
public  abstract class HZBaseForModelObjectBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>>  {	
////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final DistributedCacheService _cacheService;
	/**
	 * The model object's type
	 */
	@Getter protected final Class<M> _modelObjectType;	
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public HZBaseForModelObjectBase(final Class<M> modelObjectType,
			                        final DistributedCacheService cacheService) {
		_cacheService = cacheService;
		_modelObjectType = modelObjectType;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected PersistenceOperationResult<Boolean> doCheckExistence(final SecurityContext securityContext,
								   								   final O oid) {			
		boolean exists = _cacheService.getOrCreateCacheFor(_modelObjectType).containsKey(oid);
		// Compose the PersistenceOperationResult object
		PersistenceOperationResult<Boolean> outResult = null;
		if (! exists ) {
			outResult = new PersistenceOperationExecOK<>(COREServiceMethod.named("exists"),
														 false);	// does NOT exists
		} else {
			outResult = new PersistenceOperationExecOK<>(COREServiceMethod.named("exists"),
														 true);
		} 
		return outResult;
	}
	protected PersistenceOperationResult<Instant> lastUpdateTime(final SecurityContext securityContext,
								   							  	 final O oid) {
		//.. 1. Check existence		
		if ( doCheckExistence(securityContext,oid).hasFailed() ) {
			      return  new PersistenceOperationExecError<>(COREServiceMethod.named("lastUpdateDate"),
			    		  									 // client requested entity was NOT fount
			    		  									  PersistenceServiceErrorTypes.ENTITY_NOT_FOUND,
			    		  									  Strings.customized("Could NOT find a {} entity with oid={} when trying to get the last update date",
			    		  										                 _modelObjectType,oid));
		}
		
	    //.. 2. Load last update time		
		long lastUpdateTIme = _cacheService.getOrCreateCacheFor(_modelObjectType).lastUpdateTime(oid);		
		return new PersistenceOperationExecOK<>(COREServiceMethod.named("lastUpdateDate"), DateTimeMarshaller.instantFromEPOCHMillis(lastUpdateTIme));		
	
	}
	@SuppressWarnings("unused")
	protected PersistenceOperationResult<Boolean> doTouch(final SecurityContext securityContext,
								   						  final O oid,final Instant date) {
		//.. 1. Check existence		
		if ( doCheckExistence(securityContext,oid).hasFailed() ) {
			     return  new PersistenceOperationExecError<>(COREServiceMethod.named("touch"),
			    		  										// client requested entity was NOT fount
			    		  										PersistenceServiceErrorTypes.ENTITY_NOT_FOUND,
			    		  										Strings.customized("Could NOT find a {} entity with oid={} when trying to get the last update date",
			    		  										_modelObjectType,oid));
		}
		// 2. Touch, just at current time. No way to do 'a touch' in a given data, passed date will be ignored.,
		 M touchedObject = _cacheService.getOrCreateCacheFor(_modelObjectType).touch(oid);
		 if (touchedObject == null) {
			    return  new PersistenceOperationExecError<>(COREServiceMethod.named("touch"),
															// client requested entity was NOT fount
															PersistenceServiceErrorTypes.SERVER_ERROR,
															Strings.customized(" Some error ocurred touching object {}",
															_modelObjectType,oid));
		 }		 
		 return  new PersistenceOperationExecOK<>(COREServiceMethod.named("touch"),
				                                  true);
	}
	protected CRUDResult<M> doLoad(final SecurityContext securityContext,
			                       final O oid) {
		// check the oid
		if (oid == null) return CRUDResultBuilder.using(securityContext)
												    .on(_modelObjectType)
											  	    .notLoaded()
											  	    .becauseClientBadRequest("The {} entity's oid cannot be null in order to be loaded",_modelObjectType)
											  	   	.about(oid).build();
		// Load the entity
		M  modelObj = _cacheService.getOrCreateCacheFor(_modelObjectType)
											.get(oid);		
		// Compose the PersistenceOperationResult object
		CRUDResult<M> outEntityLoadResult = null;
		if (modelObj != null) {		
			outEntityLoadResult = CRUDResultBuilder.using(securityContext)
												   .on(_modelObjectType)
												   .loaded()
												   .entity(modelObj);
		} else {
			outEntityLoadResult = CRUDResultBuilder.using(securityContext)
												   .on(_modelObjectType)
												   .notLoaded()
												   .becauseClientRequestedEntityWasNOTFound()
												   .about(oid).build();
			log.warn(outEntityLoadResult.getDetailedMessage());
		}
		return outEntityLoadResult;
	}
}
