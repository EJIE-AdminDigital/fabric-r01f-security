package r01f.model.security.authorization;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.security.IsLoginData;
import r01f.objectstreamer.Marshaller;
import r01f.types.SerializedData;

public interface HasAuthorizationTargetResourceSpec {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public SerializedData<IsAuthorizationResourcesData> getResources();
	public void setResources(final SerializedData<IsAuthorizationResourcesData> resources);
	
/////////////////////////////////////////////////////////////////////////////////////////
//	DATA
/////////////////////////////////////////////////////////////////////////////////////////
	public default AuthorizationResourcesSerializedDataAccessor accessResourcesSpecUsing(final Marshaller marshaller) {
		return new AuthorizationResourcesSerializedDataAccessor(this,
															    marshaller);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class AuthorizationResourcesSerializedDataAccessor {
		private final HasAuthorizationTargetResourceSpec _hasAuthTargetResourceSpec;
		private final Marshaller _marshaller;
		
		public <D extends IsAuthorizationResourcesData> void setData(final D data) {
			SerializedData<IsAuthorizationResourcesData> serData = SerializedData.<IsAuthorizationResourcesData>from(data)
																				 .marshalledUsing(_marshaller)
																				 .toXml();
			_hasAuthTargetResourceSpec.setResources(serData);
		}
		public <D extends IsLoginData> D getData(final Class<D> dataType) {
			return _hasAuthTargetResourceSpec.getResources() != null
						? _hasAuthTargetResourceSpec.getResources()
													.toModelObjUsing(_marshaller,
											   			 		  	 dataType)
						: null;
		}
		public String getSerializedData() {
			return _hasAuthTargetResourceSpec.getResources() != null
						? _hasAuthTargetResourceSpec.getResources()
													.getSerializedData()
						: null;
		}
	}
}
