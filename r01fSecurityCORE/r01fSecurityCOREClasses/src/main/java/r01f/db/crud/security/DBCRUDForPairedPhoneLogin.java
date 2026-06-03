package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLogin;
import r01f.db.entities.security.DBEntityForPairedPhoneLogin;
import r01f.db.transfer.security.PairedPhoneLoginTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class DBCRUDForPairedPhoneLogin<L extends PairedPhoneLogin,
                                                DB extends DBEntityForPairedPhoneLogin >
	 extends DBCRUDForLoginBase<L, DB>
  implements CRUDServicesForPairedPhoneLogin<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForPairedPhoneLogin(final Class<L> modelObjectType,
			                         final Class<DB> dbEntityType,
								     final DBModuleConfig dbCfg,
								     final EntityManager entityManager,
								     final Marshaller marshaller,
								     final PairedPhoneLoginTransferToDBBase<L,DB> modelObjToDBEntity) {
		super(modelObjectType,
			  dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller,
			  modelObjToDBEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS.
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							    final Phone phone) {
		log.debug("DBCRUDForPairedPhoneLogin.loadBy login={} ", phone);
		
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT entity " +
																		  "FROM {} entity " +
																		 "WHERE entity._phone = :phone",
																		_DBEntityType.getSimpleName()),
													 _DBEntityType)
									.setParameter("phone",phone.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _buildCRUDResultFor(securityContext,
								   dbEntity);
	}
}
