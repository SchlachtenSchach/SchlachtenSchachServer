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

package de.th_bingen.schlachten_schach.online;

import java.io.Serializable;

public class ConnectionHeader implements Serializable{
	private static final long serialVersionUID = 3768978765997206204L;
	public String gameName;
	public String playerName;	
	
	public ConnectionHeader(String gameName, String playerName) {
		this.gameName = gameName;
		this.playerName = playerName;
	}
	
	public String toString() {
		return "gameName: "+gameName+", playerName: "+playerName;
	}
}
