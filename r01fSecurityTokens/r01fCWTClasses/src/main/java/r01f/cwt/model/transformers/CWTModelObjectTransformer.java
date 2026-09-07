package r01f.cwt.model.transformers;

import r01f.cwt.model.CWTModelObject;

public interface CWTModelObjectTransformer<M extends  CWTModelObject ,A> {

	A fromModelObject(final M modelObject);

	M toModelObject(final A thirdPartyModelObject);
}
