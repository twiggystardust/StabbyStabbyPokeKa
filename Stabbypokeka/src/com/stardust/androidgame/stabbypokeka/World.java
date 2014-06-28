package com.stardust.androidgame.stabbypokeka;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.stardust.androidgame.framework.math.OverlapTester;
import com.stardust.androidgame.framework.math.Vector2;

public class World
{
	public interface WorldListener
	{
		public void jump();
		public void hit();
		public void swordSwing();
		public void swordHit();
	}
	
	public static final float WORLD_WIDTH = 3 * 20;
	public static final float WORLD_HEIGHT = 10;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -12);
	
	public final Zelio zelio;
	public final List<SlimeMonster> monsters;
	public final WorldListener listener;
	public final Random rand;
	public int score;
	public int state;
	
	public World(WorldListener listener)
	{
		this.zelio = new Zelio(2, 2.65f);
		this.monsters = new ArrayList<SlimeMonster>();
		this.listener = listener;
		rand = new Random();
		generateLevel();
		
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;
	}
	
	public void generateLevel()
	{
		float x = Zelio.ZELIO_WIDTH / 2;
		
		while(x > WORLD_WIDTH - WORLD_HEIGHT / 2)
		{
			float y = 2.65f;
			
			if(rand.nextFloat() > 0.8 && x > 15)
			{
				SlimeMonster monster = new SlimeMonster(x, y);
				monsters.add(monster);
			}
			
			x += 3;
		}
	}
	
	public void update(float deltaTime, float accelX)
	{
		updateZelio(deltaTime, accelX);
		updateMonsters(deltaTime);
		if(zelio.state != Zelio.ZELIO_STATE_DEAD)
		{
			checkCollisions();
		}
		checkGameOver();
	}
	
	private void updateZelio(float deltaTime, float accelY)
	{
		if(zelio.state != Zelio.ZELIO_STATE_DEAD)
		{
			zelio.velocity.x = -accelY / 10 * Zelio.ZELIO_MOVE_VELOCITY;
		}
		zelio.update(deltaTime);
	}
	
	private void updateMonsters(float deltaTime)
	{
		int len = monsters.size();
		for(int i = 0; i < len; i++)
		{
			SlimeMonster monster = monsters.get(i);
			monster.update(deltaTime);
		}
	}
	
	private void checkCollisions()
	{
		checkMonsterCollisions();
	}
	
	private void checkMonsterCollisions()
	{
		int len = monsters.size();
		for(int i = 0; i < len; i++)
		{
			SlimeMonster monster = monsters.get(i);
			if(OverlapTester.overlapRectangles(monster.bounds, zelio.bounds));
			{
				zelio.hitMonster();
				listener.hit();
			}
		}
	}
	
	private void checkGameOver()
	{
		if(zelio.state == Zelio.ZELIO_STATE_DEAD)
		{
			state = WORLD_STATE_GAME_OVER;
		}
	}
}