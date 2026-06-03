package r01f.security.handler.webauthn.utils;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.AuthenticatorTransport;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.Extensions.CredentialProperties.CredentialPropertiesOutput;
import com.yubico.webauthn.data.PublicKeyCredential;

public class WebAuthnRegistrationResponseParser {
//////////////////////////////////////////////////////////////////////////////////////////
// PARSE
/////////////////////////////////////////////////////////////////////////////////////////
	public static PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs>
											parseRegistrationResponse(final String registrationResponseAsString) {

		JsonElement jelement = JsonParser.parseString(registrationResponseAsString);
	    JsonObject  registrationResponseJson = jelement.getAsJsonObject();
		// ... [1] id
	    ByteArray id  = getId(registrationResponseJson);
		// ... [2] response
		AuthenticatorAttestationResponse response = getAuthenticatorResponse(registrationResponseJson);
		// ... [3] extensions
		ClientRegistrationExtensionOutputs ext  = getExtensions(registrationResponseJson);

		return  PublicKeyCredential.<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs>
									builder()
				                      .id(id)
				                      .response(response)
				                      .clientExtensionResults(ext)
				                      .build();
	}
//////////////////////////////////////////////////////////////////////////////////////////
// PARSE MEMBER METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public static ByteArray  getId(final JsonObject creationResponse) {
		  String id = creationResponse.get("id").getAsString();
		  return  WebAuthnByteStreamUtils.from(id.getBytes());
	}

	public static AuthenticatorAttestationResponse  getAuthenticatorResponse(final JsonObject creationResponse) {
		 JsonObject  response  =   creationResponse.getAsJsonObject("response");
		 String attestationObjectAsString = response.get("attestationObject").getAsString();
		 String clientDataJSONAsString =   response.get("clientDataJSON").getAsString();

		 ByteArray attestationObject =   WebAuthnByteStreamUtils.from(attestationObjectAsString.getBytes());
	     ByteArray clientDataJSON    =   WebAuthnByteStreamUtils.from(clientDataJSONAsString.getBytes());

	     Set<AuthenticatorTransport> transports = Sets.newHashSet();
	     JsonArray  transportJson  = response.get("transports").getAsJsonArray();
	     transportJson.forEach( t -> {
	    	                         transports.add( AuthenticatorTransport.of(t.getAsString()));

		 });
		 try {
				return AuthenticatorAttestationResponse.builder()
						 									.attestationObject(attestationObject)
						 									.clientDataJSON(clientDataJSON)
						 								  .transports(transports).build();

		} catch (final Throwable e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getLocalizedMessage());
		}
	}


   public static ClientRegistrationExtensionOutputs  getExtensions(final JsonObject creationResponse) {
	   JsonObject  extesnsion  =   creationResponse.getAsJsonObject("clientExtensionResults");
	   Boolean rk =  extesnsion.getAsJsonObject("credProps").get("rk").getAsBoolean();


	   CredentialPropertiesOutput credProps =  CredentialPropertiesOutput.builder().rk(rk).build();
	   ClientRegistrationExtensionOutputs outputs =
			   ClientRegistrationExtensionOutputs.builder()
				                                     .appidExclude(null)
				                                     .credProps(credProps)
				                                     .largeBlob(null)
			                                     .build();

	   return outputs;
	}

 //////////////////////////////////////////////////////////////////////////////////////
 // MAIN
 ////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] argv) {

		String json = "{\"type\":\"public-key\","
				+ "		 \"id\":\"3pVnGK3v2XpxC_h4oPx5jw\","
				+ "        \"response\":{\"attestationObject\":\"o2NmbXRkbm9uZWdhdHRTdG10oGhhdXRoRGF0YViUSZYN5YgOjGh0NBcPZHZgW4_krrmihjLHmVzzuoMdl2NdAAAAAOqbjWZNAR0hPOS2tIy1ddQAEN6VZxit79l6cQv4eKD8eY-lAQIDJiABIVggXk115CASP0xTGpqCYHyaxY8Q1Kxfk2g0fkF7TZR2WLwiWCDbPgb4O6TbhDZSW3OKVrernRNXOl0xDlljEyIki2aOwQ\","
				+ "                       \"clientDataJSON\":\"eyJ0eXBlIjoid2ViYXV0aG4uY3JlYXRlIiwiY2hhbGxlbmdlIjoiT3NoQThYa1BkWG8tZGtGUy05dnBJTzhZYU90VXhVbmMtYTh1aURGUi03byIsIm9yaWdpbiI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImNyb3NzT3JpZ2luIjpmYWxzZX0\","
				+ "                        \"transports\":[\"hybrid\",\"internal\"]},\"clientExtensionResults\":{\"credProps\":{\"rk\":false}}}";

	/*  ByteArray byteArray;
	 AuthenticatorResponse a;
	 PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs>;
	 PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc =
			    PublicKeyCredential.builder().id(byteArray).response(a).clientExtensionResults(b).build();*/

		  JsonElement jelement = JsonParser.parseString(json);
		  JsonObject  jobject = jelement.getAsJsonObject();
		  // ID
		  String id = jobject.get("id").getAsString();
		  System.out.println("id="+ id);


		  JsonObject  response  =   jobject.getAsJsonObject("response");
		  String attestationObjectAsString = response.get("attestationObject").getAsString();
		  String clientDataJSON = response.get("clientDataJSON").getAsString();


		  JsonArray  transports  = response.get("transports").getAsJsonArray();
		  transports.forEach( t -> {
			  						System.out.println(t.getAsString());
		  });

		  System.out.println("attestationObject="+ attestationObjectAsString);
		  System.out.println("clientDataJSON="+ clientDataJSON);

		  //StringBase64 base64 =  StringBase64.encode(attestationObjectAsString.getBytes());
		  //ByteArray attestationObject = WebAuthnByteStreamUtils.from((attestationObjectAsString.getBytes()));

		  /*JsonArray jarray = jobject.getAsJsonArray("response");
		  jobject = jarray.get(0).getAsJsonObject();
		  String result = jobject.get("translatedText").getAsString();*/

	}


}
