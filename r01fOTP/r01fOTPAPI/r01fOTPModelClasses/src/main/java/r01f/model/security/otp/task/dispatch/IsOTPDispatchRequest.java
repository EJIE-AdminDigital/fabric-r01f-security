package r01f.model.security.otp.task.dispatch;

import r01f.model.security.otp.IsOTPObject;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.dispatch.OTPDispatchedMessageFactories.IsOTPDispatchedMessageFactory;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;
import r01f.types.contact.IsContactMean;

public interface IsOTPDispatchRequest<F extends IsContactMean,T extends IsContactMean,	// FROM & TO
						     		  M>  	// Message
		 extends IsOTPObject {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPTaskOID getTaskOid();
	public OTPDispatchMedium getMedium();

	public AppCode getAppCode();
	public AppComponent getAppComponent();

	public F getFrom();
	public T getTo();
	
	public <MF extends IsOTPDispatchedMessageFactory<F,T,M>> MF getMessageFactory();
	public <MF extends IsOTPDispatchedMessageFactory<F,T,M>> MF getMessageFactoryAs(final Class<MF> mfType);

	public M createMessageForSecret(final Secret secret);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public default <D extends IsOTPDispatchRequest<?,?,?>> D as(final Class<D> reqType) {
		return (D)this;
	}
}
