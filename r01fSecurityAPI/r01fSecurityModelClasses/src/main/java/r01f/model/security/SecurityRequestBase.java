package r01f.model.security;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;

@Accessors(prefix="_")
public abstract class SecurityRequestBase<SELF_TYPE extends SecurityRequestBase<SELF_TYPE>> 
		   implements IsSecurityRequest  {

    private static final long serialVersionUID = -8652875285821331943L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////    
	@MarshallField(as="at",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _at;  
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////	
	public SecurityRequestBase() {
		_at = Instant.now();
	}
	public SecurityRequestBase(final Instant at) {
		this();
		_at = at;
	}
}
