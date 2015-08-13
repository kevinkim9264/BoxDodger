package com.boxdodger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.boxdodger.basehelper.BaseHelper;
import com.boxdodger.R;






import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;

public class MainActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener {
	

	private final static int CAMERA_WIDTH = 800;
	private final static int CAMERA_HEIGHT = 480;
	
	Scene mScene;
	
	private Camera camera;
	
	long startTime = System.currentTimeMillis();
	int playTime = 0;
	int oldPlayTime = 0; // used for automatic ball-addition after 40seconds.
	
	
	// Region for stick man.
	private BitmapTextureAtlas stickBitmapTextureAtlas;
	private TiledTextureRegion stickBitmapTextureRegion;
	
	// Regions for controller(knob and arrow)
	private AnalogOnScreenControl mDigitalOnScreenControl;
	private BitmapTextureAtlas mOnScreenControlTexture;
	
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;
	
	// Region for falling objects.
	private BitmapTextureAtlas circleBitmapTextureAtlas;
	private TiledTextureRegion circleBitmapTextureRegion;
	
	private BitmapTextureAtlas squareBitmapTextureAtlas;
	private TiledTextureRegion squareBitmapTextureRegion;
	
	private BitmapTextureAtlas triangleBitmapTextureAtlas;
	private TiledTextureRegion triangleBitmapTextureRegion;
	
	private BitmapTextureAtlas bigStarBitmapTextureAtlas;
	private TiledTextureRegion bigStarBitmapTextureRegion;
	private boolean starAppeared = false;
	
	// Region for splash animations of falling objects.
	private BitmapTextureAtlas redSplashBitmapTextureAtlas;
	private TiledTextureRegion redSplashBitmapTextureRegion;
	
	private BitmapTextureAtlas blueSplashBitmapTextureAtlas;
	private TiledTextureRegion blueSplashBitmapTextureRegion;
	
	private BitmapTextureAtlas pinkSplashBitmapTextureAtlas;
	private TiledTextureRegion pinkSplashBitmapTextureRegion;
	
	private BitmapTextureAtlas blackSplashBitmapTextureAtlas;
	private TiledTextureRegion blackSplashBitmapTextureRegion;
	
	
	
	static StickMan stickMan;
	
	ArrayList<Long> tempRemove = new ArrayList<Long>();
	
	private boolean movingRight = false;
	private boolean movingLeft = false;
	
	int ballCount = 0;
	int maxBallCount = 1;
	int ballVelocity = 20;
	int ballAcceleration = 200;
	
	private Font timeFont;
	private Font gameOverFont;
	
	Text timeText;
	Text gameOverText;
	
	private boolean isPaused = false;
	private boolean justStarted = true; // This boolean is to decide if onResume is called for the first time,
	                                    // or for actually resuming the game.
	
	private long timePaused = 0;
	private long timePausedStart = 0;
	
	public Thread saveThread; //Need to create separate thread, so that saveScore can performed no on the 
	 				
	@Override
	public void onPause() {
		super.onPause();
		isPaused = true;
		timePausedStart = System.currentTimeMillis();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		isPaused = false;
		
		if (justStarted == false) {
			timePaused = System.currentTimeMillis() - timePausedStart;
			System.out.println("Resumed!");
		}
		justStarted = false;
	}
	
	
	void setDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher)
				.setTitle("BoxAvoider")
				.setMessage("If you go back to main menu now, the current score won't be saved."
						+ " Do you still want to go back to main menu?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		
		builder.create().show();
	}
	
	@Override
	public void onBackPressed() {
		if (stickMan.isDead == false) {
			
			setDialog();
		} else {
			if (saveThread.getState() == Thread.State.NEW) {
			saveThread.start();
			}
			super.onBackPressed();
		}
	}
	
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		saveThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					saveScore(playTime);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), camera);
		return engineOptions;
	}
	
	/**
	 * 理쒖�?�먯?�瑜���옣�⑸?���?
	 * 
	 * @param count
	 */
	public void saveScore(Integer count) {
		SharedPreferences pref = getSharedPreferences("pref",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("score", count.toString());
		editor.commit(); // save.
		
		// Name input
		String name = pref.getString("editText", "");
		String url = BaseHelper.URL + "getScore.php";
		
		try {
			String result = sendData(name.toString(), count.toString(),
					url);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			finish();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}
	}

	private String sendData(String param1, String param2, String API_URL)
			throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		HttpPost request = makeHttpPost(param1, param2, API_URL);
		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		return result;
	}

	// Post 諛⑹?��?�꼍��
	private HttpPost makeHttpPost(String param1, String param2, String url) {
		// TODO Auto-generated method stub
		HttpPost post = new HttpPost(url);
		Vector<NameValuePair> params = new Vector<NameValuePair>();
		params.add(new BasicNameValuePair("name", param1));
		params.add(new BasicNameValuePair("score", param2));
		post.setEntity(makeEntity(params));
		return post;
	}

	private HttpEntity makeEntity(Vector<NameValuePair> params) {
		HttpEntity result = null;
		try {
			result = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		//creating resources for stickMan
		this.stickBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 512,
				TextureOptions.BILINEAR);
		this.stickBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.stickBitmapTextureAtlas, this, "stick_man_better_trans_thinner.png", 0, 0, 4, 4);
		this.stickBitmapTextureAtlas.load();
		
		//creating resrouces for Falling Objects
		//========= For Circles
		this.circleBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, 
				TextureOptions.BILINEAR);
		this.circleBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.circleBitmapTextureAtlas, this, "circle_trans.png", 0, 0, 1, 1);
		this.circleBitmapTextureAtlas.load();
		
		//========== For squares
		this.squareBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32,
				TextureOptions.BILINEAR);
		this.squareBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.squareBitmapTextureAtlas, this, "square_trans.png", 0, 0, 1, 1);
		this.squareBitmapTextureAtlas.load();
		
		//========= For triangles
		this.triangleBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 64,
				TextureOptions.BILINEAR);
		this.triangleBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.triangleBitmapTextureAtlas, this, "triangle_trans.png", 0, 0, 1, 1);
		this.triangleBitmapTextureAtlas.load();
		
		//========= For bigStars.
		this.bigStarBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR);
		this.bigStarBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.bigStarBitmapTextureAtlas, this, "bigStar_trans.png", 0, 0, 1, 1);
		this.bigStarBitmapTextureAtlas.load();
		
		//creating resources for object splashing
		this.redSplashBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 64,
				TextureOptions.BILINEAR);
		this.redSplashBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.redSplashBitmapTextureAtlas, this, "red_splash_animation.png", 0, 0, 2, 2);
		this.redSplashBitmapTextureAtlas.load();
		
		this.blueSplashBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128,
				TextureOptions.BILINEAR);
		this.blueSplashBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.blueSplashBitmapTextureAtlas, this, "blue_splash_animation.png", 0, 0, 2, 2);
		this.blueSplashBitmapTextureAtlas.load();
		
		this.pinkSplashBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128,
				TextureOptions.BILINEAR);
		this.pinkSplashBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.pinkSplashBitmapTextureAtlas, this, "pink_splash_animation.png", 0, 0, 2, 2);
		this.pinkSplashBitmapTextureAtlas.load();
		
		this.blackSplashBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR);
		this.blackSplashBitmapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				this.blackSplashBitmapTextureAtlas, this, "black_splash_animation.png", 0, 0, 2, 2);
		this.blackSplashBitmapTextureAtlas.load();
		
		
		
		//creating resources for controllers
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mOnScreenControlTexture, this, "analog_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mOnScreenControlTexture, this, "analog_control_knob.png", 128, 0);
		//this.mOnScreenControlTexture.load();
		this.mEngine.getTextureManager().loadTexture(
				this.mOnScreenControlTexture);
		
		
		// ============= Time Text
		FontFactory.setAssetBasePath("font/");
		this.timeFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512,  512,
						TextureOptions.BILINEAR,  this.getAssets(),  "Plok.ttf",  28, true, Color.BLACK);
		this.timeFont.load();
		
		// ============ Game Over Text
		this.gameOverFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512,
						TextureOptions.BILINEAR, this.getAssets(), "Plok.ttf", 28, true, Color.RED);
		this.gameOverFont.load();
		
		
	}

	@Override
	protected Scene onCreateScene() {
		mScene = new Scene();
		mScene.registerUpdateHandler(new FPSLogger());
		mScene.setBackground(new Background(0.56f, 0.92f, 0.75f));
		VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		
		this.stickMan = new StickMan(CAMERA_WIDTH / 2, CAMERA_HEIGHT - this.stickBitmapTextureRegion.getHeight(),
				this.stickBitmapTextureRegion, vertexBufferObjectManager);
		
		mScene.attachChild(this.stickMan);
		
		
		
			
		this.mDigitalOnScreenControl = new AnalogOnScreenControl(10,
				CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(),
				this.camera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion,
				0.1f, 200, vertexBufferObjectManager, new IAnalogOnScreenControlListener() {

					@Override
					public void onControlChange(
							BaseOnScreenControl pBaseOnScreenControl,
							float pValueX, float pValueY) {
						if (stickMan.isDead == false){
							if (pValueX > 0) {
								stickMan.setVelocity(pValueX * 500);
								stickMan.moveRight();
							} else if (pValueX < 0) { // Controller to the left
								stickMan.setVelocity(pValueX * (-500));
								stickMan.moveLeft();
							} else if (pValueX == 0) {
								//stickMan.registerEntityModifier(new RotationModifier(3, 0, 360));
								stickMan.moveStop();
							}	
						} else { // stickman is dead.
							if (stickMan.deadMotion == false) {
								stickMan.deadMotion = true;
								stickMan.moveDeath();
								
								
							}
						}
						
					}

					@Override
					public void onControlClick(
							AnalogOnScreenControl pAnalogOnScreenControl) {
						// TODO Auto-generated method stub
						
					}
			
		});
    mDigitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);
    mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
    mDigitalOnScreenControl.getControlBase().setScale(1.25f);
    mDigitalOnScreenControl.getControlKnob().setScale(1.25f);
    mDigitalOnScreenControl.refreshControlKnobPosition();

    mScene.setChildScene(mDigitalOnScreenControl);
		
		//============== Time indication =============
		timeText = new Text(CAMERA_WIDTH - 180, 35, timeFont, "0 Sec", "XXXX Sec".length(), 
				vertexBufferObjectManager);
		timeText.registerEntityModifier(new ScaleModifier(2, 0.0f, 1.0f));
		mScene.attachChild(timeText);
		//=======================================
		
	mScene.setOnSceneTouchListener(this);
		
		
		mScene.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (timePaused != 0) {
					startTime += timePaused;
					timePaused = 0;
				}
				
				
				
				if (stickMan.isDead == false) {
					playTime = ((int) (System.currentTimeMillis() - startTime)) / 1000;
					timeText.setText(Integer.toString(playTime) + " Sec");
				}
				
				if (ballCount < maxBallCount) {
					System.out.println(ballCount + " ,  " + playTime);
					createRandomBall();
				}
				
				if (playTime !=0 && playTime % 10 == 0 && starAppeared == false) {
					starAppeared = true;
					createRandomStar();
				}
				if (playTime % 10 != 0) {
					starAppeared = false;
				}
				
				
				
				if (playTime < 10) {
					maxBallCount = playTime;
					
				} else if (playTime < 15) {
					maxBallCount = 11;
					ballVelocity = 25;
					ballAcceleration = 220;
				} else if (playTime < 20) {
					maxBallCount = 12;
					
				} else if (playTime < 25) {
					maxBallCount = 13;
					ballVelocity = 30;
					ballAcceleration = 250;
				} else if (playTime < 30) {
					maxBallCount = 14;
					ballVelocity = 35;
					ballAcceleration = 300;
				} else if (playTime < 35) {
					ballVelocity = 40;
					ballAcceleration = 350;
				} else if (playTime < 40) {
					maxBallCount = 15;
				} else { //From here, for every 10 seconds, maxBallCount += 1  and  velocity += 10 
					if (oldPlayTime != playTime && playTime % 10 == 0) {
						oldPlayTime = playTime;
						maxBallCount++;
						ballVelocity += 10;
						stickMan.addVelocity(10);
						
					}
				}
				
				
				
			}
			
			
			
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(
				GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.55f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(1.5f);
		this.mDigitalOnScreenControl.getControlKnob().setAlpha(0.40f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(0.7f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();
		
		
			
		mScene.setChildScene(this.mDigitalOnScreenControl);
		
		return mScene;
		
	}
	
	
	
	public void createRandomBall() {
		Random random = new Random();
		int randomShape = random.nextInt(10);
		
		final FallingObject object;
		if (randomShape >= 0 && randomShape <= 6) {
			object = new FallingCircle(randomBallPos(), -20, circleBitmapTextureRegion,
									this.getVertexBufferObjectManager());
			
		} else if (randomShape >=7 && randomShape <= 8) {
			object = new FallingSquare(randomBallPos(), -20, squareBitmapTextureRegion,
									this.getVertexBufferObjectManager());
		} else {
			object = new FallingTriangle(randomBallPos(), -20, triangleBitmapTextureRegion,
									this.getVertexBufferObjectManager());
		}
		
		mScene.attachChild(object);
		this.ballCount++;
		
		object.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (object.getPosY() >= (CAMERA_HEIGHT - object.getHeight() - 20) || stickMan.collidesWith(object)) {
					runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							ballCount--;
							mScene.detachChild(object);
							splashAnimation(object);
							if (stickMan.collidesWith(object)) {
								gameOver();
								stickMan.isDead = true;
							}
						}
					});
				}		
			}

			@Override
			public void reset(){
			}
			
		});
	}
	
	public void createRandomStar() {
		final FallingObject star = new FallingBigStar(randomBallPos(), -120, bigStarBitmapTextureRegion,
								this.getVertexBufferObjectManager());
		
		mScene.attachChild(star);
		this.ballCount++;
		
		star.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (star.getPosY() >= (CAMERA_HEIGHT - star.getHeight() - 20) || stickMan.collidesWith(star)) {
					runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							ballCount--;
							mScene.detachChild(star);
							splashAnimation(star);
							if (stickMan.collidesWith(star)) {
								gameOver();
								stickMan.isDead = true;
							}
						}
					});
				}		
			}

			@Override
			public void reset(){
			}
			
		});
	}
	
	public float randomBallPos() {
		Random random = new Random();
		return random.nextFloat() * (CAMERA_WIDTH - circleBitmapTextureRegion.getWidth());
	}
	
	/** For Falling Object splashing. */
	public void splashAnimation(FallingObject obj) {
		final SplashAnimation splash;
		String color = obj.color;
		switch(color) {
		case "red" : splash = new SplashAnimation(obj.getX(), obj.getY() + 5, this.redSplashBitmapTextureRegion, 
						this.getVertexBufferObjectManager());
					 break;
		case "blue" : splash = new SplashAnimation(obj.getX(), obj.getY() + 5, this.blueSplashBitmapTextureRegion,
						this.getVertexBufferObjectManager());
					 break;
		case "pink" : splash = new SplashAnimation(obj.getX(), obj.getY() + 5, this.pinkSplashBitmapTextureRegion,
						this.getVertexBufferObjectManager());
					 break;
		default		: splash = new SplashAnimation(obj.getX(), obj.getY(), this.blackSplashBitmapTextureRegion,
						this.getVertexBufferObjectManager());
					 break;
		}
		
		mScene.attachChild(splash);
		
		
		splash.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (splash.getCurrentTileIndex() == 3) {
					runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							mScene.detachChild(splash);
						}
					});
				}
				
			}

			@Override
			public void reset(){
			}
			
		});
	}

	public void gameOver() {
		// ===========  Game Over Text =============
		if (stickMan.isDead == false) {
			gameOverText = new Text(CAMERA_WIDTH / 2 - gameOverFont.getTexture().getWidth() / 2, 
					35, gameOverFont, "GAME OVER.......", "GAME OVER.......".length(),
					getVertexBufferObjectManager());
					gameOverText.registerEntityModifier(new ScaleModifier(2, 0.0f, 1.0f));
					mScene.attachChild(gameOverText);
		}
		
	}

	
	//Necessary for the user to go back to main menu, just by touching the screen.
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			if (stickMan.isDead){
				if (saveThread.getState() == Thread.State.NEW) {
				saveThread.start();
				}
				finish();
				return true;
			}
		}
		return false;
	}
	
}
