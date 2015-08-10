package com.boxdodger;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class StickMan extends AnimatedSprite{
	
	public PhysicsHandler mPhysicsHandler;
	
	private float velocity = 300.0f;
	private boolean movingRight = false;
	private boolean movingLeft = false;
	public boolean isDead = false;
	public boolean deadMotion = false;
	
	private float oldVelocity = 300.0f;
	
	public StickMan(final float pX, final float pY, final TiledTextureRegion pTextureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (this.mX <= 0) {
			this.setX(0);
		}
		if (this.mX >= (800 - this.getWidth())) {
			this.setX(800 - this.getWidth());
		}
		
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public void addVelocity(float plustVelocity) {
		this.velocity += plustVelocity;
		
	}
	
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	protected void moveRight() {
		if (this.movingRight == false || oldVelocity != velocity) {
			this.movingRight = true;
			this.movingLeft = false;
			this.mPhysicsHandler.setVelocityX(velocity);
			oldVelocity = velocity;
			this.animate(new long[] {75, 75, 75, 75}, 4, 7, true);
		}
	}
	
	protected void moveLeft() {
		if (this.movingLeft == false || oldVelocity != velocity) {
			this.movingLeft = true;
			this.movingRight = false;
			this.mPhysicsHandler.setVelocityX(-velocity);
			oldVelocity = velocity;
			this.animate(new long[] {75, 75, 75, 75}, 8, 11, true);
		}
	}
	
	protected void moveStop() {
		if (this.movingLeft || this.movingRight) {
			this.movingLeft = false;
			this.movingRight = false;
			this.mPhysicsHandler.setVelocityX(0);
			this.animate(new long[] {75, 75, 75, 75}, 0, 3, true);
		}
	}
	
	protected void moveDeath() {
		moveStop();
		this.animate(new long[] {600, 1000, 1000}, 12, 14, false);
	}
	
	
	
}
