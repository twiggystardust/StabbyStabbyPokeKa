package com.stardust.androidgame.stabbypokeka;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.stardust.androidgame.framework.Game;
import com.stardust.androidgame.framework.Input.TouchEvent;
import com.stardust.androidgame.framework.gl.Camera2D;
import com.stardust.androidgame.framework.gl.SpriteBatcher;
import com.stardust.androidgame.framework.impl.GLScreen;
import com.stardust.androidgame.framework.math.OverlapTester;
import com.stardust.androidgame.framework.math.Rectangle;
import com.stardust.androidgame.framework.math.Vector2;
import com.stardust.androidgame.stabbypokeka.World.WorldListener;

public class GameScreen extends GLScreen
{
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;
	
	int state;
	Camera2D guiCam;
	Vector2 touchPoint;
	SpriteBatcher batcher;
	World world;
	WorldListener worldListener;
	WorldRenderer renderer;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	Rectangle swordBounds;
	Rectangle jumpBounds;
	int lastScore;
	String scoreString;
	
	public GameScreen(Game game)
	{
		super(game);
		state = GAME_READY;
		guiCam = new Camera2D(glGraphics, 480, 320);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 1000);
		worldListener = new WorldListener()
		{
			public void jump()
			{
				Assets.playSound(Assets.jumpSound);
			}
			public void hit()
			{
				Assets.playSound(Assets.zelioDie);
			}
			public void swordSwing()
			{
				Assets.playSound(Assets.swordSwing);
			}
			public void swordHit()
			{
				Assets.playSound(Assets.swordHit);
			}
		};
		
		world = new World(worldListener);
		renderer = new WorldRenderer(glGraphics, batcher, world);
		pauseBounds = new Rectangle(480 -64, 320 - 64, 64, 64);
		resumeBounds = new Rectangle(240 - 96, 160 - 32, 192, 32);
		quitBounds = new Rectangle(240 - 96, 160 - 32, 192, 32);
		swordBounds = new Rectangle(32, 32, 64, 64);
		jumpBounds = new Rectangle(32, 448, 64, 64);
		lastScore = 0;
		scoreString = "Score: 0";
	}
	
	@Override
	public void update(float deltaTime)
	{
		if(deltaTime > 0.1f)
		{
			deltaTime = 0.1f;
		}
		
		switch(state)
		{
			case GAME_READY:
			{
				updateReady();
				break;
			}
			case GAME_RUNNING:
			{
				updateRunning(deltaTime);
				break;
			}
			case GAME_PAUSED:
			{
				updatePaused();
				break;
			}
			case GAME_LEVEL_END:
			{
				updateLevelEnd();
				break;
			}
			case GAME_OVER:
			{
				updateGameOver();
				break;
			}
		}
	}
	
	private void updateReady()
	{
		if(game.getInput().getTouchEvents().size() > 0)
		{
			state = GAME_RUNNING;
		}
	}
	
	private void updateRunning(float deltaTime)
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0 ; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			
			if(event.type != TouchEvent.TOUCH_UP)
			{
				continue;
			}
			
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if(OverlapTester.pointInRectangle(pauseBounds, touchPoint))
			{
				Assets.playSound(Assets.click);
				state = GAME_PAUSED;
				return;
			}
			
			if(OverlapTester.pointInRectangle(swordBounds, touchPoint))
			{
				if(world.zelio.state != Zelio.ZELIO_STATE_SWING)
				{
					Assets.playSound(Assets.swordSwing);
					world.zelio.state = Zelio.ZELIO_STATE_SWING;
				}
			}
			
			if(OverlapTester.pointInRectangle(jumpBounds, touchPoint))
			{
				if(world.zelio.state != Zelio.ZELIO_STATE_JUMP)
				{
					Assets.playSound(Assets.jumpSound);
					world.zelio.state = Zelio.ZELIO_STATE_JUMP;
				}
			}
			
			world.update(deltaTime, game.getInput().getAccelY());
			if(world.score != lastScore)
			{
				lastScore = world.score;
				scoreString = " " + lastScore;
			}
			
			if(world.state == World.WORLD_STATE_NEXT_LEVEL)
			{
				state = GAME_LEVEL_END;
			}
			
			if(world.state == World.WORLD_STATE_GAME_OVER)
			{
				state = GAME_OVER;
				if(lastScore >= Settings.highscores[4])
				{
					scoreString = "new HighScore: " + lastScore + "!";
				}
				else
				{
					scoreString = "score: " + lastScore;
				}
				Settings.addScore(lastScore);
				Settings.save(game.getFileIO());
			}
		}
	}
		
	private void updatePaused()
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP)
			{
				continue;
			}
			
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if(OverlapTester.pointInRectangle(resumeBounds, touchPoint))
			{
				Assets.playSound(Assets.click);
				state = GAME_RUNNING;
				return;
			}
			
			if(OverlapTester.pointInRectangle(quitBounds, touchPoint))
			{
				Assets.playSound(Assets.click);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}
	
	private void updateLevelEnd()
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP)
			{
				continue;
			}
			world = new World(worldListener);
			renderer = new WorldRenderer(glGraphics, batcher, world);
			world.score = lastScore;
			state = GAME_READY;
		}
	}
	
	private void updateGameOver()
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP)
			{
				continue;
			}
			game.setScreen(new MainMenuScreen(game));
		}
	}
	
	@Override
	public void present(float deltaTime)
	{
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		renderer.render();
		
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.items);
		switch(state)
		{
			case GAME_READY:
			{
				presentReady();
				break;
			}
			case GAME_RUNNING:
			{
				presentRunning();
				break;
			}
			case GAME_PAUSED:
			{
				presentPaused();
				break;
			}
			case GAME_LEVEL_END:
			{
				presentLevelEnd();
				break;
			}
			case GAME_OVER:
			{
				presentGameOver();
				break;
			}
		}
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
		
	}
		
	private void presentReady()
	{
		batcher.drawSprite(240, 160, 192, 32, Assets.ready);
	}
	
	private void presentRunning()
	{
		batcher.drawSprite(480 - 32, 320 - 32, 64, 64, Assets.pause);
		batcher.drawSprite(32, 32, 64, 64, Assets.swordButton);
		batcher.drawSprite(32, 448, 64, 64, Assets.jumpButton);
		Assets.font.drawText(batcher, scoreString, 16, 320 - 32);
	}
	
	private void presentPaused()
	{
		batcher.drawSprite(240,  160,  192,  96, Assets.pauseMenu);
		Assets.font.drawText(batcher, scoreString, 16, 320 - 32);
	}
	
	private void presentLevelEnd()
	{
		String topText = "Move Onward!";
		String bottomText = "Closer to the Princess!";
		float topWidth = Assets.font.glyphWidth * topText.length();
		float bottomWidth = Assets.font.glyphWidth * bottomText.length();
		Assets.font.drawText(batcher, topText, 240 - topWidth /2, 320 - 40);
		Assets.font.drawText(batcher, bottomText, 240 - bottomWidth / 2, 104);
	}
	
	private void presentGameOver()
	{
		batcher.drawSprite(240,  160,  128,  64,  Assets.gameOver);
		float scoreWidth = Assets.font.glyphWidth * scoreString.length();
		Assets.font.drawText(batcher, scoreString, 240 - scoreWidth / 2, 160 - 32);
	}


	
	@Override
	public void pause()
	{
		if(state == GAME_RUNNING)
			state = GAME_PAUSED;
	}
	
	@Override
	public void resume()
	{
	}
	
	@Override
	public void dispose()
	{
	}
}