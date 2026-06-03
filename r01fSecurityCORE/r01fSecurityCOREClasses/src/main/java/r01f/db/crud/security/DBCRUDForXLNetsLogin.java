package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.xlnets.CRUDServicesForXLNetsLogin;
import r01f.db.entities.security.DBEntityForXLNetsLogin;
import r01f.db.transfer.security.XLNetsLoginTransferToDBBase;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public class DBCRUDForXLNetsLogin<L extends XLNetsLogin,
								  DB extends DBEntityForXLNetsLogin>
	 extends DBCRUDForLoginBase<L,DB>
  implements CRUDServicesForXLNetsLogin<L>,
  			 TransformsDBEntityIntoModelObject<DB,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForXLNetsLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
								final DBModuleConfig dbCfg,
								final EntityManager entityManager,
								final Marshaller marshaller,
								final XLNetsLoginTransferToDBBase<L,DB> modelObjToDBEntity) {
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
								final WorkPlaceCode workplaceCode) {
		log.debug("DBCRUDForLogin ( {} ) .loadBy workplaceCode={} ",
				  _DBEntityType, workplaceCode.asString());
		TypedQuery<DB> query = this.getEntityManager()
									 .createQuery(Strings.customized("SELECT entity " +
																	   "FROM {} entity " +
																	  "WHERE entity._workPlace = :workPlace ",
																	 _DBEntityType.getSimpleName()),
												_DBEntityType)
									 .setParameter("workPlace",workplaceCode.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _buildCRUDResultFor(securityContext,
								   dbEntity);
	}
}
