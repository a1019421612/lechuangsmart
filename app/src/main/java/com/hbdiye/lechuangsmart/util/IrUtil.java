package com.hbdiye.lechuangsmart.util;


public class IrUtil {

	public static short[] getPulseArray(int frequency, int[] pattern) {
		short[] ret = new short[pattern.length];
		int pulseLen = 1000000 / frequency;
		for (int i = 0; i < pattern.length; i++) {
			ret[i] = (short) (pattern[i] / pulseLen);
		}

		return ret;
	}

}
