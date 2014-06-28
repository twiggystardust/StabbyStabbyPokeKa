package com.stardust.androidgame.stabbypokeka;

import com.stardust.androidgame.framework.DynamicGameObject;
import com.stardust.androidgame.framework.math.Vector2;

public class SlimeMonster extends DynamicGameObject
{
	public static final float MONSTER_WIDTH = 1.0f;
	public static final float MONSTER_HEIGHT = 1.0f;
	public static final float MONSTER_VELOCITY = 3f;
	
	Vector2 origPos = new Vector2(0, 0);
	
	float stateTime = 0;
	
	public SlimeMonster(float x, float y)
	{
		super(x, y, MONSTER_WIDTH, MONSTER_HEIGHT);
		velocity.set(MONSTER_VELOCITY, 0);
		origPos.x = x;
		origPos.y = y;
	}
	
	public void update(float deltaTime)
	{
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.lowerLeft.set(position).sub(MONSTER_WIDTH / 2, MONSTER_HEIGHT / 2);
		
		if(position.x < origPos.x + 10)
		{
			position.x = origPos.x + 10;
			velocity.x = velocity.x;
		}
		
		if(position.x > origPos.x - 10)
		{
			position.x = origPos.x - 10;
			velocity.x = -velocity.x;
		}
		
		stateTime += deltaTime;
	}
		
		
	
}
