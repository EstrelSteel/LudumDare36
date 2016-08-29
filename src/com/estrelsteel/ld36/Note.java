package com.estrelsteel.ld36;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.estrelsteel.engine2.Engine2;
import com.estrelsteel.engine2.actor.Actor;
import com.estrelsteel.engine2.actor.Text;
import com.estrelsteel.engine2.file.GameFile;
import com.estrelsteel.engine2.file.Saveable;
import com.estrelsteel.engine2.image.Animation;
import com.estrelsteel.engine2.image.ConfinedImage;
import com.estrelsteel.engine2.shape.rectangle.QuickRectangle;
import com.estrelsteel.engine2.shape.rectangle.Rectangle;
import com.estrelsteel.engine2.world.FrozenWorld;

public class Note extends Actor implements Saveable {

	private ArrayList<Text> text;
	private boolean visible;
	
	public Note(Rectangle loc) {
		super("NOTE", loc);
		this.text = new ArrayList<Text>();
		this.visible =  false;
		
		getAnimations().add(new Animation("NOTE_BASE", 0));
		getAnimations().get(0).getFrames().add(new ConfinedImage(Engine2.devPath + "/res/img/texture.png", QuickRectangle.location(0 * 16, 2 * 16, 32, 32)));
	}
	
	public ArrayList<Text> getText() {
		return text;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setText(ArrayList<Text> text) {
		this.text = text;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public String getIdentifier() {
		return "Not";
	}
	
	@Override
	public Note load(GameFile file, int line) {
		String[] args = file.getLines().get(line).split(" ");;
		Note note = null;
		Text text = new Text("", QuickRectangle.location(0, 0, 0, 0));
		if(args[0].trim().equalsIgnoreCase(getIdentifier())) {
			note = new Note(QuickRectangle.location(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
			note.setName(args[1].trim());
			for(line++; line < file.getLines().size(); line++) {
				note.getText().add(text.load(file, line));
			}
		}
		return note;
	}

	@Override
	public GameFile save(GameFile file) {
		return file;
	}
	
	@Override
	public Graphics2D render(Graphics2D ctx, FrozenWorld world) {
		if(visible) {
			super.render(ctx, world);
			for(int i = 0; i < text.size(); i++) {
				text.get(i).render(ctx, world);
			}
		}
		return ctx;
	}

	@Override
	public Graphics2D simpleRender(Graphics2D ctx, FrozenWorld world) {
		if(visible) {
			super.simpleRender(ctx, world);
			for(int i = 0; i < text.size(); i++) {
				text.get(i).simpleRender(ctx, world);
			}
		}
		return ctx;
	}

}
