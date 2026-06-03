package r01f.security.saml.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.util.types.Strings;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum SAMLIdPBinding
 implements EnumWithCode<String,SAMLIdPBinding> {
	POST("HTTP-POST"),
	REDIRECT("HTTP-Redirect");	// do not change

	@Getter private final String _code;
	@Getter private final Class<String> _codeType = String.class;

	public static SAMLIdPBinding fromHttpMethod(final HttpServletRequest req) {
		return SAMLIdPBinding.fromHttpMethod(req.getMethod());
	}
	public static SAMLIdPBinding fromHttpMethod(final String method) {
		if (Strings.isNullOrEmpty(method)) throw new IllegalArgumentException("Bad http method!");
		SAMLIdPBinding outBinding = null;
		if ("GET".equals(method)) {
			outBinding = SAMLIdPBinding.REDIRECT;
		} else if ("POST".equals(method)) {
			outBinding = SAMLIdPBinding.POST;
		} else {
			throw new IllegalArgumentException(method + " is NOT a supported SAML binding method!");
		}
		return outBinding;
	}
}
