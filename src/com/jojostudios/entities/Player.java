package com.jojostudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jojostudios.graficos.Spritesheet;
import com.jojostudios.main.Game;
import com.jojostudios.world.Camera;
import com.jojostudios.world.World;

public class Player extends Entity{
	
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1;
	
	private int frames = 0, maxFrames = 10, animationIndex, maxAnimationIndex = 4;
	private boolean moving;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	private boolean hasGun = false;
	private BufferedImage gunRight;
	private BufferedImage gunLeft;
	
	public double life = 100;
	public double maxLife = 100;
	
	public int ammo = 0;
	public boolean isDamaged = false;
	private int damageFrames = 0;

	public Player(int x, int y, int width, int height, Spritesheet spritesheet) {
		super(x, y, width, height, spritesheet);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		playerDamage = spritesheet.getSprite(0, 16, 16, 16);
		
		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = spritesheet.getSprite(32+(i*16), 0, 16, 16);
		}
		
		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = spritesheet.getSprite(32+(i*16), 16, 16, 16);
		}
		
		this.gunRight = spritesheet.getSprite(8*16, 0, 16, 16);
		this.gunLeft = spritesheet.getSprite(9*16, 0, 16, 16);
		
	}
	
	public void tick() {
		moving = false;
		if (right && World.isFree((int)(x+speed), this.getY())) {
			moving = true;
			dir = right_dir;
			x+=speed;
		} 
		
		else if (left && World.isFree((int)(x-speed), this.getY())) {
			moving = true;
			dir = left_dir;
			x-=speed;
		}
		
		if (up && World.isFree(this.getX(), (int)(y-speed))) {
			moving = true;
			y-=speed;
		} 
		
		else if (down && World.isFree(this.getX(), (int)(y+speed))) {
			moving = true;
			y+=speed;
		}
		
		if (moving) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				animationIndex++;
				
				if(animationIndex == maxAnimationIndex) {
					animationIndex = 0;
				}
			}
		}
		
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionWeapon();
		
		if (isDamaged) {
			this.damageFrames ++;
			if (this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if (life <= 0 ) {
			Game.restart();
			return;
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.width*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.height*16 - Game.HEIGHT);
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Ammo) {
				if(Entity.isColliding(this, current)) {
					ammo+=10;
					Game.entities.remove(current);
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof HealthPack) {
				if(Entity.isColliding(this, current)) {
					if (life < maxLife) {
						life+=50;
						if (life > maxLife) {
							life = maxLife;
						}
						Game.entities.remove(current);
					}
				}
			}
		}
	}
	
	public void checkCollisionWeapon() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Weapon) {
				if(Entity.isColliding(this, current)) {
					hasGun = true;
					ammo+=10;
					Game.entities.remove(current);
				}
			}
		}
	}
	
	public void mouseShoot(int mx, int my) {
		double angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x));
		
		double dx = Math.cos(angle);
		double dy = Math.sin(angle);
		int px = 8, py = 8;
		
		if (hasGun && ammo > 0) {
			Bullet bullet = new Bullet(px+this.getX(), py+this.getY(), 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
			ammo--;
		}
	}
	
	public void shoot() {
		int dx = 0;
		int dy = 0;
		int px, py = 8;
		if(dir == right_dir) {
			px = 3;
			dx = 1;
		} else {
			px = -3;
			dx = -1;
		}
		
		if (hasGun && ammo > 0) {
			Bullet bullet = new Bullet(px+this.getX(), py+this.getY(), 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
			ammo--;
		}
		
	}
	
	
	public void render(Graphics g) {
		if (!isDamaged) {
			
			if(dir == right_dir) {
				g.drawImage(rightPlayer[animationIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(gunRight, 5 + this.getX() - Camera.x, 1+ this.getY() - Camera.y, null);
				}
			}
			
			else if(dir == left_dir) {
				g.drawImage(leftPlayer[animationIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(gunLeft, this.getX() - Camera.x - 5, 1 + this.getY() - Camera.y, null);
				}
			}
		} else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
	}

}