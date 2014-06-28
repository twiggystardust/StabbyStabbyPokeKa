package com.stardust.androidgame.framework;

import com.stardust.androidgame.framework.Graphics.PixmapFormat;

public interface Pixmap
{
	public int getWidth();
	
	public int getHeight();
	
	public PixmapFormat getFormat();
	
	public void dispose();
}
