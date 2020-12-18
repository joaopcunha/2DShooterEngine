package com.jojostudios.entities;

import java.awt.image.BufferedImage;

public class EnemyBullet extends Bullet{

	public EnemyBullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite, dx, dy);
		this.spd = 1;
	}

}
