package r01f.model.security.otp.task.dispatch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.security.otp.OTPRequestBase;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.dispatch.OTPDispatchedMessageFactories.IsOTPDispatchedMessageFactory;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;
import r01f.types.contact.IsContactMean;


@Accessors(prefix="_")
@RequiredArgsConstructor
abstract class OTPDispatchRequestBase<F extends IsContactMean,T extends IsContactMean,	// FROM & TO
						  		      M>  	// Message
	   extends OTPRequestBase 
	implements IsOTPDispatchRequest<F,T,M> {
	
	private static final long serialVersionUID = 4935499367686559959L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@Getter private final OTPTaskOID _taskOid;

	@Getter private final AppCode _appCode;
	@Getter private final AppComponent _appComponent;

	@Getter private final F _from;
	@Getter private final T _to;

	@Getter private final IsOTPDispatchedMessageFactory<F,T,M> _messageFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//	MESSAGE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public M createMessageForSecret(final Secret secret) {
		return _messageFactory.createMessageUsing(_from,_to,
												  secret);
	}
	@Override @SuppressWarnings("unchecked")
	public <MF extends IsOTPDispatchedMessageFactory<F,T,M>> MF getMessageFactoryAs(final Class<MF> mfType) {
		return (MF)_messageFactory;
	}
}