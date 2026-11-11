package r01f.model.security.principal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.security.IsSecurityPrincipal;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;

@ConvertToDirtyStateTrackable
@MarshallType(as="securityPrincipalForClientApp")
@Accessors(prefix="_")
public class SecurityPrincipalForClientApp 
	 extends SecurityPrincipalBase<SecurityPrincipalForClientApp>
  implements IsSecurityPrincipal {

	private static final long serialVersionUID = -3469215897913815764L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected AppCode _appCode;
	
	@MarshallField(as="appComponent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected AppComponent _appComponent;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPrincipalForClientApp() {
		super();
	}
	public SecurityPrincipalForClientApp(final AppCode appCodet) {
		this(appCodet,null);
	}
	public SecurityPrincipalForClientApp(final AppCode appCode,final AppComponent appComponent) {
		super();
		_appCode = appCode;
		_appComponent = appComponent;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public String getDisplayName() {
		return _appCode != null && _appComponent != null
				? String.format("%s - %s",
								_appCode,_appComponent)
				: _appCode != null ? _appCode.asString()
								   : "NO app code";
	}
}
