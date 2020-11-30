package com.jojostudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;
import com.jojostudios.world.Camera;

public class Bullet extends Entity{
	
	private double dx;
	private double dy;
	private double spd = 3;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
		
		if (this.getX() - Camera.x < 0 || this.getX() - Camera.x > Game.WIDTH) {
			destroySelf();
		}
		
		if (this.getY() - Camera.y < 0 || this.getY() - Camera.y > Game.HEIGHT) {
			destroySelf();
		}
	}
	
	public void destroySelf() {
		Game.bullets.remove(this);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	}
	

}
