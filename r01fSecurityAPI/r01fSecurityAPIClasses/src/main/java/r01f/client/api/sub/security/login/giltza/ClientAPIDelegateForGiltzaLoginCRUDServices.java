package r01f.client.api.sub.security.login.giltza;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.giltza.CRUDServicesForGiltzaLogin;
import r01f.api.interfaces.security.login.google.CRUDServicesForGoogleLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginCRUDServicesBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.types.contact.EMail;

public class ClientAPIDelegateForGiltzaLoginCRUDServices<L extends GiltzaLogin>
     extends ClientAPIDelegateForLoginCRUDServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public ClientAPIDelegateForGiltzaLoginCRUDServices(final Provider<SecurityContext>  securityContextProvider,
                                                       final Marshaller marshaller,
                                                       final CRUDServicesForGiltzaLogin<L> crudServicesProxy) {
        super(securityContextProvider,
              marshaller,
              crudServicesProxy);
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a login from the given id
     * @param id the login
     * @return
     */
    @SuppressWarnings("unchecked")
    public L loadBy(final EMail email) {
        if (email == null) throw new IllegalArgumentException(email + " is NOT a valid mail address!!");
        CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForGoogleLogin.class)
                                     .loadBy(this.getSecurityContext(),
                                             email);
        L outEntity = opResult.getOrThrow();
        return outEntity;
    }
}
