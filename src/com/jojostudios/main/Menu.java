package com.jojostudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Menu {
	
	public String[] options;
	public int currentOption;
	public int maxOption;
	public boolean up, down, enter;
	
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public Menu() {
		options = new String[] {"New game", "Load game", "Exit"};
		currentOption = 0;
		maxOption = options.length - 1;
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i]-=encode;
							trans[1]+=val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			
			try {
				write.write(current);
				if (i < val1.length -1) {
					write.newLine();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			write.flush();
			write.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			
			switch(spl2[0]) 
			{
			case "level":
				Game.changeLevel(Integer.parseInt(spl2[1]));
				Game.gameState = "normal";
				break;
			case "life":
				Game.player.life = Double.parseDouble(spl2[1]);
				break;
			}
		}
	}
	
	public void tick() {
		if (up) {
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
			up = false;
		}
		
		if (down) {
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
			down = false;
		}
		
		if (enter) {
			if(options[currentOption] == "New game" || options[currentOption] == "Continue") {
				Game.gameState = "normal";
			}
			
			if (options[currentOption] == "Save game") {
				String[] opt1 = {"level", "life"};
				int[] opt2 = {Game.CUR_LEVEL, (int)Game.player.life};
				saveGame(opt1, opt2, 10);
			}
			
			if (options[currentOption] == "Load game") {
				String saver = loadGame(10);
				applySave(saver);
			}
			
			if(options[currentOption] == "Exit") {
				System.exit(1);;
			}
			enter = false;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.green);
		g.setFont(new Font("arial", Font.BOLD, 66));
		g.drawString("Ghost Hunter", (Game.WIDTH*Game.SCALE/2)-240, (Game.HEIGHT*Game.SCALE/2)-138);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 34));

		for (int i=0; i<=maxOption; i++) {
			int yOffeset = 80*i;
			g.drawString(options[i], (Game.WIDTH*Game.SCALE/2)-200, (Game.HEIGHT*Game.SCALE/2)+yOffeset);
			if (currentOption == i) {
				g.drawString(">", (Game.WIDTH*Game.SCALE/2)-230, (Game.HEIGHT*Game.SCALE/2)+yOffeset);
			}
		}
	}
	

}
