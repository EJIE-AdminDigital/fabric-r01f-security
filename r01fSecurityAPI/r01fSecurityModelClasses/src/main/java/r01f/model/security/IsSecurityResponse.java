package r01f.model.security;

import java.time.Instant;

import r01f.enums.EnumWithCode;

public interface IsSecurityResponse<E extends Enum<E> & EnumWithCode<Integer,E> & IsSecurityRequestResult> 	
		 extends IsSecurityObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Instant getAt();
	public void setAt(Instant at); 
	
	public E getResult();
	public void setResult(E result);
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public default boolean hasFailed() {
		return this.getResult() != null && this.getResult().isError();
	}
	public default boolean hasSucceeded() {
		return this.getResult() != null && this.getResult().isOK();
	}
}
