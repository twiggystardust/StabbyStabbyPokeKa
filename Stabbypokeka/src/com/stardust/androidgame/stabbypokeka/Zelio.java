package com.stardust.androidgame.stabbypokeka;

import com.stardust.androidgame.framework.DynamicGameObject;

public class Zelio extends DynamicGameObject
{
	public static final int ZELIO_STATE_JUMP = 0;
	public static final int ZELIO_STATE_DEAD = 1;
	public static final int ZELIO_STATE_SWING = 2;
	public static final int ZELIO_STATE_STILL = 3;
	public static final int ZELIO_STATE_MOVING = 4;
	public static final int ZELIO_STATE_SWINGHIT = 5;
	public static final float ZELIO_JUMP_VELOCITY = 9;
	public static final float ZELIO_MOVE_VELOCITY = 20;
	public static final float ZELIO_WIDTH = 1.0f;
	public static final float ZELIO_HEIGHT = 1.0f;
	
	int state;
	float stateTime;
	
	public Zelio(float x, float y)
	{
		super(x, y, ZELIO_WIDTH, ZELIO_HEIGHT);
		state = ZELIO_STATE_STILL;
		stateTime = 0;
	}
	
	public void update(float deltaTime)
	{
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height /2);
		
		stateTime += deltaTime;
	}
	
	public void hitMonster()
	{
		velocity.set(0, 0);
		state = ZELIO_STATE_DEAD;
		stateTime = 0;
	}
}