package r01f.cwt.parser;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.patterns.IsBuilder;

/**
 * CWT PARSER BUILDER .
 * Usage:
 * <pre class='brush:java'
 * 				CWTParserBuilder p = CWTParserBuilder.createParser()
 *                                                        .build();
 *		        p.parse(myCWT);
 * </pre>
 */

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class CWTParserBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static BuilderBuildStep createParser() {
		CWTParserBuilderInitWrapperData initData = new CWTParserBuilderInitWrapperData();

		return new CWTParserBuilder() { /* nothing */ }
					.new BuilderBuildStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class BuilderBuildStep {
		@SuppressWarnings("unused")
		private final CWTParserBuilderInitWrapperData _initData;

		public CWTParser build() {
			return new CWTParserImpl();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * CWTParserBuilderInitWrapperData
	 */
	@Accessors(prefix="_")
	public static class CWTParserBuilderInitWrapperData
	      implements Serializable {

		private static final long serialVersionUID = -7160644044519671660L;
	}
}
