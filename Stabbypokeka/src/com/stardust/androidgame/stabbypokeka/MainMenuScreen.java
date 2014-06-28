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

public class MainMenuScreen extends GLScreen
{
	Camera2D guiCam;
	SpriteBatcher batcher;
	Rectangle soundBounds;
	Rectangle playBounds;
	Rectangle highScoresBounds;
	Rectangle helpBounds;
	Vector2 touchPoint;
	
	public MainMenuScreen(Game game)
	{
		super(game);
		guiCam = new Camera2D(glGraphics, 480, 320);
		batcher = new SpriteBatcher(glGraphics, 100);
		soundBounds = new Rectangle(416, 0, 64, 64);
		playBounds = new Rectangle(240, 160, 128, 32);
		highScoresBounds = new Rectangle(240, 160, 240, 32);
		helpBounds = new Rectangle(240, 160 - 32 , 128, 32);
		touchPoint = new Vector2();
	}
	
	@Override
	public void update(float deltaTime)
	{
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for(int i = 0; i < len; i++)
		{
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP)
			{
				touchPoint.set(event.x, event.y);
				guiCam.touchToWorld(touchPoint);
				
				if(OverlapTester.pointInRectangle(playBounds, touchPoint))
				{
					Assets.playSound(Assets.click);
					game.setScreen(new GameScreen(game));
					return;
				}
				
				if(OverlapTester.pointInRectangle(highScoresBounds, touchPoint))
				{
					Assets.playSound(Assets.click);
					game.setScreen(new HighScoresScreen(game));
					return;
				}
				
				if(OverlapTester.pointInRectangle(helpBounds, touchPoint))
				{
					Assets.playSound(Assets.click);
					game.setScreen(new HelpScreen(game));
					return;
				}
				
				if(OverlapTester.pointInRectangle(soundBounds, touchPoint))
				{
					Assets.playSound(Assets.click);
					Settings.soundEnabled = !Settings.soundEnabled;
					if(Settings.soundEnabled)
					{
						Assets.music.play();
					}
					else
					{
						Assets.music.pause();
					}
				}
			}
		}
	}
	
	@Override
	public void present(float deltaTime)
	{
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		batcher.beginBatch(Assets.background);
		batcher.drawSprite(240, 160, 480, 320, Assets.backgroundRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,  GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		
		batcher.drawSprite(120, 160, 240, 320, Assets.logo);
		batcher.drawSprite(360, 160, 240, 96, Assets.mainMenu);
		batcher.drawSprite(448, 32, 64, 64, Settings.soundEnabled ? Assets.soundOff : Assets.soundOn);
		
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	@Override
	public void pause()
	{
		Settings.save(game.getFileIO());
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