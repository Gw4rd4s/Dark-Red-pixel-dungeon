/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.utils;

import com.watabou.noosa.Game;

public class GameMath {
	
	public static float speed( float speed, float acc ) {
		
		if (acc != 0) {
			speed += acc * Game.elapsed;
		}
		
		return speed;
	}

	/**
	 *returns number between <b>min</b> and <b>max</b> limits
	 * returns <b>FLOAT</b>
	 * @param value <b>FLOAT</b> input to be limited
	 * @param min <b>FLOAT</b> lower bound
	 * @param max <b>FLOAT</b> upper bound
	 */
	public static float gate( float min, float value, float max ) {
		value = Math.max(value, min);
		value = Math.min(value, max);
		return value;
	}
	/**
	 *returns number between <b>min</b> and <b>max</b> limits
	 * returns <b>INT</b>
	 * @param value <b>INT</b> input to be limited
	 * @param min <b>INT</b> lower bound
	 * @param max <b>INT</b> upper bound
	 */
	public static int gate( int min, int value, int max ) {
		value = Math.max(value, min);
		value = Math.min(value, max);
		return value;
	}

	/**
	 * Calculates normally distributed damage
	 * @param avg average damage dealt. Less than 2^9 -1 or 511!
	 * @param range range of damage from lower to upper bound
	 * @param critBonus critical bonus to damage
	 * @return INT normally distributed
	 */
	public static int damageRoll(int avg, int range, float critBonus){
		float min = avg + range * (2 * critBonus - 0.5f);
		float max = avg + range * 0.5f;
		return Random.NormalIntRange( (int) min,(int) max);
	}
}
