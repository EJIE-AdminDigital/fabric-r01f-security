package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.giltza.CRUDServicesForGiltzaLogin;
import r01f.db.entities.security.DBEntityForGiltzaLogin;
import r01f.db.transfer.security.GiltzaLoginTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.types.contact.EMail;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public class DBCRUDForGiltzaLogin<L extends GiltzaLogin,
								  DB extends DBEntityForGiltzaLogin>
	 extends DBCRUDForLoginBase<L,DB>
  implements CRUDServicesForGiltzaLogin<L>,
  			 TransformsDBEntityIntoModelObject<DB,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForGiltzaLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
								final DBModuleConfig dbCfg,
								final EntityManager entityManager,
								final Marshaller marshaller,
								final GiltzaLoginTransferToDBBase<L,DB> modelObjToDBEntity) {
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
		log.debug("DBCRUDForGiltzaLogin.loadBy login={} ", email);
		
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
