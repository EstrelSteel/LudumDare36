package com.estrelsteel.ld36.mail;

import com.estrelsteel.engine2.file.GameFile;
import com.estrelsteel.engine2.file.Saveable;

public class PendingMail implements Saveable {
	private int spawn;
	
	public PendingMail(int spawn) {
		this.spawn = spawn;
	}
	
	public int getSpawn() {
		return spawn;
	}
	
	public void setSpawn(int spawn) {
		this.spawn = spawn;
	}

	@Override
	public String getIdentifier() {
		return "Mal";
	}

	@Override
	public PendingMail load(GameFile file, int line) {
		String[] args = file.getLines().get(line).split(" ");
		if(args[0].trim().equalsIgnoreCase(getIdentifier())) {
			return new PendingMail(Integer.parseInt(args[1].trim()));
		}
		return null;
	}

	@Override
	public GameFile save(GameFile file) {
		return file;
	}
}
