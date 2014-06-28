package com.stardust.androidgame.stabbypokeka;

import javax.microedition.khronos.opengles.GL10;

import com.stardust.androidgame.framework.gl.Animation;
import com.stardust.androidgame.framework.gl.Camera2D;
import com.stardust.androidgame.framework.gl.SpriteBatcher;
import com.stardust.androidgame.framework.gl.TextureRegion;
import com.stardust.androidgame.framework.impl.GLGraphics;

public class WorldRenderer
{
	static final float FRUSTUM_WIDTH = 15;
	static final float FRUSTUM_HEIGHT = 10;
	GLGraphics glGraphics;
	World world;
	Camera2D cam;
	SpriteBatcher batcher;
	
	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world)
	{
		this.glGraphics = glGraphics;
		this.world = world;
		this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.batcher = batcher;
	}
	
	public void render()
	{
		if(world.zelio.position.x > cam.position.y)
		{
			cam.position.y = world.zelio.position.y;
		}
		cam.setViewportAndMatrices();
		renderBackground();
		renderObjects();
	}
	
	public void renderBackground()
	{
		batcher.beginBatch(Assets.background);
		batcher.drawSprite(cam.position.x, cam.position.y, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, Assets.backgroundRegion);
		batcher.endBatch();
	}
	
	public void renderObjects()
	{
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		renderZelio();
		renderMonsters();
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderZelio()
	{
		TextureRegion keyFrame;
		switch(world.zelio.state)
		{
			case Zelio.ZELIO_STATE_MOVING:
			{
				keyFrame = Assets.zelioAnim.getKeyFrame(world.zelio.stateTime,
						Animation.ANIMATION_LOOPING);
				break;
			}
			case Zelio.ZELIO_STATE_JUMP:
			{
				keyFrame = Assets.zelioJump;
			}
			case Zelio.ZELIO_STATE_SWING:
			{
				keyFrame = Assets.sword.getKeyFrame(world.zelio.stateTime,
						Animation.ANIMATION_NONLOOPING);
				break;
			}
			case Zelio.ZELIO_STATE_DEAD:
			{
				keyFrame = Assets.zelioDead;
				break;
			}
			case Zelio.ZELIO_STATE_STILL:
			default:
			{
				keyFrame = Assets.zelioStill;
			}
		}
		
		float side = world.zelio.velocity.x < 0? -1 : 1;
		batcher.drawSprite(world.zelio.position.x, world.zelio.position.y, side * 1,  1,  keyFrame);
	}
	
	private void renderMonsters()
	{
		int len = world.monsters.size();
		for(int i = 0; i < len; i++)
		{
			SlimeMonster monster = world.monsters.get(i);
			TextureRegion keyFrame = Assets.monster.getKeyFrame(monster.stateTime,
					Animation.ANIMATION_LOOPING);
			float side = monster.velocity.x < 0 ? -1 : 1;
			batcher.drawSprite(monster.position.x, monster.position.y, side * 1, 1, keyFrame);
		}
	}
	
	
	
	
}
