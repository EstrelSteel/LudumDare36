package com.estrelsteel.ld36.tube;

import com.estrelsteel.engine2.file.GameFile;
import com.estrelsteel.engine2.file.Saveable;

public class PendingTube implements Saveable {
	private int spawn;
	
	public PendingTube(int spawn) {
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
		return "Tub";
	}

	@Override
	public PendingTube load(GameFile file, int line) {
		String[] args = file.getLines().get(line).split(" ");
		if(args[0].trim().equalsIgnoreCase(getIdentifier())) {
			return new PendingTube(Integer.parseInt(args[1].trim()));
		}
		return null;
	}

	@Override
	public GameFile save(GameFile file) {
		return file;
	}
}
