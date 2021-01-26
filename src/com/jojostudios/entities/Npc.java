package com.jojostudios.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jojostudios.main.Game;
import com.jojostudios.world.Camera;

public class Npc extends Entity{
	
	public static String[] TUTORIAL_PHRASES_1 = new String[] {"Hello, welcome to the game!", "Use W/S/A/D to move around"};
	public static String[] TUTORIAL_PHRASES_2 = new String[] {"Use your mouse to aim and shoot"};
	public static String[] TUTORIAL_PHRASES_3 = new String[] {"Use E to dodge, pay atention to your stamina"};
	public String[] phrases;
	public boolean talkRange = false;
	public int talkStep = 0;
	public int maxTalkSteps;
	
	public int curIndex = 0;

	public Npc(int x, int y, int width, int height, BufferedImage sprite, String[] phrases) {
		super(x, y, width, height, sprite);
		this.phrases = phrases;
		this.maxTalkSteps = phrases.length;
	}
	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		if(Math.abs(xPlayer-x) < 30 && Math.abs(yPlayer-y) < 30) {
			talkRange = true;
		} else {
			talkRange = false;
			talkStep = 0;
		}
		
		if (talkStep > maxTalkSteps) {
			talkStep = 0;
		} else if (talkStep > 0) {
			if(curIndex < phrases[talkStep-1].length()) {
				curIndex++;
			}
		}
		
	}
	
	public void render(Graphics g) {
		super.render(g);
	}
	 
	public void renderDialog(Graphics g, int xpos, int ypos) {
		if(talkStep > 0 && talkStep <= maxTalkSteps) {
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.setColor(Color.black);
			g.drawString(phrases[talkStep-1].substring(0, curIndex), xpos, ypos);
		}
	}
	
}
