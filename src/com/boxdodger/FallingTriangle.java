package com.boxdodger;

import org.andengine.entity.modifier.RotationModifier;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FallingTriangle extends FallingObject {

	public FallingTriangle(float pX, float pY, TiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, 50, 400);
		this.registerEntityModifier(new RotationModifier(2, 0, 720));
		this.color = "pink";
	}

}
