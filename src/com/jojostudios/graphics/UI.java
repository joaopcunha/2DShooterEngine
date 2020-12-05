package com.jojostudios.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.jojostudios.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(15, 5, 120, 16);
		g.setColor(Color.green);
		g.fillRect(15, 5, (int)((Game.player.life/Game.player.maxLife)*120), 16);
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife, 40, 20);
		
		g.drawString("Ammo: "+Game.player.ammo, 600, 20);
	}
	
	public void renderGameOver(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.setColor(Color.white);
		g.drawString("Game Over", (Game.WIDTH*Game.SCALE/2)-60, (Game.HEIGHT*Game.SCALE/2)-28);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("Press space to continue", (Game.WIDTH*Game.SCALE/2)-76, (Game.HEIGHT*Game.SCALE/2)+10);
	}

}
