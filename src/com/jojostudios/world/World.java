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
import com.jojostudios.entities.Portal;
import com.jojostudios.entities.Wall;
import com.jojostudios.entities.Weapon;
import com.jojostudios.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int width, height;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		this.generateMap(path);
//		generateRandomMap();
	}
	
	private void generateRandomMap() {
		Game.player.setX(0);
		Game.player.setY(0);
		width = 100;
		height = 100;
		tiles = new Tile[width*height];
		
		for(int xx = 0; xx < width; xx++) {
			for(int yy = 0; yy < height; yy++) {
				tiles[xx+(yy*width)] = new WallTile(xx*16, yy*16);
			}
		}
		
		int dir = 0;
		int xx = 0;
		int yy = 0;
		
		for(int i = 0; i < 500; i++) {
			
			if (i == 2) {
				Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
			}
			
			if(Game.rand.nextInt(100) < 1) {
				Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
			} else if (Game.rand.nextInt(100) < 2) {
				Game.entities.add(new HealthPack(xx*16, yy*16, 16, 16, Entity.HEALTHPACK_EN));
			} else if (Game.rand.nextInt(100) < 3) {
				Game.entities.add(new Ammo(xx*16, yy*16, 16, 16, Entity.AMMO_EN));
			} else if (Game.rand.nextInt(100) < 4) {
				Enemy en = new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN);
				Game.entities.add(en);
				Game.enemies.add(en);
			}
			
			
			tiles[xx+(yy*width)] = new FloorTile(xx*16, yy*16);
			
			if(dir==0) {
				if(xx < width) {
					xx++;
				}
			} else if (dir==1) {
				if(xx > 0) {
					xx--;
				}
			} else if (dir==2) {
				if(yy < height) {
					yy++;
				}
			} else if (dir==3) {
				if(yy > 0) {
					yy--;
				}
			}
			
			if(Game.rand.nextInt(100) < 30) {
				dir = Game.rand.nextInt(4);
			}
			
			
		}
				
	}
	
	private void generateMap(String path) {
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
						Wall wall = new Wall(xx*16, yy*16, 16, 16, Tile.TILE_WALL);
						Game.entities.add(wall);
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
					} else if (pixels[currentPixel] == 0xFF42FF38) {
						if (!Game.entities.stream().anyMatch(x -> x instanceof Portal)) {
							Game.entities.add(new Portal(xx*16, yy*16, 16, 48, Entity.PORTAL_EN));
						}
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
		
		if (x1 > width || x1 < 0 || y1 > height || y1 < 0) {
			return false;
		}
		
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
	
	public static void renderMinimap() {
		for(int i = 0; i < Game.minimapPixels.length; i++) {
			Game.minimapPixels[i] = 0x008000;
		}
		for(int xx = 0; xx < width; xx++) {
			for(int yy = 0; yy < height; yy++) {
				if(tiles[xx+(yy*width)] instanceof WallTile) {
					Game.minimapPixels[xx+(yy*width)] = 0x2B2D2F;
				}
			}
		}
		
		int xPlayer = Game.player.getX()/16;
		int yPlayer = Game.player.getY()/16;
		
		Game.minimapPixels[xPlayer+(yPlayer*width)] = 0x0000ff;
		for(int i = 0; i < Game.enemies.size(); i++) {
			int xEnemy = Game.enemies.get(i).getX()/16;
			int yEnemy = Game.enemies.get(i).getY()/16;
			Game.minimapPixels[xEnemy+(yEnemy*width)] = 0xff0000;
		}
	}

}
