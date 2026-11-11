package r01f.model.security.login.data.xlnets;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsLoginData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.CommonOIDs.WorkPlaceCode;

@MarshallType(as="loginDataForXLNets")
@Accessors(prefix="_")
public class LoginDataForXLNets 
  implements IsLoginData {
	
	private static final long serialVersionUID = 2144725535919483486L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="workPlaceCode")
	@Getter @Setter private WorkPlaceCode _workPlaceCode;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginDataForXLNets() {
		// default constructor
	}
	public LoginDataForXLNets(final WorkPlaceCode workplaceCode) {
		_workPlaceCode = workplaceCode;
	}
}
