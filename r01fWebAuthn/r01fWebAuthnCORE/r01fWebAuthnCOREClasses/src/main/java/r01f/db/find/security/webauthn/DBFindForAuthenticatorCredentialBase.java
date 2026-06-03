package r01f.db.find.security.webauthn;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.webauthn.FindServicesForAuthenticatorCredential;
import r01f.db.entities.security.webauthn.DBEntityForAuthenticatorCredential;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.interfaces.FindServicesForModelObject;
import r01f.util.types.Strings;

/**
 * Persistence layer User finder.
 */
@Slf4j
public abstract class DBFindForAuthenticatorCredentialBase<C extends AuthenticatorCredential,
						   		  		                   DB extends DBEntityForAuthenticatorCredential>
	   extends DBFindForModelObjectBase<CredentialOID,C,
	 								  	DBPrimaryKeyForModelObject,DB>
    implements FindServicesForModelObject<CredentialOID,C>,
               FindServicesForAuthenticatorCredential<C> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForAuthenticatorCredentialBase(final Class<C> modelObjectType,final Class<DB> dbEntityType,
							                    final DBModuleConfig dbCfg,
							                    final EntityManager entityManager,
							                    final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EXTENSION METHOD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public FindResult<C> findBy(final SecurityContext securityContext,
			                    final UserOID userOid ) {
		log.warn("[DBFindForAuthenticatorCredentialBase]: linked credentials to user {}", userOid);
		TypedQuery<DB> query = this.getEntityManager()
									.createQuery(Strings.customized("SELECT usageProcess " +
																	"  FROM {} usageProcess" +
																	" WHERE " +
																	"   usageProcess._userOid = :userOid ",
																	_DBEntityType.getSimpleName()),
															_DBEntityType)
									.setParameter("userOid", userOid.asString());

		Collection<DB> dbEntities = query.getResultList();
		return FindResultBuilder.using(securityContext)
									 .on(_modelObjectType)
									 .foundDBEntities(dbEntities)
								 .transformedToModelObjectsUsing(this);
	}
}
