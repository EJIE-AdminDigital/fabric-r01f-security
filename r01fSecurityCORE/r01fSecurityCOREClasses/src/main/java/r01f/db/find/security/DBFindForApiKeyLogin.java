package r01f.db.find.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyLogin;
import r01f.db.entities.security.DBEntityForApiKeyLogin;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.interfaces.FindServicesForModelObject;
import r01f.util.types.Strings;

/**
 * Persistence layer for Api Key Login.
 */
@Slf4j
public class DBFindForApiKeyLogin<L extends ApiKeyLogin,
								  DB extends DBEntityForApiKeyLogin>
	 extends DBFindForLoginBase<L,DB>
  implements FindServicesForModelObject<LoginOID,L>,
  			 FindServicesForApiKeyLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForApiKeyLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
								final DBModuleConfig dbCfg,
							    final EntityManager entityManager,
							    final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	EXTENSION METHOD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public FindResult<L> findByOwnerOid(final SecurityContext securityContext,
										final UserOID ownerOid) {
		log.info(" ..find api key logins by {}", ownerOid );
		TypedQuery<DB> query = this.getEntityManager()
								  .createQuery(Strings.customized("SELECT entity " +
																    "FROM {} entity " +
																   "WHERE entity._userOid = :ownerOid",
																  _DBEntityType.getSimpleName()),
											  _DBEntityType);
		query.setParameter("ownerOid",ownerOid.asString());
		Collection<DB> entities = query.getResultList();
		FindResult<L> outEntities = FindResultBuilder.using(securityContext)
										 	  .on(_modelObjectType)
										 	  .foundDBEntities(entities)
										 	  .transformedToModelObjectsUsing(this);
		return outEntities;
	}
	@Override
	public FindResult<L> findByLoginId(final SecurityContext securityContext,
									   final LoginID loginId) {
		TypedQuery<DB> query = this.getEntityManager()
								  .createQuery(Strings.customized("SELECT entity " +
																    "FROM {} entity " +
																   "WHERE entity._loginId = :loginId",
																  _DBEntityType.getSimpleName()),
											  _DBEntityType);
		query.setParameter("loginId",loginId.asString());
		Collection<DB> entities = query.getResultList();
		FindResult<L> outEntities = FindResultBuilder.using(securityContext)
										 	  .on(_modelObjectType)
										 	  .foundDBEntities(entities)
										 	  .transformedToModelObjectsUsing(this);
		return outEntities;
	}
}
