package com.estrelsteel.ld36.mail;

import com.estrelsteel.engine2.actor.Actor;
import com.estrelsteel.engine2.image.Animation;
import com.estrelsteel.engine2.image.Image;
import com.estrelsteel.engine2.shape.rectangle.Rectangle;
import com.estrelsteel.ld36.station.Station;

public class Mail extends Actor {
	
	private MailType type;
	private long stime;
	private Station start;
	private long travelTime;
	private boolean moved;
	
	public Mail(Rectangle loc, MailType type) {
		super("MAIL", loc);
		this.type = type;
		getAnimations().add(new Animation("MAIL_BASE", 0));
		getAnimations().get(0).setMaxWaitTime(120);
		for(Image img : type.getMail().getFrames()) {
			getAnimations().get(0).getFrames().add(img);
		}
	}
	
	public MailType getType() {
		return type;
	}
	
	public long getStartTime() {
		return stime;
	}
	
	public Station getStart() {
		return start;
	}
	
	public long getTravelTime() {
		return travelTime;
	}
	
	public boolean hasMoved() {
		return moved;
	}
	
	public void setType(MailType type) {
		this.type = type;
	}
	
	public void setStartTime(long stime) {
		this.stime = stime;
	}
	
	public void setStart(Station start) {
		this.start = start;
	}
	
	public void setTravelTime(long travelTime) {
		this.travelTime = travelTime;
	}
	
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
