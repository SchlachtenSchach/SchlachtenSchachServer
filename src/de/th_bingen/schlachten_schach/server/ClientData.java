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

import de.th_bingen.schlachten_schach.PieceColor;

public class ClientData {
	private String name;
	private PieceColor color;
	private Socket socket;
	private ObjectOutputStream objOut;
	
	public ClientData(String name, PieceColor color, Socket socket, ObjectOutputStream objOut) {
		this.name = name;
		this.color = color;
		this.socket = socket;
		this.objOut = objOut;
	}

	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PieceColor getColor() {
		return color;
	}

	public void setColor(PieceColor color) {
		this.color = color;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectOutputStream getObjOut() {
		return objOut;
	}

	public void setObjOut(ObjectOutputStream objOut) {
		this.objOut = objOut;
	}
}
