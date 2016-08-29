package com.estrelsteel.ld36.station;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.estrelsteel.engine2.image.Renderable;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;
import com.estrelsteel.engine2.shape.rectangle.Rectangle;
import com.estrelsteel.engine2.world.FrozenWorld;
import com.estrelsteel.ld36.mail.Mail;
import com.estrelsteel.ld36.mail.MailType;
import com.estrelsteel.ld36.mail.PendingMail;
import com.estrelsteel.ld36.tube.PendingTube;
import com.estrelsteel.ld36.tube.Tube;

public class Level implements Renderable {
	
	private String name;
	private ArrayList<Station> stations;
	private int active;
	private ArrayList<Tube> tubes;
	private ArrayList<PendingMail> pMail;
	private ArrayList<PendingTube> pTube;
	
	public Level(String name) {
		this.name = name;
		this.stations = new ArrayList<Station>();
		this.active = 1;
		this.tubes = new ArrayList<Tube>();
		this.pMail = new ArrayList<PendingMail>();
		this.pTube = new ArrayList<PendingTube>();
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Station> getStations() {
		return stations;
	}
	
	public int getActiveStation() {
		return active;
	}
	
	public ArrayList<Tube> getTubes() {
		return tubes;
	}
	
	public ArrayList<PendingMail> getPendingMail() {
		return pMail;
	}
	
	public ArrayList<PendingTube> getPendingTube() {
		return pTube;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStations(ArrayList<Station> stations) {
		this.stations = stations;
	}
	
	public void setActiveStation(int active) {
		this.active = active;
	}
	
	public void setTubes(ArrayList<Tube> tubes) {
		this.tubes = tubes;
	}
	
	public void setPendingMail(ArrayList<PendingMail> pMail) {
		this.pMail = pMail;
	}
	
	public void setPendingTube(ArrayList<PendingTube> pTube) {
		this.pTube = pTube;
	}
	
	private Tube checkRoute(ArrayList<Station> start, MailType target, Tube tube, int path) {
		for(int j = 0; j < start.size(); j++) {
			if(!tube.getStations()[path].equals(start.get(j))) {
				if(tube.getStations()[path].getType() == target) {
					return tube;
				}
				else {
					start.add(tube.getStations()[path]);
					if(traverseTubes2(start, target) != null) {
						return tube;
					}
				}
			}
		}
		return null;
	}
	
	private Tube traverseTubes2(ArrayList<Station> start, MailType target) {
		Tube tube = null;
		for(int i = 0 ;i < tubes.size(); i++) {
			tube = tubes.get(i);
			if(tube.getCapacity() > tube.getMail().size()) {
				if(tube.getStations()[0].equals(start.get(start.size() - 1)) && !start.contains(tube.getStations()[1])) {
					tube = checkRoute(start, target, tube, 1);
					if(tube != null) {
						return tube;
					}
				}
				else if(tube.getStations()[1].equals(start.get(start.size() - 1)) && !start.contains(tube.getStations()[0])) {
					tube = checkRoute(start, target, tube, 0);
					if(tube != null) {
						return tube;
					}
				}
			}
			
		}
		return null;
	}
	
	public int calcTotalTubes() {
		int tot = 0;
		for(int i = 0; i < tubes.size(); i++) {
			tot = tot + tubes.get(i).getCapacity();
		}
		return tot;
	}
	
	public int checkAvaliableMail() {
		int tot = 0;
		for(int i = 0; i < tubes.size(); i++) {
			tot = tot + tubes.get(i).getMail().size();
		}
		for(int i = 0; i < stations.size(); i++) {
			tot = tot + stations.get(i).getMail().size();
		}
		tot = tot + pMail.size();
		return tot;
	}
	
	public boolean checkForExpiredMail() {
		for(int i = 0; i < tubes.size(); i++) {
			for(int j = 0; j < tubes.get(i).getMail().size(); j++) {
				if(System.currentTimeMillis() - tubes.get(i).getMail().get(j).getStartTime() >= 22000) {
					return true;
				}
			}
		}
		for(int i = 0; i < stations.size(); i++) {
			for(int j = 0; j < stations.get(i).getMail().size(); j++) {
				if(System.currentTimeMillis() - stations.get(i).getMail().get(j).getStartTime() >= 22000) {
					return true;
				}
			}
		}
		return false;
	}
	
	private Tube findDirectRoute(Station start, MailType target) {
		Tube tube = null;
		for(int i = 0 ;i < tubes.size(); i++) {
			tube = tubes.get(i);
			if(tube.getCapacity() > tube.getMail().size()) {
				if(tube.getStations()[0].equals(start)) {
					if(tube.getStations()[1].getType() == target) {
						return tube;
					}
				}
				else if(tube.getStations()[1].equals(start)) {
					if(tube.getStations()[0].getType() == target) {
						return tube;
					}
				}
			}
			
		}
		ArrayList<Station> s1 = new ArrayList<Station>();
		s1.add(start);
		return traverseTubes2(s1, target);
	}

	public Tube findTubeWithDestination(Station start, MailType target) {
		return findDirectRoute(start, target);
	}
	
	public void spawnMail(Station s) {
		int rType = (int) (Math.random() * MailType.values().length - 1);
		if(s == null) {
			int rStation = (int) (Math.random() * (active + 1));
			s = stations.get(rStation);
		}
		
		
		if(MailType.values()[rType + 1] != s.getType()) {
			Mail mail = new Mail(QuickRectangle.location(s.getLocation().getX() + s.getLocation().getWidth() + 5,
					s.getLocation().getY() + (20 * s.getMail().size()), 16, 16), MailType.values()[rType + 1]);
			mail.setStart(s);
			mail.setStartTime(System.currentTimeMillis());
			s.getMail().add(mail);
		}
		else {
			spawnMail(s);
		}
	}
	
	@Override
	public Rectangle getLocation() {
		return QuickRectangle.location(0, 0, 0, 0);
	}

	@Override
	public Graphics2D render(Graphics2D ctx, FrozenWorld world) {
		for(int i = 0; i < tubes.size(); i++) {
			ctx = tubes.get(i).render(ctx, world);
		}
		for(int i = 0; i <= active && i < stations.size(); i++) {
			ctx = stations.get(i).render(ctx, world);
			for(int j = 0; j < stations.get(i).getMail().size(); j++) {
				ctx = stations.get(i).getMail().get(j).render(ctx, world);
			}
		}
		
		return ctx;
	}

	@Override
	public Graphics2D simpleRender(Graphics2D ctx, FrozenWorld world) {
		for(int i = 0; i < tubes.size(); i++) {
			ctx = tubes.get(i).simpleRender(ctx, world);
		}
		for(int i = 0; i <= active && i < stations.size(); i++) {
			ctx = stations.get(i).simpleRender(ctx, world);
			for(int j = 0; j < stations.get(i).getMail().size(); j++) {
				ctx = stations.get(i).getMail().get(j).simpleRender(ctx, world);
			}
		}
		return ctx;
	}

	@Override
	public boolean isSortable() {
		return false;
	}

	@Override
	public void setLocation(Rectangle loc) {}

	@Override
	public void setSortable(boolean sortable) {}
}
