package r01f.model.security.login.userpassword.factors;

import java.time.Instant;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorDispatchResponseBase")
@Accessors(prefix="_")
public abstract class LoginAuthFactorDispatchResponseBase
     implements LoginAuthFactorDispatchResponse {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="dispatched",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter public final boolean _dispatched;
	
	@MarshallField(as="tst",
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter public final Instant _tst;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorDispatchResponseBase(final boolean dispatched,final Instant tst) {		
		_dispatched = dispatched;
		_tst = tst;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean wasDispatched() {	
		return _dispatched;
	}
	@Override
	public LoginAuthFactorDispatchResponseNOK asNOK() {
		if ( this instanceof LoginAuthFactorDispatchResponseNOK )
			return  (LoginAuthFactorDispatchResponseNOK)this;
		 throw new  IllegalArgumentException(" Cannot cast asNOK()");
				
	}

	@Override
	public LoginAuthFactorDispatchResponseOK asOK() {
		if ( this instanceof LoginAuthFactorDispatchResponseOK )
			return  (LoginAuthFactorDispatchResponseOK)this;
	    throw new  IllegalArgumentException(" Cannot cast asOK()");
	}	
}