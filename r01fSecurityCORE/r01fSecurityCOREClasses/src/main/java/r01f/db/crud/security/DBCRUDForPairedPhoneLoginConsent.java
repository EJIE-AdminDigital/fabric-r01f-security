package r01f.db.crud.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLoginConsent;
import r01f.db.entities.security.DBEntityForPairedPhoneLoginConsent;
import r01f.db.transfer.security.PairedPhoneLoginConsentTransferToDBBase;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentBase;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;


public abstract class DBCRUDForPairedPhoneLoginConsent< C  extends PairedPhoneLoginConsentBase,
                                                        DB extends DBEntityForPairedPhoneLoginConsent >
	 		  extends DBCRUDForModelObjectBase<PairedPhoneLoginConsentOID,C,DBPrimaryKeyForModelObject, DB>
  		   implements CRUDServicesForPairedPhoneLoginConsent<C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	MEMBERS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final PairedPhoneLoginConsentTransferToDBBase<C,DB> _transformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForPairedPhoneLoginConsent (  final Class<C> modelObjectType,
			                                   final Class<DB> dbEntityType,
											   final DBModuleConfig dbCfg,
											   final EntityManager entityManager,
											   final Marshaller marshaller,
											   final PairedPhoneLoginConsentTransferToDBBase<C,DB> modelObjToDBEntity) {
		super(modelObjectType,
			  dbEntityType, modelObjToDBEntity,
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
												 final C modelObj,
												 final DB dbEntity) {
		_transformer.setDBEntityFieldsFromModelObject(securityContext,
												      modelObj,dbEntity);
	}
	@Override
	public C dbEntityToModelObject(final SecurityContext securityContext,
								   final DB dbEntity) {

		System.out.println(" Trasnformer =" + _transformer.getClass().getName());
		C out = _transformer.dbEntityToModelObject(securityContext,
										           dbEntity);
		return out;
	}
}
