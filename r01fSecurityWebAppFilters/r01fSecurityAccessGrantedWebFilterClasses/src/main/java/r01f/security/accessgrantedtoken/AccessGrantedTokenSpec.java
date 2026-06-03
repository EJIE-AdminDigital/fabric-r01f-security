package r01f.security.accessgrantedtoken;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.TimeLapse;

@MarshallType(as="accessTokenSpec")
@Accessors(prefix="_")
public class AccessGrantedTokenSpec 
  implements ContainsConfigData,
  			 Serializable {

	private static final long serialVersionUID = 6000152450300640587L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static final int DEFAULT_ACCESS_TOKEN_EXPIRATION_SECONDS = 60 * 30;	// 30 minutes
	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="issuer",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AppCode _issuer;
	
	@MarshallField(as="audience",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AppComponent _audience;
	
	@MarshallField(as="expirationSeconds",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _tokenExpirationSeconds = DEFAULT_ACCESS_TOKEN_EXPIRATION_SECONDS;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedTokenSpec() {
		// default no-args constructor
	}
	public AccessGrantedTokenSpec(final AppCode issuer,final AppComponent audience) {
		this(issuer,audience,
			 DEFAULT_ACCESS_TOKEN_EXPIRATION_SECONDS);
	}
	public AccessGrantedTokenSpec(final AppCode issuer,final AppComponent audience,
								  final long accessTokenExpSecs) {
		_issuer = issuer;
		_audience = audience;
		_tokenExpirationSeconds = accessTokenExpSecs;
	}
	public AccessGrantedTokenSpec(final AppCode issuer,final AppComponent audience,
								  final TimeLapse accessTokenExp) {
		_issuer = issuer;
		_audience = audience;
		_tokenExpirationSeconds = accessTokenExp.asMillis() / 1000;
	}
	public static AccessGrantedTokenSpec defaultSpec() {
		return new AccessGrantedTokenSpec(AppCode.forId("r01ht"),AppComponent.forId("r01.authmanager"));	// TODO legacy code!!! remove!
	}
}
