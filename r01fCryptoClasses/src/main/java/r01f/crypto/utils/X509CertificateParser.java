package r01f.crypto.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.google.common.base.Throwables;


import r01f.io.IOStreams;


public class X509CertificateParser {

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static X509Certificate loadPublicCertificate(final InputStream certificateAsInputStream)  {
       try {
			//InputStream is = SignWithPK.class.getClassLoader().getResource("publicKey.pem").openStream();
			String encodedKey = IOStreams.inputStreamAsString(certificateAsInputStream);
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate)f.generateCertificate(new ByteArrayInputStream(encodedKey.getBytes()));
			return certificate;
       }catch (final Throwable th) {
    	   th.printStackTrace();
    	   Throwables.throwIfUnchecked(th);
       }
       return null;
	}
}
