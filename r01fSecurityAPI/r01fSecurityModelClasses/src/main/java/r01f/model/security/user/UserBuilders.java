package r01f.model.security.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.patterns.FactoryFrom;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.securitycontext.SecurityOIDs.UserOID;

/**
 * Usage: just extend
 * <pre class='brush:java'>
 *			public abstract class MyUserBuilder 
 *					   implements IsBuilder {
 *				//////////
 *				public static UserDataBuilderStart<MyUser,MyUserBuilderPersonIDStep> create() {
 *					MyUser user = new MyUser();
 *					return new UserDataBuilderStart<>(user,MyUserBuilderPersonIDStep::new);
 *				}
 *				public static UserBuilderPersonalDataStep<MyUser,MyUserBuilderPersonIDStep> createWithOid(final UserOID userOid) {
 *					return MyUserBuilder.create()
 *										  .withOid(userOid);
 *				}
 *				//////////
 *				@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
 *				public static class MyUserBuilderPersonIDStep 
 *				  	     implements IsBuilderStep {
 *					
 *					private final MyUser _user;
 *					
 *					public UserDataBuilderBuildStep<MyUser> withPersonId(final PersonID personId) {
 *						_user.setPersonId(personId);
 *						return new UserDataBuilderBuildStep<>(_user);
 *					}
 *					public UserDataBuilderBuildStep<MyUser> noPersonId() {
 *						return new UserDataBuilderBuildStep<>(_user);
 *					}
 *				}
 *			}
 * </pre>
 * @param <U>
 * @param <NEXT_STEP>
 */
@Slf4j
@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class UserBuilders
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class UserDataBuilderStart<U extends User,NEXT_STEP extends IsBuilderStep> 
	  		 implements IsBuilderStep {
		
		private final U _user;
		private final FactoryFrom<U,NEXT_STEP> _nextBuilderStepFactory;
		
		public NEXT_STEP withOid(final UserOID oid) {
			_user.setOid(oid);
			return _nextBuilderStepFactory.from(_user);
		}
		public NEXT_STEP create() {
			return this.withOid(null);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BUILD
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class UserDataBuilderBuildStep<U extends User> 
	         implements IsBuilderStep {
		
		private final U _user;

		public U build() {
			return _user;
		}
		public U buildWithOid() {
			_user.setOid(UserOID.supply());
			log.warn("..building oid type {} ",
					 _user.getOid().getClass());
			return _user;
		}
	}
}
