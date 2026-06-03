package r01f.cwt.verifier;



import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum CWTVerificationResultInvalidCause
  implements EnumWithCode<Integer,CWTVerificationResultInvalidCause> {
	
	INVALID_SIGNATURE(1),
	EXPIRED(2),
	INVALID_AUDIENCE(3),
	INVALID_ISSUE_DATE(4),
	INVALID_ISSUER(5),
	INVALID_ENCRYPTION(6),
	INVALID_VERSION(7),
	OTHER(100);

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
 }