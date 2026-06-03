package r01f.model.security.login.xlnets;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public enum XLNetsLoginWorkPlaceCodePrefix
 implements EnumWithCode<String,XLNetsLoginWorkPlaceCodePrefix> {
	IE ("IE"),
	EJ ("EJ"),
	UD ("UD"),
	CO ("CO");

	@Getter private final  String _code;
	@Getter private final  Class<String>  _codeType = String.class;
}