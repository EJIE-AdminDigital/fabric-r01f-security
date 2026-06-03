package r01f.cwt.model;

import java.security.cert.X509Certificate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.types.StringHex;

@NoArgsConstructor
@Accessors(prefix="_")
public class CWTUnprotectedHeader
  implements CWTModelObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter private Integer _alg;

	@Getter @Setter private String _contentType;

	@Getter @Setter private String _kid;

	@Getter @Setter private StringHex _iv;
	@Getter @Setter private StringHex _partialIv;

	@Getter @Setter private List<X509Certificate> _x5chain;
}
