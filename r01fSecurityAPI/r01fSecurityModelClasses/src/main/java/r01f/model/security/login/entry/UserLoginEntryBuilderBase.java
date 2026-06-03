package r01f.model.security.login.entry;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;

@Slf4j
@NoArgsConstructor
public abstract class UserLoginEntryBuilderBase<U extends UserLoginEntry>
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
// 	Entry Id.
/////////////////////////////////////////////////////////////////////////////////////////
    @RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class UserLoginEntryBuilderIdStep {
		private final U _user;

		public UserLoginEntryBuilderLoginIdStep entryId(final UserLoginEntryID id) {			
			_user.setId(id);
			return new UserLoginEntryBuilderLoginIdStep(_user);
		}
		public UserLoginEntryBuilderLoginIdStep noEntryId() {			
			return new UserLoginEntryBuilderLoginIdStep(_user);
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
// 	Login Id.
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserLoginEntryBuilderLoginIdStep {
		private final U _user;

		public UserDataBuilderBuildStep loginId(final LoginID id) {			
			_user.setLoginId(id);
			return new UserDataBuilderBuildStep(_user);
		}
		public UserDataBuilderBuildStep noLoginId() {
			
			return new UserDataBuilderBuildStep(_user);
		}
	}	
/////////////////////////////////////////////////////////////////////////////////////////
// 	Date
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserLoginEntryBuilderDatedStep {
		private final U _user;

		public UserDataBuilderBuildStep atDate(final Instant date) {			
			_user.setDate(date);
			return new UserDataBuilderBuildStep(_user);
		}
		public UserDataBuilderBuildStep now() {			
			return atDate(Instant.now());
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
// Build
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserDataBuilderBuildStep {
		private final U _user;

	
		public U build() {
			return _user;
		}
		public U buildWithOid() {
			_user.setOid(UserLoginEntryOID.supply());
			log.warn("..building oid type {} ",
					 _user.getOid().getClass());
			return _user;
		}
	}
}
