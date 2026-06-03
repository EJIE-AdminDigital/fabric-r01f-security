package r01f.client.api.sub.security.login.webauthn;

import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallType;

/**
 * When using [web atestation finish flow]
 */
@MarshallType(as="responseForWebAuthAttestationFinishFlow")
@Accessors(prefix="_")
public class ResponseForWebAuthAttestationFinishFlow
  implements SecurityObject {

	private static final long serialVersionUID = -3134448127913644074L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public ResponseForWebAuthAttestationFinishFlow() {
		// default no-args constructor
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
