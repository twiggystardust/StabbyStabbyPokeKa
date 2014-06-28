package com.stardust.androidgame.framework.impl;

import com.stardust.androidgame.framework.Game;
import com.stardust.androidgame.framework.Screen;

public abstract class GLScreen extends Screen
{
	protected final GLGraphics glGraphics;
	protected final GLGame glGame;
	
	public GLScreen(Game game)
	{
		super(game);
		glGame = (GLGame)game;
		glGraphics = glGame.getGLGraphics();
	}
}
