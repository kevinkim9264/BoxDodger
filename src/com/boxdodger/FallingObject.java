package com.boxdodger;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.hardware.SensorManager;

public class FallingObject extends AnimatedSprite{
	
	public PhysicsHandler mPhysicsHandler;
	boolean already_collided = false;
	int START_VELOCITY;
	int ACCELERATION;
	
	public FallingObject(final float pX, final float pY, final TiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, int initVelocity, int initAcceleration) {
		
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		
		this.START_VELOCITY = initVelocity;
		this.ACCELERATION = initAcceleration;
		
		this.mPhysicsHandler.setVelocityY(START_VELOCITY);
		this.mPhysicsHandler.accelerate(0, ACCELERATION);
		//this.registerEntityModifier(new RotationModifier(3, 0, 360));
		
	}
	
	public float getPosY() {
		return this.mY;
	}
	
	
}
