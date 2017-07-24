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

public class PieceMove implements Serializable{
	private static final long serialVersionUID = 175786811929639425L;
	public int oldX, oldY, newX, newY;
	
	public PieceMove(int oldX, int oldY, int newX, int newY) {
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}
	
	public String toString() {
		return "("+ oldX +"|"+ oldY +") -> ("+ newX +"|"+ newY +")";
	}
}
