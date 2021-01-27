package com.jojostudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;
import com.jojostudios.world.Camera;
import com.jojostudios.world.World;

public class Bullet extends Entity{
	
	private double dx;
	private double dy;
	protected double spd = 3;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		
		if (this.getX() - Camera.x < 0 || this.getX() - Camera.x > Game.WIDTH) {
			fadeSelf();
		}
		
		if (this.getY() - Camera.y < 0 || this.getY() - Camera.y > Game.HEIGHT) {
			fadeSelf();
		}
		
		if (checkCollisionWall()) {
			destroySelf();
		}
	}
	
	public void destroySelf() {
		World.generateParticle(100, (int)x, (int)y);
		Game.bullets.remove(this);
	}
	
	public void fadeSelf() {
		Game.bullets.remove(this);
	}
	
	public boolean checkCollisionWall() {
		Rectangle bulletCurrent = new Rectangle(this.getX(), this.getY(), width, height);
		for(int i = 0; i <= Game.entities.size()-1; i++) {
			if (Game.entities.get(i) instanceof Wall) {
				Rectangle wall = new Rectangle(Game.entities.get(i).getX(), Game.entities.get(i).getY(), 16, 16);
				if(bulletCurrent.intersects(wall)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	}
	

}
