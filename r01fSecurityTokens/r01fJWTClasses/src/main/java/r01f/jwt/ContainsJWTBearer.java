package r01f.jwt;

/**
 * Useful when abstracting the JWTBearer location:
 * 
 * Given a base type like:
 * <pre class='brush:java'>
 * 		public abstract class JWTBasedSomethingBase {
 * 
 *			protected static JWTBearer _getApiKeyJWTBearer(final ContainsJWTBearer containsJwtBearer) {
 *				return containsJwtBearer.getJWTBearer();
 *			}
 * 
 * 			protected void _doSomethingWithTheJWT(final JWTBearer jwt) {
 * 				...
 * 			}
 * 		}
 * </pre>
 * ... the question is how to get the JWTBearer when it might be anywhere like an http request
 *     BUT without showing up the JWTBearer carrier implementation (ie the http request)
 * The final type is like:
 * <pre class='brush:java'>
 * 		public class JWTBasedThing
 * 			 extends JWTBasedSomethingBase {
 * 
 * 			public void filter(final ContainerRequestContext reqCtx) throws IOException {
 * 				// the base type DOES NOT KNOW that the JWTBearer is at a request header...
 *		        JWTBearer jwBearer = _getApiKeyJWTBearer(new ContainsJWTBearer() {
 *		        												@Override
 *		        												public JWTBearer getJWTBearer() {
 *		        													String jwtBearerAsString  = reqCtx.getHeaderString(JWTBearer.HEADER_PARAM_NAME);
 *		        													return jwtBearerAsString != null ? JWTBearer.forValue(jwtBearerAsString)
 *		        																					 : null;
 *	 	        												}
 *		        										 });
 *				....
 * 			}
 * 		}
 * </pre>
 */
public interface ContainsJWTBearer {
	public JWTBearer getJWTBearer();
}
