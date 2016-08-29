package com.estrelsteel.ld36.mail;

import com.estrelsteel.engine2.Engine2;
import com.estrelsteel.engine2.image.Animation;
import com.estrelsteel.engine2.image.ConfinedImage;
import com.estrelsteel.engine2.image.Image;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;

public enum MailType {
	UNKNOWN(new ConfinedImage("/res/img/texture.png", QuickRectangle.location(0 * 16, 0 * 16, 16, 16)), 
			0, 0),
	GOVERNMENT(new ConfinedImage("/res/img/texture.png", QuickRectangle.location(2 * 16, 0 * 16, 32, 32)),
			8, 0), 
	PERSONAL(new ConfinedImage("/res/img/texture.png", QuickRectangle.location(4 * 16, 0 * 16, 32, 32)),
			9, 0), 
	MAILROOM(new ConfinedImage("/res/img/texture.png", QuickRectangle.location(6 * 16, 0 * 16, 32, 32)),
			10, 0);
	
	private ConfinedImage stationImg;
	private Animation mail;
	
	MailType(ConfinedImage stationImg, int x, int y) {
		this.stationImg = stationImg;
		this.mail = new Animation("MAIL", 0);
		this.mail.setMaxWaitTime(120);
		for(int i = y; i < 13; i++) {
			this.mail.getFrames().add(new ConfinedImage("/res/img/texture.png", QuickRectangle.location(x * 16, i * 16, 16, 16)));
		}
	}
	
	public ConfinedImage getStationImage() {
		return stationImg;
	}
	
	public Animation getMail() {
		return mail;
	}
	
	public static void updateSRC() {
		for(MailType type : MailType.values()) {
			type.getStationImage().setSRC(Engine2.devPath + type.getStationImage().getSRC());
			for(Image img : type.getMail().getFrames()) {
				img.setSRC(Engine2.devPath + img.getSRC());
			}
		}
	}
}
