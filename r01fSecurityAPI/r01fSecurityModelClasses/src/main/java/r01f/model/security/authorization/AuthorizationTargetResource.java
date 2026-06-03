package r01f.model.security.authorization;

import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.LanguageTexts;
import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;

public interface AuthorizationTargetResource
		 extends HasAuthorizationTargetResourceSpec,
		 		 PersistableSecurityObject<AuthorizationTargetResourceOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public AppCode getAppCode();
	public void setAppCode(AppCode appCode);
	
	public AppComponent getAppComponent();
	public void setAppComponent(AppComponent appComponent);
	
	public String getName();
	public void setName(final String name);

	public LanguageTexts getDescription();
	public void setDescription(final LanguageTexts description);
}
