package r01f.model.security.otp.dispatch;

import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.OTPObject;
import r01f.model.security.otp.dispatch.OTPDispatchedMessageFactories.OTPDispatchedMessageFactory;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.securitycontext.SecurityIDS.Password;

public interface OTPDispatchRequest<F,T,	// FROM & TO
						     		M>  	// Message
		 extends OTPObject {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPTaskOID getTaskOid();
	public OTPDispatchMedium getMedium();

	public AppCode getAppCode();
	public AppComponent getAppComponent();

	public F getFrom();
	public T getTo();
	public <MF extends OTPDispatchedMessageFactory<F,T,M>> MF getMessageFactory();
	public <MF extends OTPDispatchedMessageFactory<F,T,M>> MF getMessageFactoryAs(final Class<MF> mfType);

	public M createMessageForSecret(final Password secret);

	public <D extends OTPDispatchRequest<?,?,?>> D as(final Class<D> reqType);
}
