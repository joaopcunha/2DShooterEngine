package com.jojostudios.entities;

import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;

public class DummyEnemy extends Enemy{

	public DummyEnemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		depth = 0;
		
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			animationIndex++;
			
			if(animationIndex == maxAnimationIndex) {
				animationIndex = 0;
			}
		}
		
		collideBullet();
		
		if(life <= 0) {
			destroySelf();
		}
		
		if (isDamaged) {
			this.damageFrames ++;
			if (this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
	}
}
