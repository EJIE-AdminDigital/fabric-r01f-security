package r01f.crypto.httpsig;

import java.util.Collection;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.httpclient.HttpHeader;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;


@MarshallType(as="httpSigningStringHeaders")
@Accessors(prefix="_")
@NoArgsConstructor
public class HttpSigningStringHeaders {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="headers")
	@Setter @Getter private Collection<HttpHeader>  _headers;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public String asString() {
		String separatedByComma =
				_headers.stream()
			             .map (h -> h.getId().asString().toLowerCase() + ": " +  h.getValue())
			             .collect(Collectors.joining ("\n"));
			           //  .collect(Collectors.joining (System.lineSeparator()));
		return separatedByComma;
	}
}
