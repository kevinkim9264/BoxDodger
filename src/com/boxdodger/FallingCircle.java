package com.boxdodger;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FallingCircle extends FallingObject {

	public FallingCircle(float pX, float pY, TiledTextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager, 20, 200);
		this.color = "red";
		
	}

}
