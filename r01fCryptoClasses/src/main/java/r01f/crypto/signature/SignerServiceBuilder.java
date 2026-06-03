package r01f.crypto.signature;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.crypto.signature.SignerService.SignerServiceException;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SignerServiceBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static SignerServicerBuilderSecurityProviderStep create() {

		return new SignerServiceBuilder() {
			/* nothing */}.new SignerServicerBuilderSecurityProviderStep();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class SignerServicerBuilderSecurityProviderStep {

		public SignerServicerBuilderKeyfactoryStep with(final Provider securityProvider) {
			return new  SignerServicerBuilderKeyfactoryStep(securityProvider);
		}
		public SignerServicerBuilderKeyfactoryStep withBouncySecurityProvider() {
			return new  SignerServicerBuilderKeyfactoryStep(new BouncyCastleProvider());
		}
		public SignerServicerBuilderKeyfactoryStep defaultSecurityProviders() {
			return new  SignerServicerBuilderKeyfactoryStep(null);// default plataform will be used
		}

	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class SignerServicerBuilderKeyfactoryStep {

		final Provider  _securityProvider;

		public SignerServicerBuilderAlgorithmStep andKeyfactory(final KeyFactory kf) {
			return new  SignerServicerBuilderAlgorithmStep(_securityProvider,
					   									    kf);
		}
		public SignerServicerBuilderAlgorithmStep andRSAKeyfactory() {
			try {
				if (_securityProvider != null ) {
					return new  SignerServicerBuilderAlgorithmStep(_securityProvider,
						                                           KeyFactory.getInstance("RSA", _securityProvider));
				}
				// Pass null security provider & call KeyFactory.getInstance("RSA") without security provider
				return new  SignerServicerBuilderAlgorithmStep(_securityProvider,
                                                               KeyFactory.getInstance("RSA"));

			} catch (final NoSuchAlgorithmException e) {
				throw new SignerServiceException(e.getMessage(), e);
			}
		}

		public SignerServicerBuilderAlgorithmStep andECDSAKeyfactory() {
			try {
				if (_securityProvider != null ) {
					return new  SignerServicerBuilderAlgorithmStep(_securityProvider,
							                                       KeyFactory.getInstance("ECDSA", _securityProvider));
				}
				// Pass null security provider & call KeyFactory.getInstance("ECDSA") without security provider
				return new  SignerServicerBuilderAlgorithmStep(_securityProvider,
                                                                 KeyFactory.getInstance("ECDSA"));
			} catch (final NoSuchAlgorithmException e) {
				throw new SignerServiceException(e.getMessage(), e);
			}
		}
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class SignerServicerBuilderAlgorithmStep {

		final Provider  _securityProvider;
	    final KeyFactory _keyFactory;

		public SignerServicerBuilderCharetStep algorithm(final SignatureAlgorithm signatureAlgorithm) {
			return new  SignerServicerBuilderCharetStep(_securityProvider,
					   									_keyFactory,
					   									signatureAlgorithm);
		}
		public SignerServicerBuilderCharetStep algorithm_SHA256_WITH_RSA() {
			return new  SignerServicerBuilderCharetStep(_securityProvider,
					   									_keyFactory,
					   									SignatureAlgorithm.SHA256_WITH_RSA);
		}
		public SignerServicerBuilderCharetStep algorithm_SHA256_WITH_ECDSA() {
			return new  SignerServicerBuilderCharetStep(_securityProvider,
					   									_keyFactory,
					   									SignatureAlgorithm.SHA2456_WITH_ECDSA);
		}
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class SignerServicerBuilderCharetStep {

		final Provider  _securityProvider;
	    final KeyFactory _keyFactory;
		final SignatureAlgorithm _signatureAlgorithm;

		public SignerServicerBuilderBuildStep andCharset(final Charset charset) {
			return new  SignerServicerBuilderBuildStep(_securityProvider,
													   _keyFactory,
													   _signatureAlgorithm,
													   charset);
		}
		public SignerServicerBuilderBuildStep defaultCharset() {
			return new  SignerServicerBuilderBuildStep(_securityProvider,
													   _keyFactory,
													   _signatureAlgorithm,
													    Charset.defaultCharset());
		}
	}

	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SignerServicerBuilderBuildStep {

		final Provider  _securityProvider;
	    final KeyFactory _keyFactory;
		final SignatureAlgorithm _signatureAlgorithm;
	    final Charset _charSet;

		public SignerService build()  {
			return new  SignerServiceImpl(_securityProvider,
										  _keyFactory,
										  _signatureAlgorithm,
										  _charSet);

		}
	}


}
