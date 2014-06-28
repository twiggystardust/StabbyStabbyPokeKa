package com.stardust.androidgame.stabbypokeka;

import com.stardust.androidgame.framework.Music;
import com.stardust.androidgame.framework.Sound;
import com.stardust.androidgame.framework.gl.Animation;
import com.stardust.androidgame.framework.gl.Font;
import com.stardust.androidgame.framework.gl.Texture;
import com.stardust.androidgame.framework.gl.TextureRegion;
import com.stardust.androidgame.framework.impl.GLGame;

public class Assets
{
	public static Texture background;
	public static TextureRegion backgroundRegion;
	
	public static Texture items;
	public static TextureRegion mainMenu;
	public static TextureRegion pauseMenu;
	public static TextureRegion ready;
	public static TextureRegion gameOver;
	public static TextureRegion highScoresRegion;
	public static TextureRegion logo;
	public static TextureRegion soundOn;
	public static TextureRegion soundOff;
	public static TextureRegion arrow;
	public static TextureRegion pause;
	public static TextureRegion swordButton;
	public static TextureRegion jumpButton;
	public static TextureRegion zelioStill;
	public static TextureRegion zelioDead;
	public static TextureRegion zelioJump;
	public static Animation zelioAnim;
	public static Animation sword;
	public static Animation monster;
	public static Font font;
	
	public static Music music;
	
	public static Sound jumpSound;
	public static Sound swordSwing;
	public static Sound swordHit;
	public static Sound zelioDie;
	public static Sound click;
	
	public static void load(GLGame game)
	{
		background = new Texture(game, "background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 480, 320);
		
		items = new Texture(game, "spriteSheet.png");
		
		mainMenu = new TextureRegion(items, 0, 416, 240, 96);
		pauseMenu = new TextureRegion(items, 256, 288, 192, 64);
		ready = new TextureRegion(items, 256, 256, 160, 32);
		gameOver = new TextureRegion(items, 256, 352, 128, 64);
		highScoresRegion = new TextureRegion(items, 0, 448, 240, 32);
		logo = new TextureRegion(items, 0, 96, 240, 320);
		soundOn = new TextureRegion(items, 320, 448, 64, 64);
		soundOff = new TextureRegion(items, 384, 448, 64, 64);
		arrow = new TextureRegion(items, 448, 320, 64, 64);
		pause = new TextureRegion(items, 448, 256, 64, 64);
		swordButton = new TextureRegion(items, 448, 448, 64, 64);
		jumpButton = new TextureRegion(items, 448, 384, 64, 64);
		zelioStill = new TextureRegion(items, 0, 0, 32, 32);
		zelioDead = new TextureRegion(items, 128, 0, 32, 32);
		zelioJump = new TextureRegion(items, 192, 32, 32, 32);
		
		zelioAnim = new Animation(0.2f,
									new TextureRegion(items, 32, 0, 32, 32),
									new TextureRegion(items, 64 ,0, 32, 32),
									new TextureRegion(items, 96, 0, 32, 32));
		sword = new Animation(0.2f,
									new TextureRegion(items, 0, 32, 64, 32),
									new TextureRegion(items, 64, 32, 64, 32),
									new TextureRegion(items, 96, 32, 64, 32));
		monster = new Animation(0.2f,
									new TextureRegion(items, 160, 0, 32, 32),
									new TextureRegion(items, 192, 0, 32, 32));
		
		font = new Font(items, 256, 0, 8, 32, 32);
		
		music = game.getAudio().newMusic("aGameSong.wav");
		music.setLooping(true);
		music.setVolume(10.0f);
		if(Settings.soundEnabled)
		{
			music.play();
		}
		jumpSound = game.getAudio().newSound("aGameJump.ogg");		
		swordSwing = game.getAudio().newSound("aGameSwordSwing.wav");		
		swordHit = game.getAudio().newSound("aGameSwordHit.ogg");
		zelioDie = game.getAudio().newSound("aGameDie.ogg");
		click = game.getAudio().newSound("aGameClick.ogg");
		
	}
	
	public static void reload()
	{
		background.reload();
		items.reload();
		if(Settings.soundEnabled)
		{
			music.play();
		}
	}
	
	public static void playSound(Sound sound)
	{
		if(Settings.soundEnabled)
		{
			sound.play(1);
		}
	}
}
