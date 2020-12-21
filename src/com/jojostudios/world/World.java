package com.jojostudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jojostudios.entities.Ammo;
import com.jojostudios.entities.Enemy;
import com.jojostudios.entities.Entity;
import com.jojostudios.entities.GunEnemy;
import com.jojostudios.entities.HealthPack;
import com.jojostudios.entities.Weapon;
import com.jojostudios.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int width, height;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			width = map.getWidth();
			height = map.getHeight();
			
			int[] pixels = new int[width * height];
			tiles = new Tile[width * height];
			map.getRGB(0, 0, width, height, pixels, 0, width);
			for(int xx = 0; xx < width; xx++) {
				for (int yy = 0; yy < height ; yy++) {
					int currentPixel = xx + (yy*width);
					
					// Wall or Floor?
					if (pixels[currentPixel] == 0xFFFFFFFF) {
						tiles[currentPixel] = new WallTile(xx*16, yy*16);
					} else {
						tiles[currentPixel] = new FloorTile(xx*16, yy*16);
					}
					
					if (pixels[currentPixel] == 0xFF0026FF) {
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					} else if (pixels[currentPixel] == 0xFFFF0000) {
						Enemy en = new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if (pixels[currentPixel] == 0xFFFF6A00) {
						Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
					} else if (pixels[currentPixel] == 0xFFFF7FED) {
						Game.entities.add(new HealthPack(xx*16, yy*16, 16, 16, Entity.HEALTHPACK_EN));
					} else if (pixels[currentPixel] == 0xFFFFD800) {
						Game.entities.add(new Ammo(xx*16, yy*16, 16, 16, Entity.AMMO_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1)/TILE_SIZE;
		int y2 = (ynext)/TILE_SIZE;
		
		int x3 = (xnext)/TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1)/TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1)/TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1)/TILE_SIZE;
		
		return !(tiles[x1+(y1*width)] instanceof WallTile ||
				tiles[x2+(y2*width)] instanceof WallTile ||
				tiles[x3+(y3*width)] instanceof WallTile ||
				tiles[x4+(y4*width)] instanceof WallTile);
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x/16;
		int ystart = Camera.y/16;
		
		int xfinal = xstart + (Game.WIDTH/16);
		int yfinal = ystart + (Game.HEIGHT/16);
		
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= width || yy >= height) {
					continue;
				}
				Tile tile = tiles[xx + (yy*width)];
				tile.render(g);
			}
		}
	}

}
