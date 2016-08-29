package com.estrelsteel.ld36.tube;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.estrelsteel.engine2.actor.Actor;
import com.estrelsteel.engine2.point.AbstractedPoint;
import com.estrelsteel.engine2.point.PointMaths;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;
import com.estrelsteel.engine2.shape.rectangle.Rectangle;
import com.estrelsteel.engine2.sound.SFX;
import com.estrelsteel.engine2.world.FrozenWorld;
import com.estrelsteel.ld36.LD36;
import com.estrelsteel.ld36.mail.Mail;
import com.estrelsteel.ld36.station.CapacityColours;
import com.estrelsteel.ld36.station.Station;

public class Tube extends Actor {

	private Station[] stations;
	private long travelTime;
	private ArrayList<Mail> mail;
	private int capacity;
	
	public Tube(String name, Rectangle loc) {
		super(name, loc);
		this.stations = new Station[2];
		this.mail = new ArrayList<Mail>();
		this.capacity = 1;
	}
	
	public Station[] getStations() {
		return stations;
	}
	
	public long getTravelTime() {
		return travelTime;
	}
	
	public ArrayList<Mail> getMail() {
		return mail;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void processMail() {
		for(int i = 0; i < mail.size(); i++) {
			if(System.currentTimeMillis() - mail.get(i).getTravelTime() >= travelTime) {
				if(!stations[0].equals(mail.get(i).getStart())) {
					if(mail.get(i).getType() == stations[0].getType()) {
						LD36.score++;
						if(LD36.sfx && !LD36.music) {
							SFX.getSounds().get(0).play();
						}
					}
					else {
						mail.get(i).setStart(stations[0]);
						mail.get(i).setLocation(QuickRectangle.location(stations[0].getLocation().getX() + stations[0].getLocation().getWidth() + 5,
								stations[0].getLocation().getY() + (20 * stations[0].getMail().size()), 16, 16));
						mail.get(i).setMoved(true);
						stations[0].getMail().add(mail.get(i));
					}
					mail.remove(i);
					i--;
				}
				else {
					if(mail.get(i).getType() == stations[1].getType()) {
						LD36.score++;
						if(LD36.sfx && !LD36.music) {
							SFX.getSounds().get(0).play();
						}
					}
					else {
						mail.get(i).setStart(stations[1]);
						mail.get(i).setLocation(QuickRectangle.location(stations[1].getLocation().getX() + stations[1].getLocation().getWidth() + 5,
								stations[1].getLocation().getY() + (20 * stations[1].getMail().size()), 16, 16));
						mail.get(i).setMoved(true);
						stations[1].getMail().add(mail.get(i));
						
					}
					mail.remove(i);
					i--;
				}
			}
		}
	}
	
	@Override
	public Graphics2D render(Graphics2D ctx, FrozenWorld world) {
		if(capacity >= CapacityColours.values().length) {
			ctx.setColor(Color.BLACK);
		}
		else {
			ctx.setColor(CapacityColours.values()[capacity].getColour());
		}
		ctx.setStroke(new BasicStroke(5));
		ctx.drawLine((int) (stations[0].getLocation().getWidth() / 2 + stations[0].getLocation().getX()), (int) (stations[0].getLocation().getHeight() / 2 + stations[0].getLocation().getY()), 
				(int) (stations[1].getLocation().getWidth() / 2 + stations[1].getLocation().getX()), (int) (stations[1].getLocation().getHeight() / 2 + stations[1].getLocation().getY()));
		for(int i = 0; i < mail.size(); i++) {
			AbstractedPoint point = PointMaths.getMidpoint(new AbstractedPoint((stations[0].getLocation().getWidth() / 2 + stations[0].getLocation().getX()), 
						(stations[0].getLocation().getHeight() / 2 + stations[0].getLocation().getY())), 
					new AbstractedPoint((stations[1].getLocation().getWidth() / 2 + stations[1].getLocation().getX()), 
						(stations[1].getLocation().getHeight() / 2 + stations[1].getLocation().getY())));
			mail.get(i).setLocation(QuickRectangle.location(point.getX() + (20 * i), point.getY(), 16, 16));
			mail.get(i).render(ctx, world);
		}
		return ctx;
	}

	@Override
	public Graphics2D simpleRender(Graphics2D ctx, FrozenWorld world) {
		if(capacity >= CapacityColours.values().length) {
			ctx.setColor(Color.BLACK);
		}
		else {
			ctx.setColor(CapacityColours.values()[capacity].getColour());
		}
		ctx.setStroke(new BasicStroke(5));
		ctx.drawLine((int) (stations[0].getLocation().getWidth() / 2 + stations[0].getLocation().getX()), (int) (stations[0].getLocation().getHeight() / 2 + stations[0].getLocation().getY()), 
				(int) (stations[1].getLocation().getWidth() / 2 + stations[1].getLocation().getX()), (int) (stations[1].getLocation().getHeight() / 2 + stations[1].getLocation().getY()));
		for(int i = 0; i < mail.size(); i++) {
			AbstractedPoint point = PointMaths.getMidpoint(new AbstractedPoint((stations[0].getLocation().getWidth() / 2 + stations[0].getLocation().getX()), 
						(stations[0].getLocation().getHeight() / 2 + stations[0].getLocation().getY())), 
					new AbstractedPoint((stations[1].getLocation().getWidth() / 2 + stations[1].getLocation().getX()), 
						(stations[1].getLocation().getHeight() / 2 + stations[1].getLocation().getY())));
			mail.get(i).setLocation(QuickRectangle.location(point.getX() + (20 * i), point.getY(), 16, 16));
			mail.get(i).simpleRender(ctx, world);
		}
		return ctx;
	}
	
	public void setStations(Station[] stations) {
		this.stations = stations;
	}
	
	public void setTravelTime(long travelTime) {
		this.travelTime = travelTime;
	}
	
	public void setMail(ArrayList<Mail> mail) {
		this.mail = mail;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}
