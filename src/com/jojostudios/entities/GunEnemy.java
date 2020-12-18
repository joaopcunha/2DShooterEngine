package com.jojostudios.entities;

import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;
import com.jojostudios.world.Camera;
import com.jojostudios.world.World;

public class GunEnemy  extends Enemy{
	
	protected int shootDistance = 30;
	protected int shootFrames = 0, maxShootFrames = 30;

	public GunEnemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(112+16, 16, 16, 16);
		damagedSprite = Game.spritesheet.getSprite(112+32, 16, 16, 16);
	}
	
	public void shoot() {
		double angle = Math.atan2(Game.player.getY() - (this.getY() + 8), Game.player.getX() - (this.getX() + 8));
		
		double dx = Math.cos(angle);
		double dy = Math.sin(angle);
		int px = 8, py = 8;
		
		Bullet bullet = new EnemyBullet(px+this.getX(), py+this.getY(), 3, 3, null, dx, dy);
		Game.bullets.add(bullet);
	}
	
	public void tick() {
		double currentDistance = this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY());
		if(currentDistance < fieldOfView && currentDistance >= shootDistance) {
			shootFrames++;
			if (shootFrames >= maxShootFrames) {
				shoot();
				shootFrames = 0;
			}
		} else if(currentDistance < shootDistance) {
			if (this.isCollidingWithPlayer() == false) {
				if ((int)x < Game.player.getX() && World.isFree((int)(x+speed), (int)y) &&
						!isColliding((int)(x+speed), (int)y)) {
					x+=speed;
				} else if ((int)x > Game.player.getX() && World.isFree((int)(x-speed), (int)y) &&
						!isColliding((int)(x-speed), (int)y)) {
					x-=speed;
				}
				
				if ((int)y < Game.player.getY() && World.isFree((int)(x), (int)(y+speed)) &&
						!isColliding((int)(x), (int)(y+speed))) {
					y+=speed;
				} else if ((int)y > Game.player.getY() && World.isFree((int)(x), (int)(y-speed)) &&
						!isColliding((int)(x), (int)(y-speed))) {
					y-=speed;
				}
				
				
			} else {
				if (Game.rand.nextInt(100) < 25) {
					if (!Game.player.isInvulnerable) {
						Game.player.life-=5;
						Game.player.isDamaged = true;
					}
				}
			}
		}
		
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
