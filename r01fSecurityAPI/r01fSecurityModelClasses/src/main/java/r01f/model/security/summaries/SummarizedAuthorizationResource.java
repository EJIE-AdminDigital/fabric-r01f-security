package r01f.model.security.summaries;

import java.util.Collection;

import r01f.facets.HasOID;
import r01f.facets.LangInDependentNamed;
import r01f.model.SummarizedModelObject;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.authorization.HasAuthorizationTargetResourceSpec;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;

public interface SummarizedAuthorizationResource<A extends AuthorizationTargetResource> 
		 extends HasAuthorizationTargetResourceSpec,
		 		 SummarizedModelObject<A>,
		 		 HasOID<AuthorizationTargetResourceOID>,
		 		 LangInDependentNamed {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Returns the number of [user auths] with a certain role
	 * @return
	 */
	public Collection<UserAuthWithRoleCount> getUserAuthsCountByRole();
	/**
	 * Sets the number of [user auths] with a certain role
	 * @param byRole
	 */
	public void setUserAuthsCountByRole(final Collection<UserAuthWithRoleCount> byRole);
	/**
	 * Returns the number of [user auths] with a certain role
	 * @param userRole
	 * @return
	 */
	public long getUserAuthWithRoleCount(final UserRole userRole);
}
