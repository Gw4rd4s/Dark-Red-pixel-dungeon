package com.watabou.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.watabou.utils.GameMath;
public class GameMathTest {

    @Test
    public void gatetest() {
        assertEquals(3.5, GameMath.gate(2,3.5f,4), 0.01);
    }
}