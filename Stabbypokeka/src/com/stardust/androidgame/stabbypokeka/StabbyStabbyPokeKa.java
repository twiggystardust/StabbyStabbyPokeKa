package com.stardust.androidgame.stabbypokeka;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.stardust.androidgame.framework.Screen;
import com.stardust.androidgame.framework.impl.GLGame;

public class StabbyStabbyPokeKa extends GLGame
{
	boolean firstTimeCreate = true;
	
	public Screen getStartScreen()
	{
		return new MainMenuScreen(this);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		super.onSurfaceCreated(gl, config);
		if(firstTimeCreate)
		{
			Settings.load(getFileIO());
			Assets.load(this);
			firstTimeCreate = true;
		}
		else
		{
			Assets.reload();
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		if(Settings.soundEnabled)
		{
			Assets.music.pause();
		}
	}
}
