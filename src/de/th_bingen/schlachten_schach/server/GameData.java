/*
SchlachtenSchachServer
Copyright (C) 2017 Patrick Reths

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.th_bingen.schlachten_schach.server;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameData {
	private final String name;
	private ClientData playerA, playerB;
	
	public GameData(String name) {
		this(name, null, null);
	}
	
	public GameData(String name, ClientData playerA, ClientData playerB) {
		this.name = name;
		this.playerA = playerA; // white
		this.playerB = playerB; // black
	}
	
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public ClientData getPlayerA() {
		return playerA;
	}

	public void setPlayerA(ClientData playerA) {
		this.playerA = playerA;
	}

	public ClientData getPlayerB() {
		return playerB;
	}

	public void setPlayerB(ClientData playerB) {
		this.playerB = playerB;
	}
	
	public Socket getSocketPlayerA() {
		if (playerA == null) {
			return null;
		}
		return playerA.getSocket();
	}
	
	public Socket getSocketPlayerB() {
		if (playerB == null) {
			return null;
		}
		return playerB.getSocket();
	}
	
	public ObjectOutputStream getObjOutPlayerA() {
		if (playerA == null) {
			return null;
		}
		return playerA.getObjOut();
	}
	
	public ObjectOutputStream getObjOutPlayerB() {
		if (playerB == null) {
			return null;
		}
		return playerB.getObjOut();
	}
}
