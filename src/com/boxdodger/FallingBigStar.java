package com.boxdodger;

import org.andengine.entity.modifier.RotationModifier;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FallingBigStar extends FallingObject {

	public FallingBigStar(float pX, float pY, TiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, 20, 200);
		this.registerEntityModifier(new RotationModifier(5, 0, 360));
	}

}
