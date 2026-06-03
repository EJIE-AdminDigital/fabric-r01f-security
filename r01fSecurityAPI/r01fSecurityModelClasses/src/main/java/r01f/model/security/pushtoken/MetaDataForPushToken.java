package r01f.model.security.pushtoken;

import r01f.locale.Language;
import r01f.model.metadata.annotations.DescInLang;
import r01f.model.metadata.annotations.MetaDataForType;
import r01f.model.security.metadata.MetaDataForSecurityObjectBase;

/**
 * Describes a {@link PushTokenBase}
 */
@MetaDataForType(modelObjTypeCode = 5000,
			     description = {
						@DescInLang(language=Language.SPANISH, value="Par teléfono-token para uso con notificaciones push"),
						@DescInLang(language=Language.BASQUE, value="[eu] Par teléfono-token para uso con notificaciones push"),
						@DescInLang(language=Language.ENGLISH, value="Phone-token pair for push notification purpouses")
				 })
public abstract class MetaDataForPushToken
			  extends MetaDataForSecurityObjectBase<PushTokenOID>{
	// nothing
}
