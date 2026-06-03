package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.google.CRUDServicesForGoogleLogin;
import r01f.db.entities.security.DBEntityForGoogleLogin;
import r01f.db.transfer.security.GoogleLoginTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.google.GoogleLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.types.contact.EMail;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public class DBCRUDForGoogleLogin<L extends GoogleLogin,
								  DB extends DBEntityForGoogleLogin>
	 extends DBCRUDForLoginBase<L,DB>
  implements CRUDServicesForGoogleLogin<L>,
  			 TransformsDBEntityIntoModelObject<DB,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForGoogleLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
								final DBModuleConfig dbCfg,
								final EntityManager entityManager,
								final Marshaller marshaller,
								final GoogleLoginTransferToDBBase<L,DB> modelObjToDBEntity) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller,
			  modelObjToDBEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final EMail email) {
		log.debug("DBCRUDForGoogleLogin.loadBy login={} ", email);
		
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT entity " +
																		  "FROM {} entity " +
																		 "WHERE entity._email = :email",
																		_DBEntityType.getSimpleName()),
													 _DBEntityType)
									.setParameter("email",email.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _buildCRUDResultFor(securityContext,
								   dbEntity);
	}
}
