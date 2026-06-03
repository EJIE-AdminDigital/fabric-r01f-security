package r01f.model.security.otp.generation;

import java.security.SecureRandom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.securitycontext.SecurityIDS.Password;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPSecretFactory {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static Password createSecret(final OTPSecretType otpType,
								 		final long length) {
		StringBuffer sb = new StringBuffer();
		SecureRandom rnd = new SecureRandom();
		for (int i = 0; i < length; i++) {
			int index = rnd.nextInt(otpType.getPattern().length());
			char c = otpType.getPattern().charAt(index);
			sb.append(c);
		}
		return Password.forId(sb.toString());
	}

}
