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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.TreeMap;

import com.sun.media.sound.InvalidDataException;

import de.th_bingen.schlachten_schach.PieceColor;
import de.th_bingen.schlachten_schach.online.*;

public class Server {
	private final ServerSocket server;
	private TreeMap<String, GameData> games;

	public Server(int port) throws IOException {
		server = new ServerSocket(port);
		games = new TreeMap<String, GameData>();
	}

	public void start() {
		while (true) {
			Socket client = null;

			try {
				client = server.accept();
				new Thread(new ConnectionHandler(client)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private class ConnectionHandler implements Runnable {
		private Socket client;
		private GameData game;
		private ClientData clientData;
		
		public ConnectionHandler(Socket client) {
			this.client = client;
		}
		
		public void run() {
			print("start");
			try {
				handleConnection();
			} catch (EOFException | SocketException e) {
				print("client disconnected");
				
				try {
					// notify other player
					sendToOtherPlayer("otherPlayerDisconnected");
				} catch (IOException e1) {
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (client != null) {
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			// remove current player from game
			if (client == game.getSocketPlayerA()) {
				game.setPlayerA(null);
			}
			if (client == game.getSocketPlayerB()) {
				game.setPlayerB(null);
			}
			
			if ((game.getSocketPlayerA() == null) && (game.getSocketPlayerB() == null)) {
				games.remove(game.getName());
				print("remove game with name '"+game.getName()+"'");
			}
			
			print("end");
		}
		
		private void print(String text) {
			System.out.println("ConnectionHandler("+client.getRemoteSocketAddress()+"): "+text);
		}
		
		private ClientData getOtherPlayer() {
			if (client == game.getSocketPlayerA()) {
				return game.getPlayerB();
			}
			if (client == game.getSocketPlayerB()) {
				return game.getPlayerA();
			}
			
			if ((game.getSocketPlayerA() == null) && (game.getSocketPlayerB() != null)) {
				return game.getPlayerB();
			}
			if ((game.getSocketPlayerA() != null) && (game.getSocketPlayerB() == null)) {
				return game.getPlayerA();
			}
			
			return null;
		}
		
		private ObjectOutputStream getOtherPlayerObjOut() {
			if (client == game.getSocketPlayerA()) {
				return game.getObjOutPlayerB();
			} else {
				return game.getObjOutPlayerA();
			}
		}
		
		private void checkConnection() throws IOException {
			ObjectOutputStream oo = clientData.getObjOut();
			oo.writeObject(new PingData());
		}
		
		private void waitForOtherPlayer() throws IOException {
			ClientData otherPlayer = getOtherPlayer();
			int c = 0;
			while (otherPlayer == null) {
				c = (c+1) % 100;
				if (c == 0) {
					checkConnection();
				}
				try {
					Thread.sleep(1); // sleep 1 ms to reduce cpu load
				} catch (InterruptedException e) {
				}
				otherPlayer = getOtherPlayer();
			}
		}
		
		private void sendToOtherPlayer(Object data) throws IOException {
			ObjectOutputStream otherPlayerObjOut = getOtherPlayerObjOut();
			if (otherPlayerObjOut == null) {
				throw new IOException();
			}
			otherPlayerObjOut.writeObject(data);
		}
		
		private void handleConnection() throws IOException, ClassNotFoundException {
			ObjectInputStream oi = new ObjectInputStream(client.getInputStream());
			ObjectOutputStream oo = new ObjectOutputStream(client.getOutputStream());
			Object obj;

			obj = oi.readObject();
			if (obj instanceof ConnectionHeader) {
				ConnectionHeader clientInfo = (ConnectionHeader) obj;
				System.out.println(clientInfo);

				clientData = new ClientData(clientInfo.playerName, null, client, oo);
				
				if (games.containsKey(clientInfo.gameName)) { // existing game
					game = games.get(clientInfo.gameName);
					
					if (getOtherPlayer() == null) {
						print("game '"+game.getName()+"' exists already");
						oo.writeObject("game '"+game.getName()+"' exists already");
						return;
					}
					
					print("enter to existing game with name '"+game.getName()+"'");
					
					if (getOtherPlayer().getColor() == PieceColor.Black) { // white
						clientData.setColor(PieceColor.White);
					} else { // black
						clientData.setColor(PieceColor.Black);
					}
				} else {
					game = new GameData(clientInfo.gameName); // new game
					games.put(clientInfo.gameName, game);
					print("create new game with name '"+game.getName()+"'");
					
					if (Math.random() > 0.5) { // white
						clientData.setColor(PieceColor.White);
					} else { // black
						clientData.setColor(PieceColor.Black);
					}
				}
				
				if (clientData.getColor() == PieceColor.White) { // white
					game.setPlayerA(clientData);
					oo.writeObject(PieceColor.White);
				} else { // black
					game.setPlayerB(clientData);
					oo.writeObject(PieceColor.Black);
				}
			} else {
				throw new InvalidDataException();
			}
			
			waitForOtherPlayer();
			oo.writeObject("ready");
			
			obj = oi.readObject();
			while (obj != null) {
				if (obj instanceof PieceMove) {
					PieceMove move = (PieceMove) obj;
					print("move: "+move);
					try {
						sendToOtherPlayer(move);
					} catch (IOException e) { // other player disconnected
						oo.writeObject("otherPlayerDisconnected");
					}
				} else {
					throw new InvalidDataException();
				}
				obj = oi.readObject();
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		Server server = new Server(3210);
		server.start();
	}
}
