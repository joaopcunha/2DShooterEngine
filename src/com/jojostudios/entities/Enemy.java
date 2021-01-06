package com.jojostudios.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;
import com.jojostudios.world.Camera;
import com.jojostudios.world.World;

public class Enemy extends Entity{
	
	protected double speed = 0.3;
	protected int fieldOfView = 80;
	
	protected int maskx=8, masky=8, maskw=10, maskh=10;
	protected int frames = 0, maxFrames = 10, animationIndex, maxAnimationIndex = 2;

	protected BufferedImage[] sprites;
	protected BufferedImage damagedSprite;
	protected int damageFrames = 0;
	
	protected int life = 10;
	protected boolean isDamaged = false;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(112+16, 16, 16, 16);
		damagedSprite = Game.spritesheet.getSprite(112+32, 16, 16, 16);
	}
	
	public void tick() {
		depth = 0;
		if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < fieldOfView) {
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
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx, this.getY()+masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public void collideBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof PlayerBullet) {
				if(Entity.isColliding(this, e)) {
					isDamaged = true;
					life-=5;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext+maskx, ynext+masky, maskw, maskh);
		
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX()+maskx, e.getY()+masky, maskw, maskh);
			
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		if (!isDamaged) {
			g.drawImage(sprites[animationIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(damagedSprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
	}

}
