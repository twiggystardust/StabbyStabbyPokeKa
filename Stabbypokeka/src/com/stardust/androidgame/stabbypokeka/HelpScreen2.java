package com.stardust.androidgame.stabbypokeka;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.stardust.androidgame.framework.Game;
import com.stardust.androidgame.framework.Input.TouchEvent;
import com.stardust.androidgame.framework.gl.Camera2D;
import com.stardust.androidgame.framework.gl.SpriteBatcher;
import com.stardust.androidgame.framework.gl.Texture;
import com.stardust.androidgame.framework.gl.TextureRegion;
import com.stardust.androidgame.framework.impl.GLScreen;
import com.stardust.androidgame.framework.math.OverlapTester;
import com.stardust.androidgame.framework.math.Rectangle;
import com.stardust.androidgame.framework.math.Vector2;

public class HelpScreen2 extends GLScreen
{
	Camera2D guiCam;
	SpriteBatcher batcher;
	Rectangle nextBounds;
	Rectangle backBounds;
	Vector2 touchPoint;
	Texture helpImage;
	TextureRegion helpRegion;
	
	public HelpScreen2(Game game)
	{
		super(game);
		
		guiCam = new Camera2D(glGraphics, 480, 320);
		nextBounds = new Rectangle(416, 64, 64, 64);
		backBounds = new Rectangle(0, 64, 64, 64);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 2);
	}
	
	@Override
	public void resume()
	{
		helpImage = new Texture(glGame, "help2.png");
		helpRegion = new TextureRegion(helpImage, 0, 0, 480, 320);
	}
	
	@Override
	public void pause()
	{
		helpImage.dispose();
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
				if(OverlapTester.pointInRectangle(nextBounds, touchPoint))
				{
					Assets.playSound(Assets.click);
					game.setScreen(new HelpScreen3(game));
					return;
				}
				
				if(OverlapTester.pointInRectangle(backBounds, touchPoint))
				{
					Assets.playSound(Assets.click);
					game.setScreen(new HelpScreen(game));
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
		
		batcher.beginBatch(helpImage);
		batcher.drawSprite(240, 160, 480, 320, helpRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		batcher.drawSprite(480-32,  96,  -64,  64, Assets.arrow);
		batcher.drawSprite(32, 96, 64, 64, Assets.arrow);
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	@Override
	public void dispose()
	{
	}
}