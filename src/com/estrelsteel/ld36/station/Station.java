package com.estrelsteel.ld36.station;

import java.util.ArrayList;

import com.estrelsteel.engine2.actor.Actor;
import com.estrelsteel.engine2.file.GameFile;
import com.estrelsteel.engine2.file.Saveable;
import com.estrelsteel.engine2.image.Animation;
import com.estrelsteel.engine2.image.ConfinedImage;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;
import com.estrelsteel.engine2.shape.rectangle.Rectangle;
import com.estrelsteel.ld36.mail.Mail;
import com.estrelsteel.ld36.mail.MailType;

public class Station extends Actor implements Saveable {
	
	private MailType type;
	private ArrayList<Mail> mail;
	private int reqMail;
	
	public Station(MailType type, Rectangle loc) {
		super("STATION", loc);
		this.type = type;
		getAnimations().add(new Animation("STATION_BASE", 0));
		getAnimations().get(0).getFrames().add(type.getStationImage());
		getAnimations().add(new Animation("STATION_SELECT", 1));
		ConfinedImage img = new ConfinedImage(type.getStationImage().getSRC(), type.getStationImage().getLocation());
		img.setLocation(QuickRectangle.location(img.getLocation().getX(), 2 * 16, img.getLocation().getWidth(), img.getLocation().getHeight()));
		getAnimations().get(1).getFrames().add(img);
		
		getAnimations().add(new Animation("STATION_DELETE", 2));
		img = new ConfinedImage(type.getStationImage().getSRC(), type.getStationImage().getLocation());
		img.setLocation(QuickRectangle.location(img.getLocation().getX(), 4 * 16, img.getLocation().getWidth(), img.getLocation().getHeight()));
		getAnimations().get(2).getFrames().add(img);
		setRunningAnimationNumber(0);
		
		mail = new ArrayList<Mail>();
		reqMail = 0;
	}
	
	public MailType getType() {
		return type;
	}
	
	public ArrayList<Mail> getMail() {
		return mail;
	}
	
	public int getRequiredMail() {
		return reqMail;
	}
	
	public boolean equals(Object other) {
		if(other instanceof Station) {
			if(getLocation().equals(((Station) other).getLocation()) 
					&& getType() == ((Station) other).getType()) {
				return true;
			}
			return false;
		}
		return super.equals(other);
	}
	
	
	public void setType(MailType type) {
		this.type = type;
	}
	
	public void setMail(ArrayList<Mail> mail) {
		this.mail = mail;
	}

	public void setRequiredMail(int reqMail) {
		this.reqMail = reqMail;
	}
	
	@Override
	public String getIdentifier() {
		return "PSt";
	}

	@Override
	public Station load(GameFile file, int line) {
		String[] args = file.getLines().get(line).split(" ");
		Station station = null;
		if(args[0].trim().equalsIgnoreCase(getIdentifier())) {
			station = new Station(MailType.values()[Integer.parseInt(args[1].trim())],
					QuickRectangle.location(Double.parseDouble(args[3].trim()), Double.parseDouble(args[4].trim()), 
							Double.parseDouble(args[5].trim()), Double.parseDouble(args[6].trim())));
			station.setRequiredMail(Integer.parseInt(args[2].trim()));
		}
		return station;
	}

	@Override
	public GameFile save(GameFile file) {
		file.getLines().add(getIdentifier() + " " + type.ordinal() + " " + reqMail + " "+ getLocation().getX() + " " + getLocation().getY()
				+ " " + getLocation().getWidth() + " " + getLocation().getHeight());
		return file;
	}
}
