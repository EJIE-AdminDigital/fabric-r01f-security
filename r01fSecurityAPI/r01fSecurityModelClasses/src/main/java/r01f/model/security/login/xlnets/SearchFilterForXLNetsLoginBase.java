package r01f.model.security.login.xlnets;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.enums.EnumExtended;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.metadata.FieldID;
import r01f.model.search.SearchFilterAsCriteriaString;
import r01f.model.search.SearchFilters;
import r01f.model.search.query.BooleanQueryClause;
import r01f.model.search.query.BooleanQueryClause.QueryClauseOccur;
import r01f.model.search.query.ContainsTextQueryClause;
import r01f.model.search.query.EqualsQueryClause;
import r01f.model.search.query.RangeQueryClause;
import r01f.model.security.login.SearchFilterForLoginBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.Range;


/**
 * <pre class='brush:java'>
 *		BooleanQueryClause reponseCodeQry = new BooleanQueryClause();
 *		reponseCodeQry.add(EqualsQueryClause.forField(LASTCONSUMER_RESPONSE_CODE)
 *											.of(200),QueryClauseOccur.SHOULD);
 *
 *		reponseCodeQry.add(EqualsQueryClause.forField(LASTCONSUMER_RESPONSE_CODE)
 *			                            	.of(201),QueryClauseOccur.SHOULD);
 *		System.out.println(reponseCodeQry.encodeAsString());
 * </pre>
 *
 * @param <R>
 * @param <SELF_TYPE>
 */
@Accessors(prefix="_")
public abstract class SearchFilterForXLNetsLoginBase<L extends XLNetsLogin,
  													 SELF_TYPE extends SearchFilterForXLNetsLoginBase<L,SELF_TYPE>>
	extends SearchFilterForLoginBase<L,SELF_TYPE> {

	private static final long serialVersionUID = -2278119967244726493L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchFilterForXLNetsLoginBase(final Class<L> objType) {
		super(objType);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
//the ids of these fields MUST match the corresponding DBEntity field name

	//.. Created at
	public static final FieldID CREATED_AT_ID = FieldID.forId("createDate");


	//.. WORKPLACE
	public static final FieldID WORKAPLACE_ID = FieldID.forId("workPlace");

	//...LOGINID
	public static final FieldID LOGIN_ID = FieldID.forId("loginId");

/////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Created at range.
	*/
	@MarshallField(as="createdAtRange")
	@Getter @Setter Range<Instant>  _createdAtRange;
	/**
	* Colective Prefix
	*/
	@MarshallField(as="colectivePrefix")
	@Getter @Setter XLNetsLoginWorkPlaceCodePrefix  _colectivePrefix;
	/**
	* workplaceCode
	*/
	@MarshallField(as="workplaceCode")
	@Getter @Setter WorkPlaceCode  _workplaceCode;
	/**
	* LoginId
	*/
	@MarshallField(as="loginId")
	@Getter @Setter LoginID  _loginId;
	/**
	* showWorkplacesChanged
	*/
	@MarshallField(as="showWorkplaces")
	@Getter @Setter FilterWorkplaceStatus  _showWorkplaces = FilterWorkplaceStatus.ALL;

/////////////////////////////////////////////////////////////////////////////////////////
//TO CRITERIA STRING
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SearchFilterAsCriteriaString toCriteriaString() {
		return SearchFilters.searchFilterToCriteriaString(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//BOOLEAN QUERY
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public BooleanQueryClause getBooleanQuery() {

		// build boolean query
		BooleanQueryClause outQry = new BooleanQueryClause();

		// .. Requested At Range Clause
		if (_createdAtRange !=  null) {
			RangeQueryClause<Instant> rangeAtQueryClause = null;
			if (_createdAtRange.hasLowerBound() && _createdAtRange.hasUpperBound()) {
				rangeAtQueryClause = RangeQueryClause.forField(CREATED_AT_ID)
													 .open(_createdAtRange.getLowerBound(),
													       _createdAtRange.getUpperBound());

			} else if (_createdAtRange.hasLowerBound()) {
					rangeAtQueryClause = RangeQueryClause.forField(CREATED_AT_ID)
														 .greaterThan(_createdAtRange.getLowerBound()); //greaterThanOrEqualTo¿?


			} else if (_createdAtRange.hasUpperBound()) {
					rangeAtQueryClause = RangeQueryClause.forField(CREATED_AT_ID)
							.lessThan(_createdAtRange.getLowerBound()); //lessThanOrEqualTo¿?
			}
			outQry.add(rangeAtQueryClause,QueryClauseOccur.MUST);
		}

		if ( _colectivePrefix != null ) {
			ContainsTextQueryClause textQry1 = ContainsTextQueryClause.forField(WORKAPLACE_ID)
	 																  .beginingWith(_colectivePrefix.getCode())
	 																  .languageIndependent();

			outQry.add(textQry1,QueryClauseOccur.MUST);

		}
		if (_workplaceCode != null) {
			EqualsQueryClause<String> equalsQueryClause = EqualsQueryClause.forField(WORKAPLACE_ID)
																		   .of(_workplaceCode.asString());


			outQry.add(equalsQueryClause,QueryClauseOccur.MUST);
		}
		if (_loginId != null) {
			EqualsQueryClause<String> equalsQueryClause = EqualsQueryClause.forField(LOGIN_ID)
																		   .of(_loginId.asString());


			outQry.add(equalsQueryClause,QueryClauseOccur.MUST);
		}
		return outQry;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
  public enum FilterWorkplaceStatus
   implements EnumExtended<FilterWorkplaceStatus> {
		ALL,
		CHANGED,
		NOT_CHANGED,
		NOT_FOUND;

		public static final boolean canBe(final String name) {
			return EnumExtended.canBeFromName(name,FilterWorkplaceStatus.class);
		}
	}

}

