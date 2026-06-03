package r01f.client.api.sub.security.login.xlnets;

import java.util.Collection;

import jakarta.inject.Provider;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.xlnets.FindServicesForXLNetsLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginFindServicesBase;
import r01f.model.PersistableModelObject;
import r01f.model.facets.Versionable;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLoginWorkPlaceCodeSummary;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

@Slf4j
public class ClientAPIDelegateForXLNetsLoginFindServices<L extends XLNetsLogin>
	 extends ClientAPIDelegateForLoginFindServicesBase<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForXLNetsLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
													   final Marshaller marshaller,
													   final FindServicesForXLNetsLogin<L,?> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds all persisted model login objets as [LoginID,WorkPlace] codes.
	 * If the entity is a {@link Versionable}  {@link PersistableModelObject}, it returns the
	 * currently active versions
	 * @param createDate
	 * @return a {@link PersistenceOperationOK} that encapsulates the entities
	 */
	@SuppressWarnings("unchecked")
	public Collection<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummary() {
		FindResult<XLNetsLoginWorkPlaceCodeSummary> summaryResult = this.getServiceProxyAs(FindServicesForXLNetsLogin.class)
																		.findWorkplaceSummaries(this.getSecurityContext());

		log.warn(summaryResult.debugInfo().toString());
		Collection<XLNetsLoginWorkPlaceCodeSummary> outOids = summaryResult.getOrThrow();
		return outOids;
	}
}
