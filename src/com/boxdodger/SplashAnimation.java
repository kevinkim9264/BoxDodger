package com.boxdodger;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SplashAnimation extends AnimatedSprite {

	public SplashAnimation(final float pX, final float pY, final TiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.animate(new long[] {100, 100, 100, 100}, 0, 3, false);
	}
	
}
