package r01f.db.transfer.security;


import com.google.common.reflect.TypeToken;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.db.entities.security.DBEntityForPairedPhoneLoginConsent;
import r01f.model.ModelObject;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentBase;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;


/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@Slf4j
@Accessors(prefix="_")
public  abstract class PairedPhoneLoginConsentTransferToDBBase<C  extends PairedPhoneLoginConsentBase,
                                                               DB extends DBEntityForPairedPhoneLoginConsent>
	           extends SecurityObjectTransferToDBBase<C,DB>  
	        implements TransformsDBEntityIntoModelObject<DB,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter final Class<C> _modelObjType;
	@Getter final Marshaller _marshaler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginConsentTransferToDBBase(final Class<C> modelObjType, final Marshaller marshaler) {
		_modelObjType = modelObjType;
		_marshaler = marshaler;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// Set DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final C modelObj,final DB dbEntity) {
		
		log.trace("\n\n.....PairedPhoneLoginConsentTransferToDBBase.setDBEntityFieldsFromModelObject ");
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
	
		/////////////////////////////////  Phone
		dbEntity.setPhone(modelObj.getPairedPhone() != null ? modelObj.getPairedPhone().asString()
													   	    : null);
		log.trace(" ..modelObj paired phone {}",modelObj.getPairedPhone() );
		/////////////////////////////////  Status
		if (modelObj.getStatus() != null) {			
			dbEntity.setStatus(modelObj.getStatus());
		}
		log.trace(" ..modelObj status {}",modelObj.getStatus() );
		///////////////////////////////// Request Data
		if (modelObj.getRequest() != null) {
			dbEntity.setRequestAt(modelObj.getRequest().getRequestedAt());
		}		
		log.trace(" ..modelObj setRequestAt {}",modelObj.getRequest().getRequestedAt());
		////////////////////////////////  Response Data		
		if (modelObj.getResponse() != null ) {			
			if ( modelObj.getResponse().getResponseDate() != null ) {
				log.info("Setting response date {} ", modelObj.getResponse().getResponseDate());
				dbEntity.setResponseAt(modelObj.getResponse().getResponseDate());
			}
			log.info(" ..modelObj setResponseAt {}",modelObj.getResponse().getResponseDate());
			if ( modelObj.getResponse().getUserOid() != null ) {
				log.info("Setting wallet owner {}" , modelObj.getResponse().getUserOid());
				dbEntity.setUserOid(modelObj.getResponse().getUserOid().asString());
			}
			log.info(" ..modelObj user owner {}",modelObj.getResponse().getUserOid() );
		}		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// Get from DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public C dbEntityToModelObject(final SecurityContext securityContext, final  DB dbEntity) {
		log.debug("\n\n.....PairedPhoneLoginConsentTransferToDBBase.dbEntityToModelObject {}",
																							_modelObjType);
		if (dbEntity.getDescriptor() == null) {
			return null;
		}
		// read from descriptor.
		C consent = _marshaler.forReading().fromXml(dbEntity.getDescriptor(), TypeToken.of(_modelObjType));
		// set last entiy version ( on xml could be the last-1) 
		consent.setEntityVersion(dbEntity.getEntityVersion());
		return consent;
	}
}
