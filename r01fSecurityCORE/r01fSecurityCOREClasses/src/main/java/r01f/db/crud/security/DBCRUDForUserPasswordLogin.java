package r01f.db.crud.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.db.entities.security.DBEntityForUserPasswordLogin;
import r01f.db.transfer.security.UserPasswordLoginTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.delegates.security.CRUDServicesDelegateForUserPasswordLogin;

public class DBCRUDForUserPasswordLogin<L extends UserPasswordLogin,
										DB extends DBEntityForUserPasswordLogin>
	 extends DBCRUDForLoginBase<L,DB>
  implements CRUDServicesForUserPasswordLogin<L>,
  			 TransformsDBEntityIntoModelObject<DB,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForUserPasswordLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
								  	  final DBModuleConfig dbCfg,
								  	  final EntityManager entityManager,
								  	  final Marshaller marshaller,
								  	  final UserPasswordLoginTransferToDBBase<L,DB> modelObjToDBEntity) {
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
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID userCode,
										final Password password) {
		throw new UnsupportedOperationException("Implemented at upper layer (see " + CRUDServicesDelegateForUserPasswordLogin.class + ")");
	}
	@Override
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID userCode,
										final Password oldPassword,final Password newPassword) {
		throw new UnsupportedOperationException("Implemented at upper layer (see " + CRUDServicesDelegateForUserPasswordLogin.class + ")");
	}
}
