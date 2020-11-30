package com.jojostudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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

}
