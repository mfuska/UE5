package de.jojoob.dh;

import java.math.BigInteger;
import java.security.SecureRandom;

public class DHHost {
	SecureRandom secureRandom;
	private BigInteger p;
	private BigInteger g;
	private BigInteger a;
	private BigInteger aA;
	private BigInteger bB;
	private BigInteger k;

	public DHHost() {
		this.secureRandom = new SecureRandom();
	}

	public void generateGPRandom(int pLength) {
		this.p = BigInteger.probablePrime(pLength, this.secureRandom);
		this.g = new BigInteger(p.bitLength(), this.secureRandom).mod(p);
	}

	/**
	 *
	 * @param pLength
	 * @param qLength size of subgroup order
	 */
	public void generateGPDSALike(int pLength, int qLength) {

//		choose q (as subgroup order)
		BigInteger q = BigInteger.probablePrime(qLength, secureRandom);

//		calculate p with p = k * q + 1 // p - 1 | q
		BigInteger p;
		BigInteger k; // do not confuse with this.k
		int kLength = pLength - q.bitLength();
		do {
			do {
				do {
					k = new BigInteger(kLength, secureRandom);
				} while (k.bitLength() == kLength);
				p = k.multiply(q).add(BigInteger.ONE);
			} while (p.bitLength() == pLength);
		} while (!p.isProbablePrime(100));
		this.p = p;

//		calculate g with order q
		BigInteger h;
		BigInteger g = BigInteger.ONE;
		do {
			h = new BigInteger(p.bitLength(), this.secureRandom);
			if (h.compareTo(BigInteger.ONE) == 1 && h.compareTo(p.subtract(BigInteger.ONE)) == -1) {
				g = h.modPow(k, p);
			}
		} while (g.compareTo(BigInteger.ONE) == 0);
		this.g = g;
	}

	public void generateGPsavePrime(int pLength) {
		this.g = new BigInteger("2");
		BigInteger q;
		BigInteger p;
		do {
			q = BigInteger.probablePrime(pLength - 1, this.secureRandom);
			p = g.multiply(q).add(BigInteger.ONE);
		} while (!p.isProbablePrime(100));
		this.p = p;
	}

	public void generateGPsavePrimeRoot(int pLength) {
		this.g = new BigInteger("2");
		BigInteger q;
		BigInteger p;
		do {
			do {
				q = BigInteger.probablePrime(pLength - 1, this.secureRandom);
				p = g.multiply(q).add(BigInteger.ONE);
			} while (!p.isProbablePrime(100));
		} while (this.g.modPow(q, p).compareTo(BigInteger.ONE) == 0);
		this.p = p;
	}

	public void generateA() {
		BigInteger a;
		BigInteger aA;
		do {
			a = new BigInteger(this.p.bitLength(), this.secureRandom).mod(p);
			aA = this.g.modPow(a, this.p);
		} while (aA.compareTo(BigInteger.ONE) == 0 || aA.compareTo(a) == 0);
		this.a = a;
		this.aA = aA;
	}

	public BigInteger getP() {
		return this.p;
	}

	public BigInteger getG() {
		return this.g;
	}

	public void setP(BigInteger p) {
		this.p = p;
	}

	public void setG(BigInteger g) {
		this.g = g;
	}

	public BigInteger getA() {
		return this.aA;
	}

	public void setB(BigInteger bB) {
		this.bB = bB;
	}

	public void generateK() {
		this.k = this.bB.modPow(this.a, this.p);
	}

	public BigInteger getK() {
		return this.k;
	}
}
