package r01f.model.security.authorization;

/**
 * A marker interface to mark classes that encodes the [target resources]
 * It can be
 * 		- the oid of the [target resource]
 * 		- a pattern (like *)
 * 		- a list of oids (like oid1,oid2,oid3)
 * 		- or anything else
 */
public interface IsAuthorizationResourcesData {
	// just a marker interface to mark classes that are authorization resources
}
