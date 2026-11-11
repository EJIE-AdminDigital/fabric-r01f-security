package r01f.model.security;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;

@Accessors(prefix="_")
public abstract class SecurityResponseBase<E extends Enum<E> & EnumWithCode<Integer,E> & IsSecurityRequestResult,
										   SELF_TYPE extends SecurityResponseBase<E,SELF_TYPE>> 
		   implements IsSecurityResponse<E>  {

    private static final long serialVersionUID = -8652875285821331943L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////    
	@MarshallField(as="at",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected Instant _at;  
	
	@MarshallField(as="result",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected E _result;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////	
	public SecurityResponseBase() {
		_at = Instant.now();
	}
	public SecurityResponseBase(final Instant at) {
		this();
		_at = at;
	}
	public SecurityResponseBase(final E result) {
		this(Instant.now(),result);
	}
	public SecurityResponseBase(final Instant at,final E result) {
		this();
		_at = at;
		_result = result;
	}
}
