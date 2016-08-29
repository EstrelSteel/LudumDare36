package com.estrelsteel.ld36;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.estrelsteel.engine2.Engine2;
import com.estrelsteel.engine2.Launcher;
import com.estrelsteel.engine2.events.listener.RenderListener;
import com.estrelsteel.engine2.events.listener.StartListener;
import com.estrelsteel.engine2.events.listener.StopListener;
import com.estrelsteel.engine2.events.listener.TickListener;
import com.estrelsteel.engine2.file.GameFile;
import com.estrelsteel.engine2.grid.PixelGrid;
import com.estrelsteel.engine2.image.Image;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;
import com.estrelsteel.engine2.sound.SFX;
import com.estrelsteel.engine2.window.WindowSettings;
import com.estrelsteel.engine2.world.World;
import com.estrelsteel.ld36.mail.Mail;
import com.estrelsteel.ld36.mail.MailType;
import com.estrelsteel.ld36.mail.PendingMail;
import com.estrelsteel.ld36.station.Level;
import com.estrelsteel.ld36.station.Station;
import com.estrelsteel.ld36.tube.PendingTube;
import com.estrelsteel.ld36.tube.Tube;


public class LD36 implements StartListener, TickListener, RenderListener, StopListener {

	/*
	 * 
	 * PNEUMATIC PIPE MANAGER
	 * 
	 * BY: ESTRELSTEEL
	 * LUDUM DARE 36 - COMPO
	 * 
	 * THEME: ANCIENT TECHNOLOGY
	 * 
	 * (28.08.2016)
	 * 
	 */
	
	
	private Launcher l;
	private World world;
	public static double score;
	public int tubes;
	private InputHandler input;
	public int lvl = 0;
	private Mail tempM;
	private Tube tempT;
	public int start;
	private Image map;
	private double tubeAdd;
	private Random rand;
	public static boolean music;
	public static boolean sfx;
	public static SFX effect;
	public boolean paused;
	private ArrayList<Image> maps;
	
	public static void main(String[] args) {
		new LD36();
	}
	
	@SuppressWarnings("static-access")
	public LD36() {
		l = new Launcher();
		
		l.getEngine().setWindowSettings(new WindowSettings(QuickRectangle.location(0, 0, 640, 640), "Pneumatic Pipe Manager", "v1.0b", 1));
		
		l.getEngine().START_EVENT.addListener(this);
		l.getEngine().STOP_EVENT.addListener(this);
		l.getEngine().RENDER_EVENT.addListener(this);
		l.getEngine().TICK_EVENT.addListener(this);
		
		input = new InputHandler(this);
		
		l.getEngine().addKeyListener(input);
		l.getEngine().addMouseListener(input);
		l.getEngine().addMouseMotionListener(input);
		
		l.getEngine().development = true;
//		l.getEngine().devPath = System.getProperty("user.home") + "/Documents/usb/LD36/LD36";
		l.getEngine().devPath = GameFile.getCurrentPath();
		MailType.updateSRC();
		
		rand = new Random();
		
		music = true;
		sfx = true;
		paused = false;
		
		GameFile sound = new GameFile(Engine2.devPath + "/res/sounds.txt");
		try {
			sound.updateLines();
			effect = new SFX();
			effect.load(sound, 0);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		l.boot();
	}
	
	public World getWorld() {
		return world;
	}
	
	public Launcher getLauncher() {
		return l;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void start() {
		score = 0;
		tubes = 0;
		start = Integer.MIN_VALUE;
		world = new World(new PixelGrid());
		GameFile levels = new GameFile(l.getEngine().devPath + "/lvl/lvls.txt");
		try {
			levels.updateLines();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		Level lvl;
		String[] args;
		GameFile lvlFile;
		Station station = new Station(MailType.UNKNOWN, QuickRectangle.location(0, 0, 0, 0));
		PendingMail pMail = new PendingMail(0);
		PendingTube pTube = new PendingTube(0);
		maps = new ArrayList<Image>();
		for(String line : levels.getLines()) {
			args = line.split(" ");
			lvl = new Level(args[0]);
			maps.add(new Image(Engine2.devPath + args[2].trim()));
			lvlFile = new GameFile(l.getEngine().devPath + "/lvl" + args[1]);
			try {
				lvlFile.updateLines();
				for(int i = 0; i < lvlFile.getLines().size(); i++) {
					station = station.load(lvlFile, i);
					if(station != null) {
						lvl.getStations().add(station.load(lvlFile, i));
					}
					else {
						station = new Station(MailType.UNKNOWN, QuickRectangle.location(0, 0, 0, 0));
						pMail = pMail.load(lvlFile, i);
						if(pMail != null) {
							lvl.getPendingMail().add(pMail);
						}
						else {
							pMail = new PendingMail(0);
							pTube = pTube.load(lvlFile, i);
							if(pTube != null) {
								lvl.getPendingTube().add(pTube);
							}
						}
					}
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			world.getObjects().add(lvl);
		}
		map = maps.get(0);
		Note note = new Note(QuickRectangle.location(0, 0, 0, 0));
		GameFile notes = new GameFile(l.getEngine().devPath + "/notes/notes.txt");
		GameFile n;
		try {
			notes.updateLines();
			for(int i = 0; i < notes.getLines().size(); i++) {
				n = new GameFile(l.getEngine().devPath + "/notes" + notes.getLines().get(i));
				n.updateLines();
				note = note.load(n, 0);
				world.getObjects().add(note);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if(world.getObjects().get(1) instanceof Note) {
			((Note) world.getObjects().get(1)).setVisible(true);
		}
	}

	@Override
	public void stop() {
		
	}

	@Override
	public Graphics2D render(Graphics2D ctx) {
		if(!map.isImageLoaded()) {
			map.loadImage();
		}
		ctx.drawImage(map.getImage(), 0, 0, 640, 640, null);
		if(input.drawingTube) {
			ctx.setStroke(new BasicStroke(5));
			ctx.setColor(Color.GREEN);
			ctx.drawLine((int) (input.start.getLocation().getWidth() / 2 + input.start.getLocation().getX()), (int) (input.start.getLocation().getHeight() / 2 + input.start.getLocation().getY()), input.mouseX, input.mouseY);
		}
		else if(input.drawingDel) {
			ctx.setStroke(new BasicStroke(5));
			ctx.setColor(Color.RED);
			ctx.drawLine((int) (input.start.getLocation().getWidth() / 2 + input.start.getLocation().getX()), (int) (input.start.getLocation().getHeight() / 2 + input.start.getLocation().getY()), input.mouseX, input.mouseY);
		}
		world.simpleRenderWorld(ctx);
		ctx.setFont(new Font("Menlo", Font.BOLD, 16));
		ctx.setColor(Color.BLACK);
		if(world.getObjects().size() > 0) {
			if(world.getObjects().get(1) instanceof Note && world.getObjects().get(2) instanceof Note && world.getObjects().get(3) instanceof Note) {
				if(!((Note) world.getObjects().get(1)).isVisible() && !((Note) world.getObjects().get(2)).isVisible()
						&& !((Note) world.getObjects().get(3)).isVisible()) {
					ctx.drawString("Avaliable Pipes: " + (tubes - ((Level) world.getObjects().get(lvl)).calcTotalTubes()), 20, 600);
					ctx.drawString("Deliveries: " + (int) score, 475, 600);
				}
			}
			
//			ctx.drawString("Time: " + (l.getEngine().getStats().getTicks() - start), 475, 620);
		}
		return ctx;
	}

	@Override
	public void tick() {
		
		if(world.getObjects().size() <= 0) {
			return;
		}
		if(music && !((Note) world.getObjects().get(2)).isVisible()) {
			SFX.getSounds().get(1).play();
		}
		else {
			if(SFX.getSounds().get(1).getClip().isRunning()) {
				SFX.getSounds().get(1).getClip().setFramePosition(0);
				SFX.getSounds().get(1).getClip().stop();
				SFX.getSounds().get(1).getClip().flush();
			}
		}
		if(paused) {
			return;
		}
		for(int i = 0; i < ((Level) world.getObjects().get(lvl)).getTubes().size(); i++) {
			for(int j = 0; j < ((Level) world.getObjects().get(lvl)).getTubes().get(i).getMail().size(); j++) {
				((Level) world.getObjects().get(lvl)).getTubes().get(i).getMail().get(j).getAnimations().get(0).run();
			}
		}
		for(int i = 0; i < ((Level) world.getObjects().get(lvl)).getStations().size(); i++) {
			for(int j = 0; j < ((Level) world.getObjects().get(lvl)).getStations().get(i).getMail().size(); j++) {
				((Level) world.getObjects().get(lvl)).getStations().get(i).getMail().get(j).getAnimations().get(0).run();
			}
		}
		
		if(l.getEngine().getStats().getTicks() % 60 == 0) {
			for(int i = 0; i < ((Level) world.getObjects().get(lvl)).getTubes().size(); i++) {
				((Level) world.getObjects().get(lvl)).getTubes().get(i).processMail();
			}
			for(int i = 0; i < ((Level) world.getObjects().get(lvl)).getStations().size(); i++) {
				for(int j = 0; j < ((Level) world.getObjects().get(lvl)).getStations().get(i).getMail().size(); j++) {
					tempM = ((Level) world.getObjects().get(lvl)).getStations().get(i).getMail().get(j);
					if(!tempM.hasMoved()) {
						tempT = ((Level) world.getObjects().get(lvl)).findTubeWithDestination(tempM.getStart(), tempM.getType());
						if(tempT != null) {
							tempM.setTravelTime(System.currentTimeMillis());
							tempT.getMail().add(tempM);
							((Level) world.getObjects().get(lvl)).getStations().get(i).getMail().remove(j);
							j--;
						}
					}
				}
			}
			for(int i = 0; i < ((Level) world.getObjects().get(lvl)).getTubes().size(); i++) {
				for(int j = 0; j < ((Level) world.getObjects().get(lvl)).getTubes().get(i).getMail().size(); j++) {
					((Level) world.getObjects().get(lvl)).getTubes().get(i).getMail().get(j).setMoved(false);
				}
			}
			for(int i = 0; i < ((Level) world.getObjects().get(lvl)).getStations().size(); i++) {
				for(int j = 0; j < ((Level) world.getObjects().get(lvl)).getStations().get(i).getMail().size(); j++) {
					((Level) world.getObjects().get(lvl)).getStations().get(i).getMail().get(j).setMoved(false);
				}
			}
		}
		if(((Level) world.getObjects().get(lvl)).getPendingMail().size() > 0) {
			if(((Level) world.getObjects().get(lvl)).getPendingMail().get(0).getSpawn() <= l.getEngine().getStats().getTicks() - start) {
				((Level) world.getObjects().get(lvl)).spawnMail(null);
				((Level) world.getObjects().get(lvl)).getPendingMail().remove(0);
			}
		}
		else if(((Level) world.getObjects().get(lvl)).getPendingMail().size() <= 0) {
			if((l.getEngine().getStats().getTicks() - start) % 480 == 0) {
				int y = (int) (0.99 * Math.pow(1.015, (l.getEngine().getStats().getTicks() - start) / 240)) + 15;
				Station s = null;

				if(rand.nextDouble() < 0.15) {
					s = ((Level) world.getObjects().get(lvl)).getStations().get((int) (rand.nextDouble() * ((Level) world.getObjects().get(lvl)).getActiveStation()));
				}
				while(y > 0) {
					((Level) world.getObjects().get(lvl)).spawnMail(s);
					y--;
				}
			}
		}
		if(((Level) world.getObjects().get(lvl)).getActiveStation() + 1 < ((Level) world.getObjects().get(lvl)).getStations().size()) {
			if(((Level) world.getObjects().get(lvl)).getStations().get(((Level) world.getObjects().get(lvl)).getActiveStation() + 1).getRequiredMail() <= score) {
				((Level) world.getObjects().get(lvl)).setActiveStation(((Level) world.getObjects().get(lvl)).getActiveStation() + 1);
			}
		}
		if(((Level) world.getObjects().get(lvl)).getPendingTube().size() > 0) {
			if(score >= ((Level) world.getObjects().get(lvl)).getPendingTube().get(0).getSpawn()) {
				((Level) world.getObjects().get(lvl)).getPendingTube().remove(0);
				tubes++;
			}
		}
		else if(((Level) world.getObjects().get(lvl)).getPendingTube().size() <= 0) {
			if(score - tubeAdd > 50) {
				tubes++;
				tubeAdd = score;
			}
		}
		
		if(((Level) world.getObjects().get(lvl)).checkAvaliableMail() - ((Level) world.getObjects().get(lvl)).getPendingMail().size() <= 0) {
			int y = (int) (0.99 * Math.pow(1.015, (l.getEngine().getStats().getTicks() - start) / 240));
			Station s = null;
			if(rand.nextDouble() < 0.15) {
				s = ((Level) world.getObjects().get(lvl)).getStations().get((int) (rand.nextDouble() * ((Level) world.getObjects().get(lvl)).getActiveStation()));
			}
			while(y > 0) {
				((Level) world.getObjects().get(lvl)).spawnMail(s);
				y--;
			}
		}
		
		if(((Level) world.getObjects().get(lvl)).checkForExpiredMail()) {
			if(world.getObjects().get(2) instanceof Note) {
				if(!((Note) world.getObjects().get(2)).isVisible()) {
					((Note) world.getObjects().get(2)).setVisible(true);
					((Note) world.getObjects().get(2)).getText().get(1).setText(((Note) world.getObjects().get(2)).getText().get(1).getText() + (int) (score));
					if(music) {
						SFX.getSounds().get(1).getClip().stop();
						SFX.getSounds().get(4).play();
					}
				}
			}
		}
		
	}
}
