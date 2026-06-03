package r01f.model.security.otp.dispatch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.dispatch.OTPDispatchedMessageFactories.OTPEMailDispatchedMessageFactory;
import r01f.model.security.otp.dispatch.OTPDispatchedMessageFactories.OTPSMSDispatchedMessageFactory;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.patterns.IsBuilder;
import r01f.types.contact.EMail;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPDispatchRequestBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static OTPDispatchRequestBuilderMediumStep forTaskWithOid(final OTPTaskOID taskOid) {
		return new OTPDispatchRequestBuilder() { /* nothing */ }
						.new OTPDispatchRequestBuilderMediumStep(taskOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderMediumStep {
		private final OTPTaskOID _taskOid;

		public OTPDispatchRequestBuilderMailFromStep usingEMail() {
			return new OTPDispatchRequestBuilderMailFromStep(_taskOid);
		}
		public OTPDispatchRequestBuilderSMSFromStep usingSMS() {
			return new OTPDispatchRequestBuilderSMSFromStep(_taskOid);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MAIL
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderMailFromStep {
		private final OTPTaskOID _taskOid;

        public OTPDispatchRequestBuilderMailToStep from(final EMailRFC822Address email) {
            return new OTPDispatchRequestBuilderMailToStep(_taskOid,
											   			    email);
        }
        public OTPDispatchRequestBuilderMailToStep from(final EMail email,final String name) {
            return new OTPDispatchRequestBuilderMailToStep(_taskOid,
											   			    EMailRFC822Address.of(email,name));
        }
        public OTPDispatchRequestBuilderMailToStep from(final EMail email) {
            return new OTPDispatchRequestBuilderMailToStep(_taskOid,
											   			   EMailRFC822Address.of(email));
        }
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderMailToStep {
		private final OTPTaskOID _taskOid;
		private final EMailRFC822Address _from;

        public OTPDispatchRequestBuilderMailFactoryStep to(final EMailRFC822Address email) {
            return new OTPDispatchRequestBuilderMailFactoryStep(_taskOid,
											   			   		_from,email);
        }
        public OTPDispatchRequestBuilderMailFactoryStep to(final EMail email,final String name) {
            return new OTPDispatchRequestBuilderMailFactoryStep(_taskOid,
											   			   		_from,EMailRFC822Address.of(email,name));
        }
        public OTPDispatchRequestBuilderMailFactoryStep to(final EMail email) {
            return new OTPDispatchRequestBuilderMailFactoryStep(_taskOid,
											   			   		_from,EMailRFC822Address.of(email));
        }
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderMailFactoryStep {
		private final OTPTaskOID _taskOid;
		private final EMailRFC822Address _from;
		private final EMailRFC822Address _to;

		public OTPDispatchRequestBuilderByMailBuildStep buildMessageUsing(final OTPEMailDispatchedMessageFactory messageFactory) {
			return new OTPDispatchRequestBuilderByMailBuildStep(_taskOid,
							   			   		 				_from,_to,
							   			   		 				messageFactory);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderByMailBuildStep {
		private final OTPTaskOID _taskOid;
		private final EMailRFC822Address _from;
		private final EMailRFC822Address _to;
		private final OTPEMailDispatchedMessageFactory _messageFactory;

		public OTPDispatchRequestByEMail buildFor(final AppCode appCode,final AppComponent appComp) {
			return new OTPDispatchRequestByEMail(_taskOid,
											     appCode,appComp,
											     _from,_to,
											     _messageFactory);
		}
		public OTPDispatchRequestByEMail buildFor(final AppCode appCode) {
			return this.buildFor(appCode,null);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SMS
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderSMSFromStep {
		private final OTPTaskOID _taskOid;

        public OTPDispatchRequestBuilderSMSToStep from(final OwnedContactMean<Phone> phone) {
            return new OTPDispatchRequestBuilderSMSToStep(_taskOid,
            											  phone);
        }
        public OTPDispatchRequestBuilderSMSToStep from(final Phone phone,final String name) {
            return new OTPDispatchRequestBuilderSMSToStep(_taskOid,
            											  new OwnedContactMean<Phone>(phone,name));
        }
        public OTPDispatchRequestBuilderSMSToStep from(final Phone phone) {
            return new OTPDispatchRequestBuilderSMSToStep(_taskOid,
            											  new OwnedContactMean<Phone>(phone,null));
        }

	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderSMSToStep {
		private final OTPTaskOID _taskOid;
		private final OwnedContactMean<Phone> _from;

        public OTPDispatchRequestBuilderSMSFactoryStep to(final Phone phone) {
            return new OTPDispatchRequestBuilderSMSFactoryStep(_taskOid,
            												   _from,phone);
        }
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderSMSFactoryStep {
		private final OTPTaskOID _taskOid;
		private final OwnedContactMean<Phone> _from;
		private final Phone _to;

        public OTPDispatchRequestBuilderBySMSBuildStep buildMessageUsing(final OTPSMSDispatchedMessageFactory messageFactory) {
            return new OTPDispatchRequestBuilderBySMSBuildStep(_taskOid,
            												   _from,_to,
            												   messageFactory);
        }
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPDispatchRequestBuilderBySMSBuildStep {
		private final OTPTaskOID _taskOid;
		private final OwnedContactMean<Phone> _from;
		private final Phone _to;
		private final OTPSMSDispatchedMessageFactory _messageFactory;

		public OTPDispatchRequestBySMS buildFor(final AppCode appCode,final AppComponent appComp) {
			return new OTPDispatchRequestBySMS(_taskOid,
											   appCode,appComp,
											   _from,_to,
											   _messageFactory);
		}
		public OTPDispatchRequestBySMS buildFor(final AppCode appCode) {
			return this.buildFor(appCode,null);
		}
	}
}
