package com.jojostudios.entities;

import java.awt.image.BufferedImage;

public class PlayerBullet extends Bullet{

	public PlayerBullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite, dx, dy);
	}

}
