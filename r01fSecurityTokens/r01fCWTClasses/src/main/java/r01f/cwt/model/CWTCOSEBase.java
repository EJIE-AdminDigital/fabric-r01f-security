package r01f.cwt.model;



import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.types.StringHex;

@RequiredArgsConstructor
@Accessors(prefix="_")
public abstract class CWTCOSEBase
		   implements CWTCOSE {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter final private CWTCOSEType    _type;
	@Getter final private StringHex      _asCWTEncodedBytes;

}
