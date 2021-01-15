package com.jojostudios.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import com.jojostudios.graphics.Spritesheet;
import com.jojostudios.main.Game;
import com.jojostudios.world.Camera;
import com.jojostudios.world.Node;

public class Entity {
	
	public static BufferedImage HEALTHPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage AMMO_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage WEAPON_LEFT = Game.spritesheet.getSprite(8*16, 0, 16, 16);
	public static BufferedImage WEAPON_RIGHT = Game.spritesheet.getSprite(9*16, 0, 16, 16);
	public static BufferedImage PORTAL_EN = Game.spritesheet.getSprite(0, 32, 16, 48);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	protected BufferedImage sprite;
	private Spritesheet spritesheet;
	
	private int maskx, masky, maskw, maskh;
	
	public int depth;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}
	
	public Entity(int x, int y, int width, int height, Spritesheet spritesheet) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.spritesheet = spritesheet;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}
	
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		@Override
		public int compare(Entity n0, Entity n1) {
			if(n1.depth < n0.depth) {
				return +1;
			}
			if (n1.depth > n0.depth) {
				return -1;
			}
			return 0;
		}
	};
	
	public void setMask(int maskx, int masky, int maskw, int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.maskw, e1.maskh);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.maskw, e2.maskh);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(this.sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

}
