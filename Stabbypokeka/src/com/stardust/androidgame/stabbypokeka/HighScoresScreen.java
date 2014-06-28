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

public class HighScoresScreen extends GLScreen
{
	Camera2D guiCam;
	SpriteBatcher batcher;
	Rectangle backBounds;
	Vector2 touchPoint;
	String[] highScores;
	float xOffset = 0;
	
	public HighScoresScreen(Game game)
	{
		super(game);
		
		guiCam = new Camera2D(glGraphics, 480, 320);
		backBounds = new Rectangle(0, 64, 64, 64);
		touchPoint = new Vector2();
		batcher  =  new SpriteBatcher(glGraphics, 100);
		highScores = new String[5];
		for(int i = 0; i < 5; i++)
		{
			highScores[i] = (i +1) + "." + Settings.highscores[i];
			xOffset = Math.max(highScores[i].length() * Assets.font.glyphWidth, xOffset);
		}
		xOffset = 240 - xOffset / 2;
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
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if(event.type == TouchEvent.TOUCH_UP)
			{
				if(OverlapTester.pointInRectangle(backBounds, touchPoint))
				{
					Assets.playSound(Assets.click);
					game.setScreen(new MainMenuScreen(game));
					return;
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
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		batcher.drawSprite(240, 250, 240, 32, Assets.highScoresRegion);
		
		float y = 91;
		for(int i = 4; i >= 0; i--)
		{
			Assets.font.drawText(batcher, highScores[i], xOffset, y);
			y += Assets.font.glyphHeight;
		}
		
		batcher.drawSprite(32,  96,  64,  64,  Assets.arrow);
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	@Override
	public void resume()
	{
	}
	
	@Override
	public void pause()
	{
	}
	
	@Override
	public void dispose()
	{
	}
}