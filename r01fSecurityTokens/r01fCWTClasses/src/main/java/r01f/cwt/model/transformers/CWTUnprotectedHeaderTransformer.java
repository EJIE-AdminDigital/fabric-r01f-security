package r01f.cwt.model.transformers;

import java.security.cert.CertificateEncodingException;

import com.authlete.cose.COSEUnprotectedHeader;
import com.authlete.cose.COSEUnprotectedHeaderBuilder;

import r01f.cwt.model.CWTUnprotectedHeader;
import r01f.types.StringHex;

public class CWTUnprotectedHeaderTransformer
	implements CWTModelObjectTransformer<CWTUnprotectedHeader,COSEUnprotectedHeader> {

	/**
	 *
	 * 	@Getter @Setter   private String _alg;
		@Getter @Setter   private List<Object> _crit;
		@Getter @Setter   private Object _contentType;
		@Getter @Setter   private byte[] _kid;
		@Getter @Setter   private byte[] _iv;
		@Getter @Setter   private byte[] _partialIv;
		@Getter @Setter   private List<X509Certificate> _x5chain;

	 * @param header
	 * @return
	 */
	@Override
	public  COSEUnprotectedHeader fromModelObject(final CWTUnprotectedHeader header) {

		COSEUnprotectedHeaderBuilder builder = new COSEUnprotectedHeaderBuilder();
		if ( header.getAlg() != null ) {
			builder.alg( header.getAlg());
		}

		if (header.getContentType() != null) {
			builder.contentType(header.getContentType());
		}
		if (header.getKid() != null) {
			builder.kid(header.getKid());
		}
		if ( header.getIv() != null) {
			builder.iv(header.getIv().decodeAsBytes());
		}
		if ( header.getPartialIv() != null ) {
			builder.partialIv(header.getPartialIv().decodeAsBytes());
		}
		if ( header.getX5chain() != null) {
			try {
				builder.x5chain(header.getX5chain());
			} catch (final CertificateEncodingException e) {
				e.printStackTrace();
			}
		}
		return builder.build();
	}

	@Override
	public  CWTUnprotectedHeader toModelObject (final COSEUnprotectedHeader header) {
		CWTUnprotectedHeader protectedHeader = new CWTUnprotectedHeader();

		if ( header.getAlg() != null ) {
			protectedHeader.setAlg((Integer) header.getAlg());
		}

		if (header.getContentType() != null) {
			protectedHeader.setContentType((String)header.getContentType());
		}
		if (header.getKid() != null) {
			protectedHeader.setKid( new String(header.getKid()));
		}
		if ( header.getIv() != null) {
			byte[] ivBytes = header.getIv();
			protectedHeader.setIv(StringHex.encode(ivBytes));
		}
		if ( header.getPartialIv() != null ) {
			byte[] partiallyIvBytes = header.getPartialIv();
			protectedHeader.setPartialIv(StringHex.encode(partiallyIvBytes));
		}
		if ( header.getX5Chain() != null) {
			protectedHeader.setX5chain(header.getX5Chain() );

		}
		return protectedHeader;
	}
}