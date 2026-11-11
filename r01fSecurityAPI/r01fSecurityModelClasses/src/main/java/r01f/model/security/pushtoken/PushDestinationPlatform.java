package r01f.model.security.pushtoken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum PushDestinationPlatform
 implements EnumWithCode<String,PushDestinationPlatform> {
	ANDROID("Android"),
	IOS("iOS"),
	WEB("WEB"),
	;

	@Getter private final String _code;
	@Getter private final Class<String> _codeType = String.class;
	

	
}
