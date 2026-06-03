package r01f.crypto.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import lombok.experimental.Accessors;

@Accessors(prefix="_")
public abstract class PEMParser {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static String publicKeyToPEM(final PublicKey key) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));

		pemWriter.writeObject(key);

		pemWriter.close();

		return new String(bos.toByteArray());
	}
	public static PublicKey publicKeyFromPEM(final byte[] pem) throws IOException {
		return publicKeyFromPEM(new String(pem));
	}
	public static PublicKey publicKeyFromPEM(final String pem) throws IOException {
		StringReader reader = new StringReader(pem);
		org.bouncycastle.openssl.PEMParser pemParser = new org.bouncycastle.openssl.PEMParser(reader);

		try {
			Object o = pemParser.readObject();
			if (o == null || !(o instanceof SubjectPublicKeyInfo)) {
				throw new IOException("Not an OpenSSL key");
			}
			PublicKey pk = new JcaPEMKeyConverter().setProvider("BC").getPublicKey((SubjectPublicKeyInfo) o);

			return pk;
		} finally {
			pemParser.close();
		}
	}
	public static String privateKeyToPEM(final PrivateKey key) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));

		pemWriter.writeObject(key);

		pemWriter.close();

		return new String(bos.toByteArray());
	}
	public static PrivateKey privateKeyFromPEM(final byte[] pem) throws IOException {
		return privateKeyFromPEM(new String(pem));
	}

	public static PrivateKey privateKeyFromPEM(final String pem) throws IOException {
		StringReader reader = new StringReader(pem);
		org.bouncycastle.openssl.PEMParser pemParser = new org.bouncycastle.openssl.PEMParser(reader);

		try {
			Object o = pemParser.readObject();

			if (o == null || !(o instanceof PEMKeyPair)) {
				throw new IOException("Not an OpenSSL key");
			}

			KeyPair kp = new JcaPEMKeyConverter().setProvider("BC").getKeyPair((PEMKeyPair) o);
			return kp.getPrivate();
		} finally {
			pemParser.close();
		}
	}


}
