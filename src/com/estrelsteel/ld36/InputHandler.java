package com.estrelsteel.ld36;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.estrelsteel.engine2.point.PointMaths;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;
import com.estrelsteel.engine2.sound.SFX;
import com.estrelsteel.ld36.station.Level;
import com.estrelsteel.ld36.station.Station;
import com.estrelsteel.ld36.tube.Tube;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

	private LD36 ld;
	public boolean drawingTube;
	public boolean drawingDel;
	public int mouseX;
	public int mouseY;
	public Station start;
	public int tempStart;
	private long startPause;
	
	public InputHandler(LD36 ld) {
		this.ld = ld;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
//		case 116:
//			ld.start();
//			break;
		case 32:
			ld.start = ld.getLauncher().getEngine().getStats().getTicks();
			for(int i = 0; i < ld.getWorld().getObjects().size(); i++) {
				if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_WELCOME")) {
					((Note) ld.getWorld().getObjects().get(i)).setVisible(false);
					break;
				}
			}
			break;
		case 49:
			for(int i = 0; i < ld.getWorld().getObjects().size(); i++) {
				if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_GAMEOVER")) {
					if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
						ld.start();
						break;
					}
				}
				else if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_PAUSE")) {
					if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
						pause();
						break;
					}
				}
			}
			break;
		case 50:
			for(int i = 0; i < ld.getWorld().getObjects().size(); i++) {
				if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_GAMEOVER")) {
					if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
						ld.getLauncher().getEngine().stop();
						break;
					}
				}
				else if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_PAUSE")) {
					if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
						LD36.music = !LD36.music;
						((Note) ld.getWorld().getObjects().get(i)).getText().get(6).setText("[2] Music: " + LD36.music);
						break;
					}
				}
			}
			break;
		case 51:
			for(int i = 0; i < ld.getWorld().getObjects().size(); i++) {
				if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_PAUSE")) {
					if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
						LD36.sfx = !LD36.sfx;
						((Note) ld.getWorld().getObjects().get(i)).getText().get(7).setText("[3] SFX: " + LD36.sfx);
						break;
					}
				}
			}
			break;
		case 57:
			for(int i = 0; i < ld.getWorld().getObjects().size(); i++) {
				if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_PAUSE")) {
					if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
						ld.start();
						break;
					}
				}
			}
			break;
		case 48:
			for(int i = 0; i < ld.getWorld().getObjects().size(); i++) {
				if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_PAUSE")) {
					if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
						ld.getLauncher().getEngine().stop();
						break;
					}
				}
			}
			break;
		case 27:
			pause();
			break;
		case 80:
			pause();
			break;
		}
	}
	
	public void pause() {
		for(int i = 0; i < ld.getWorld().getObjects().size(); i++) {
			if(ld.getWorld().getObjects().get(i) instanceof Note && ((Note) ld.getWorld().getObjects().get(i)).getName().equalsIgnoreCase("NOTE_PAUSE")) {
				((Note) ld.getWorld().getObjects().get(i)).setVisible(!((Note) ld.getWorld().getObjects().get(i)).isVisible());
				((Note) ld.getWorld().getObjects().get(i)).getText().get(1).setText("Mail Delivered: " + (int) LD36.score);
				((Note) ld.getWorld().getObjects().get(i)).getText().get(6).setText("[2] Music: " + LD36.music);
				((Note) ld.getWorld().getObjects().get(i)).getText().get(7).setText("[3] SFX: " + LD36.sfx);
				if(((Note) ld.getWorld().getObjects().get(i)).isVisible()) {
					ld.paused = true;
					tempStart = ld.getLauncher().getEngine().getStats().getTicks() - ld.start;
					startPause = System.currentTimeMillis();
				}
				else {
					ld.paused = false;
					ld.start = ld.getLauncher().getEngine().getStats().getTicks() - tempStart;
					updateMailTimes();
				}
				break;
			}
		}
	}
	
	public void updateMailTimes() {
		long change = System.currentTimeMillis() - startPause;
		for(int i = 0; i < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().size(); i++) {
			for(int j = 0; j < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i).getMail().size(); j++) {
				((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i).getMail().get(j).setStartTime(
						((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i).getMail().get(j).getStartTime() + change);
			}
		}
		for(int i = 0; i < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getStations().size(); i++) {
			for(int j = 0; j < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getStations().get(i).getMail().size(); j++) {
				((Level) ld.getWorld().getObjects().get(ld.lvl)).getStations().get(i).getMail().get(j).setStartTime(
						((Level) ld.getWorld().getObjects().get(ld.lvl)).getStations().get(i).getMail().get(j).getStartTime() + change);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(ld.start < 0) {
			return;
		}
		ArrayList<Station> stations = ((Level) ld.getWorld().getObjects().get(0)).getStations();
		Station station;
		for(int s = 0; s < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getActiveStation() + 1 && s < stations.size(); s++) {
			station = ((Level) ld.getWorld().getObjects().get(ld.lvl)).getStations().get(s);
			if(station.getLocation().getX() <= e.getX() && station.getLocation().getX() + station.getLocation().getWidth() >= e.getX()) {
				if(station.getLocation().getY() <= e.getY() && station.getLocation().getY() + station.getLocation().getHeight() >= e.getY()) {
					if(e.getButton() == MouseEvent.BUTTON1) {
						if(ld.tubes - ((Level) ld.getWorld().getObjects().get(ld.lvl)).calcTotalTubes() > 0) {
							station.setRunningAnimationNumber(1);
							start = station;
							drawingTube = true;
						}
					}
					else if(e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
						station.setRunningAnimationNumber(2);
						start = station;
						drawingDel = true;
					}
				}
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		ArrayList<Station> stations = ((Level) ld.getWorld().getObjects().get(0)).getStations();
		
		if(drawingTube) {
			Station station;
			for(int s = 0; s < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getActiveStation() + 1 && s < stations.size(); s++) {
				station = ((Level) ld.getWorld().getObjects().get(ld.lvl)).getStations().get(s);
				if(station.getLocation().getX() <= e.getX() && station.getLocation().getX() + station.getLocation().getWidth() >= e.getX()) {
					if(station.getLocation().getY() <= e.getY() && station.getLocation().getY() + station.getLocation().getHeight() >= e.getY()) {
						if(station != start) {
							station.setRunningAnimationNumber(1);
							
							Tube tube2 = null;
							for(int i = 0; i < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().size(); i++) {
								tube2 = ((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i);
								if((tube2.getStations()[0].equals(start) || tube2.getStations()[1].equals(start)) 
										&& (tube2.getStations()[0].equals(station) || tube2.getStations()[1].equals(station))) {
									((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i).setCapacity(tube2.getCapacity() + 1);
									break;
								}
								else {
									tube2 = null;
								}
							}
							if(tube2 == null) {
								Tube tube =  new Tube("TUBE", QuickRectangle.location(start.getLocation().getX(),
										start.getLocation().getY(), station.getLocation().getX() - start.getLocation().getX(), 
										station.getLocation().getY() - start.getLocation().getY()));
								tube.getStations()[0] = start;
								tube.getStations()[1] = station;
								((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().add(0, tube);
								
								((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(0).setTravelTime(
										(long) (PointMaths.getDistanceTo(start.getLocation().getTop(), station.getLocation().getTop()) * 5));
							}
							drawingTube = false;
							if(LD36.sfx) {
								SFX.getSounds().get(2).play();
							}
							break;
						}
						
					}
				}
			}
		}
		else if(drawingDel) {
			for(Station station : stations) {
				if(station.getLocation().getX() <= e.getX() && station.getLocation().getX() + station.getLocation().getWidth() >= e.getX()) {
					if(station.getLocation().getY() <= e.getY() && station.getLocation().getY() + station.getLocation().getHeight() >= e.getY()) {
						if(station != start) {
							station.setRunningAnimationNumber(2);
							
							Tube tube2 = null;
							for(int i = 0; i < ((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().size(); i++) {
								tube2 = ((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i);
								if((tube2.getStations()[0].equals(start) || tube2.getStations()[1].equals(start)) 
										&& (tube2.getStations()[0].equals(station) || tube2.getStations()[1].equals(station))) {
									((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i).setCapacity(tube2.getCapacity() - 1);
									if(tube2.getMail().size() > 0) {
										for(int j = 0; j < tube2.getMail().size(); j++) {
											((Level) ld.getWorld().getObjects().get(ld.lvl)).spawnMail(null);
										}
									}
									if(((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().get(i).getCapacity() == 0) {
										((Level) ld.getWorld().getObjects().get(ld.lvl)).getTubes().remove(i);
									}
									break;
								}
							}
							drawingDel = false;
							if(LD36.sfx) {
								SFX.getSounds().get(3).play();
							}
							break;
						}
						
					}
				}
			}
		}
		for(Station station : stations) {
			if(station.getRunningAnimationNumber() == 1 || station.getRunningAnimationNumber() == 2) {
				station.setRunningAnimationNumber(0);
			}
		}
		drawingDel = false;
		drawingTube = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
	}

}
