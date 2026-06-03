package r01f.crypto.httpsig;

import java.util.Collection;
import java.util.stream.Collectors;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.httpclient.HttpHeaderID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;
import r01f.util.types.collections.Lists;


@MarshallType(as="httpSignature") 	//https://datatracker.ietf.org/doc/html/draft-cavage-http-signatures-12
@Accessors(prefix="_")
@NoArgsConstructor
public class HttpSignature {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="keyId")
	@Setter @Getter private String  _keyId;

	@MarshallField(as="algorithm")
	@Setter @Getter private String  _algorithm;

	@MarshallField(as="headers")
    @Getter private Collection<HttpHeaderID>  _headers; //in the same order as they have in the signing string signature

	@MarshallField(as="signature")
	@Setter @Getter private StringBase64 _signature;
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public void setHeaders(final HttpHeaderID...headerId ) {
		_headers = Lists.newArrayList(headerId);
	}
	public void setHeaders(final Collection<HttpHeaderID> headerIds ) {
		_headers = headerIds;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  Sample:
// 		signature: keyId="5D3E79AAE2EF293246323119FFAA5E80",algorithm="rsa-sha512",headers="content-length x-request-id content-type digest",signature="HDsh1QjiH7bG0/+7W4s6HdEIy0ksCu6BoAb4VkasMvT5DO+ahZWi4dmX86g57BSu24sOdURptUyJUPdiBVNCJ1a2b5Ue8JEfzxg2aW3QVNtImVs7KFdAFdOgxwwtApJrfX9E2y1O0hr7jFAMvMHGZtmEIdqmMMWjDqQfqRErfGwNcNynZoTDe/N0YhmMiiISEWZdiw4f8zuvVH3bjOxqyBmUUQrOQO3HCHXUGiVm5NZMTFfEnYXxX5kBDyye4mnTHS7VO8e6OwYlmaAz3mCdqeIIyCUjB7uKiq0aBHcdKWhK6HfPNxbVxmapUAwud0eOcivFoR80eo4hLHLwmG9amw=="

/////////////////////////////////////////////////////////////////////////////////////////
	public String asString() {
	    String headerIdsAsString  = _headers.stream()
			   								.map( headerId -> headerId.asString().toLowerCase() )
						                    .collect(Collectors.joining(" "));

		StringBuffer normalizedStr = new StringBuffer();
		normalizedStr.append("keyId=\""      + _keyId + '\"');
		normalizedStr.append( ",algorithm=\""  +  _algorithm + '\"');
		normalizedStr.append( ",headers=\"" +     headerIdsAsString + '\"');
		normalizedStr.append( ",signature=\"" +   _signature.asString() + '\"');
		return normalizedStr.toString();
	}
}
