package r01f.db.customizers.security.otp.persistence;

import r01f.guids.CommonOIDs.AppCode;

/*public class OTPCustomizer
  implements SessionCustomizer {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void customize(final Session session) throws Exception {
		Map<Class<?>, ClassDescriptor> descs = session.getDescriptors();
		Collection<ClassDescriptor> descriptors = descs.values();
	

		// This code assumes single table per descriptor!
		for ( final  ClassDescriptor desc : descriptors) {
			_updateTableNameMapping( session, desc );
		}
	}
	public AppCode getAppCode() {
		return AppCode.forId("r01f");
	}

	@SuppressWarnings("static-method")
	public String otpTaskTableName() {
		return "OTPTASK";
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	private void _updateTableNameMapping (final Session session,
			                              final ClassDescriptor desc) {
		Class clazz = desc.getJavaClass();
		desc.setTableName(_tableNameFrom(session, clazz) );
	}
	@SuppressWarnings("rawtypes")
	private String _tableNameFrom(final Session session,
			                      final Class clazz) {
		session.getLogin().getDatabaseURL();
		return this.getAppCode().asString().toUpperCase() +  otpTaskTableName() ;
	}
}*/
@SuppressWarnings("removal")
public class OTPCustomizer {
	//implements MetadataContributor {
   
	public AppCode getAppCode() {
		return AppCode.forId("r01f");
	}

    /*@Override
    public void contribute(InFlightMetadataCollector metadataCollector, IndexView jandexIndex) {
        
        final String appCode = this.getAppCode().asString().toUpperCase();
        final String tableSuffix = "OTPTASK";
        
        // El InFlightMetadataCollector contiene la colección de todos los mapeos de entidades.
        // Se itera sobre todos los PersistentClass (que son los descriptores de cada entidad).
        for (PersistentClass persistentClass : metadataCollector.getEntityBindingMap().values()) {
         
            if (persistentClass.getMappedClass().getName().endsWith("Task")) {          
                Table primaryTable = persistentClass.getTable();                
                if (primaryTable != null) {                    
                    //[4]. New name of the table in-flight
                    String newTableName = appCode + tableSuffix;
                    
                    //[5] Rename table
                    primaryTable.setName(newTableName);
                    
                    
                }
            }
        }
    }*/
}