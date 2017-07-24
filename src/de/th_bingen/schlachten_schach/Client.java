/*
SchlachtenSchach
Copyright (C) 2017 Patrick Reths

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.th_bingen.schlachten_schach;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sun.media.sound.InvalidDataException;

import de.th_bingen.schlachten_schach.online.*;

public class Client {
	public static void main(String[] args) {
		Socket socket = null;
		
		if (args.length != 1) {
			System.out.println("no player name");
			return;
		}
		
		try {
			socket = new Socket("localhost", 3210);
			socket.setKeepAlive(true);
			ObjectOutputStream oo = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
			
			PieceColor playerColor;
			Object obj;
			
			oo.writeObject(new ConnectionHeader("game1", args[0]));
			
			obj = read(oi);
			if (obj instanceof PieceColor) {
				playerColor = (PieceColor) obj;
				System.out.println(playerColor);
			} else {
				throw new InvalidDataException();
			}
			
			
			if (playerColor == PieceColor.White) { // white
				obj = read(oi);
				if (obj instanceof String) {
					String message = (String) obj;
					System.out.println(message);
				} else {
					throw new InvalidDataException();
				}
				
				oo.writeObject(new PieceMove(0, 1, 0, 2));
				
				obj = read(oi);
				if (obj instanceof PieceMove) {
					PieceMove move = (PieceMove) obj;
					System.out.println(move);
				} else {
					throw new InvalidDataException();
				}
				
			} else { // black				
				obj = read(oi);
				if (obj instanceof String) {
					String message = (String) obj;
					System.out.println(message);
				} else {
					throw new InvalidDataException();
				}
				
				obj = read(oi);
				if (obj instanceof PieceMove) {
					PieceMove move = (PieceMove) obj;
					System.out.println(move);
				} else {
					throw new InvalidDataException();
				}
				
				oo.writeObject(new PieceMove(0, 6, 0, 5));
			}
			
			

//			String s = br.readLine();
//			while (s != null) {
//				System.out.println(s);
//				s = br.readLine();
//			}
			
		} catch (UnknownHostException e) {
			System.out.println("Unbekannter Host");
			e.printStackTrace();
		} catch (InvalidDataException e) {
			System.out.println("Ungültige Daten empfangen");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Verbindungsfehler");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Klasse nicht gefunden");
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
					System.out.println("Verbindung getrennt");
				} catch (IOException e) {
					System.out.println("Socket kann nicht geschlossen werden");
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Object read(ObjectInputStream oi) throws ClassNotFoundException, IOException {
		Object obj = oi.readObject();
		while (obj instanceof PingData) {
			obj = oi.readObject();
		}
		return obj;
	}
}
