package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.db.entities.security.DBEntityForLogin;
import r01f.db.transfer.security.SecurityObjectTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class DBCRUDForLoginBase<L extends Login,
						  		         DB extends DBEntityForLogin>
	   		  extends DBCRUDForModelObjectBase<LoginOID,L,
	 								  		   DBPrimaryKeyForModelObject,DB>
  		   implements CRUDServicesForLogin<L>,
  			   		  TransformsDBEntityIntoModelObject<DB,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SecurityObjectTransferToDBBase<L,DB> _transformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForLoginBase(final Class<L> modelObjectType,final Class<DB> dbEntityType,
							  final DBModuleConfig dbCfg,
							  final EntityManager entityManager,
							  final Marshaller marshaller,
							  final SecurityObjectTransferToDBBase<L,DB> modelObjToDBEntity) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_transformer = modelObjToDBEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final L modelObj,final DB dbEntity) {
		_transformer.setDBEntityFieldsFromModelObject(securityContext,
													  modelObj,dbEntity);
	}
	@Override
	public L dbEntityToModelObject(final SecurityContext securityContext,
								   final DB dbEntity) {
		// use the descriptor to convert to a model object
		L out = super.dbEntityToModelObject(securityContext,
											dbEntity);
		return out;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		log.debug("DBCRUDForLogin ( {} ) .loadBy userOid={} ",
				  _DBEntityType, userOid.asString());
		TypedQuery<DB> query = this.getEntityManager()
									 .createQuery(Strings.customized("SELECT entity " +
																	   "FROM {} entity " +
																	  "WHERE entity._userOid = :userOid ",
																	 _DBEntityType.getSimpleName()),
												_DBEntityType)
									 .setParameter("userOid",userOid.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _buildCRUDResultFor(securityContext,
								   dbEntity);
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		log.debug("DBCRUDForLogin ( {} ) .loadBy loginId={} ",
				  _DBEntityType, login.asString());
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT entity " +
																		  "FROM {} entity " +
																		 "WHERE entity._loginId = :loginId",
																		_DBEntityType.getSimpleName()),
													 _DBEntityType)
									.setParameter("loginId",login.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _buildCRUDResultFor(securityContext,
								   dbEntity);
	}
	protected CRUDResult<L> _buildCRUDResultFor(final SecurityContext securityContext,
											  final DB dbEntity) {
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<L> outCRUDResult = CRUDResultBuilder.using(securityContext)
												 .on(_modelObjectType)
												 .loaded()
												 .dbEntity(dbEntity)
												 .transformedToModelObjectUsing(this);
		return outCRUDResult;
	}
}
