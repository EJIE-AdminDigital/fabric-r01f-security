package r01f.model.security.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
@AllArgsConstructor
public abstract class SecurityAccessContext<S extends Object,
											R extends Object,
											A extends Object,
											E extends Object> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter private S _subject;
	@Getter @Setter private R _resource;
	@Getter @Setter private A _action;
	@Getter @Setter private E _environment;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_action == null) ? 0 : _action.hashCode());
		result = prime * result + ((_environment == null) ? 0 : _environment.hashCode());
		result = prime * result + ((_resource == null) ? 0 : _resource.hashCode());
		result = prime * result + ((_subject == null) ? 0 : _subject.hashCode());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecurityAccessContext<S,R,A,E> other = (SecurityAccessContext<S,R,A,E>) obj;
		if (_action == null) {
			if (other._action != null)
				return false;
		} else if (!_action.equals(other._action))
			return false;
		if (_environment == null) {
			if (other._environment != null)
				return false;
		} else if (!_environment.equals(other._environment))
			return false;
		if (_resource == null) {
			if (other._resource != null)
				return false;
		} else if (!_resource.equals(other._resource))
			return false;
		if (_subject == null) {
			if (other._subject != null)
				return false;
		} else if (!_subject.equals(other._subject))
			return false;
		return true;
	}
	
}
