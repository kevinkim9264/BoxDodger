package com.boxdodger;

import org.andengine.entity.modifier.RotationModifier;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FallingSquare extends FallingObject {

	public FallingSquare(float pX, float pY, TiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, 40, 300);
		this.registerEntityModifier(new RotationModifier(3, 0, 360));
	}

}
