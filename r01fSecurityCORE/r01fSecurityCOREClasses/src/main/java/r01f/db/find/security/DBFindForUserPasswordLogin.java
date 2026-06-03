package r01f.db.find.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.userpassword.FindServicesForUserPasswordLogin;
import r01f.db.entities.security.DBEntityForUserPasswordLogin;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.interfaces.FindServicesForModelObject;

/**
 * Persistence layer User finder.
 */
public class DBFindForUserPasswordLogin<L extends UserPasswordLogin,
						   				DB extends DBEntityForUserPasswordLogin>
	 extends DBFindForLoginBase<L,DB>
  implements FindServicesForModelObject<LoginOID,L>,
  			 FindServicesForUserPasswordLogin<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForUserPasswordLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
									  final DBModuleConfig dbCfg,
									  final EntityManager entityManager,
									  final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected FindResult<L> _buildResultsFromDBEntities(final SecurityContext securityContext,
														final Collection<? extends DB> dbEntities) {
		// Avoid the password hash exposure
		FindResult<L> outResult = super._buildResultsFromDBEntities(securityContext,
																	dbEntities);
		if (outResult.hasSucceeded()) {
			for (L login : outResult.getOrThrow()) {
				login.setPassword(null);
			}
		}
		return outResult;
	}
//	@Override
//	public FindResult<L> findBy(final SecurityContext securityContext,
//								final UserOID userOid) {
//		log.debug("DBFindForUserPasswordLogin.loading login data by userOid={} ",
//				  userOid.asString());
//		String jpql = "SELECT entity " +
//					    "FROM {} entity " +
//					   "WHERE entity._userOid = :userOid ";
//
//		TypedQuery<DB> query = this.getEntityManager()
//									 .createQuery(Strings.customized(jpql,
//																	 _DBEntityType.getSimpleName()),
//												_DBEntityType)
//									 .setParameter("userOid",userOid.asString());
//
//		Collection<DB> entities = query.getResultList();
//		FindResult<L> outEntities = FindResultBuilder.using(securityContext)
//													 .on(_modelObjectType)
//													 .foundDBEntities(entities)
//													 .transformedToModelObjectsUsing(this);
//		return outEntities;
//	}
}
