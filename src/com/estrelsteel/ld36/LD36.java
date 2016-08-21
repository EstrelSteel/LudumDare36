package com.estrelsteel.ld36;

import java.awt.Graphics2D;

import com.estrelsteel.engine2.Launcher;
import com.estrelsteel.engine2.events.listener.RenderListener;
import com.estrelsteel.engine2.events.listener.StartListener;
import com.estrelsteel.engine2.events.listener.StopListener;
import com.estrelsteel.engine2.events.listener.TickListener;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;
import com.estrelsteel.engine2.window.WindowSettings;


public class LD36 implements StartListener, TickListener, RenderListener, StopListener {
	
	private Launcher l;
	
	public static void main(String[] args) {
		new LD36();
	}
	
	@SuppressWarnings("static-access")
	public LD36() {
		l = new Launcher();
		
		l.getEngine().setWindowSettings(new WindowSettings(QuickRectangle.location(0, 0, 640, 640), "LudumDare 36 - EstrelSteel", "v1.0a", 0));
		
		l.getEngine().START_EVENT.addListener(this);
		l.getEngine().STOP_EVENT.addListener(this);
		l.getEngine().RENDER_EVENT.addListener(this);
		l.getEngine().TICK_EVENT.addListener(this);
		
		l.getEngine().development = true;
		l.getEngine().devPath = System.getProperty("user.home") + "/Documents/usb/LD36/LD36";
		
		l.boot();
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public Graphics2D render(Graphics2D ctx) {
		return ctx;
	}

	@Override
	public void tick() {
		
	}
}
