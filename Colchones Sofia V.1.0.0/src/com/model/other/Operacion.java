package com.model.other;

import java.math.BigInteger;

public class Operacion {

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigInteger resta (BigInteger a, BigInteger b) {
		if(b == null) {
			b= BigInteger.ZERO;
		}
		if(a != null) {
			return a.subtract(b);
		}
		return null;
	}
	
	public static boolean mayorZero(BigInteger a) {
		if(a != null && a.compareTo(BigInteger.ZERO) == 1 ){
			return true;
		}
		return false;
	}
	
	public static boolean menorZero(BigInteger a) {
		if(a != null && a.compareTo(BigInteger.ZERO) == -1 ){
			return true;
		}
		return false;
	}
	
	public static boolean mayorIgualZero(BigInteger a) {
		if(a != null && a.compareTo(BigInteger.ZERO) >= 0 ){
			return true;
		}
		return false;
	}
	
	public static boolean menorIgualZero(BigInteger a) {
		if(a != null && a.compareTo(BigInteger.ZERO) <= 0 ){
			return true;
		}
		return false;
	}
}
