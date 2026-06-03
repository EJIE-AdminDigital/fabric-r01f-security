package r01f.cwt.parser;

import r01f.cwt.CWT;
import r01f.cwt.model.CWTDecoded;


public interface CWTParser {

	CWTDecoded parse(final CWT cwt);
}
