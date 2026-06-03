package r01f.model.security.otp.dispatch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.dispatch.OTPDispatchedMessageFactories.OTPDispatchedMessageFactory;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.securitycontext.SecurityIDS.Password;


@Accessors(prefix="_")
@RequiredArgsConstructor
abstract class OTPDispatchRequestBase<F,T,	// FROM & TO
						       		  M>  	// Message
    implements OTPDispatchRequest<F,T,	// FROM & TO
						   		  M> { 	// Message {
	private static final long serialVersionUID = 4935499367686559959L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@Getter private final OTPTaskOID _taskOid;

	@Getter private final AppCode _appCode;
	@Getter private final AppComponent _appComponent;

	@Getter private final F _from;
	@Getter private final T _to;

	@Getter private final OTPDispatchedMessageFactory<F,T,M> _messageFactory;

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public M createMessageForSecret(final Password password) {
		return _messageFactory.createMessageUsing(_from,_to,
												  password);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public <MF extends OTPDispatchedMessageFactory<F,T,M>> MF getMessageFactoryAs(final Class<MF> mfType) {
		return (MF)_messageFactory;
	}
	@Override @SuppressWarnings("unchecked")
	public <D extends OTPDispatchRequest<?,?,?>> D as(final Class<D> reqType) {
		return (D)this;
	}

}