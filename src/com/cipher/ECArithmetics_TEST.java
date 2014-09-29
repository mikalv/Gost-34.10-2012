package com.cipher;

import java.math.BigInteger;
import java.security.spec.ECPoint;

/**
 * Created by ilya on 29/09/14.
 */
public class ECArithmetics_TEST {

    public static void main(String[] args) {

        BigInteger xs = new BigInteger("d458e7d127ae671b0c330266d246769353a012073e97acf8", 16);
        BigInteger ys = new BigInteger("325930500d851f336bddc050cf7fb11b5673a1645086df3b", 16);
        BigInteger xt = new BigInteger("f22c4395213e9ebe67ddecdd87fdbd01be16fb059b9753a4", 16);
        BigInteger yt = new BigInteger("264424096af2b3597796db48f8dfb41fa9cecc97691a9c79", 16);
        ECPoint S = new ECPoint(xs,ys);
        ECPoint T = new ECPoint(xt,yt);
        // Verifying addition

        ECPoint Rst = ECArithmetics.addPoint(S, T);
        // Specified value of x of point R for addition  in NIST Routine example
        BigInteger xst = new BigInteger("48e1e4096b9b8e5ca9d0f1f077b8abf58e843894de4d0290", 16);
        System.out.println("\nx-coordinate of point Rst is : " + Rst.getAffineX());
        System.out.println("\ny-coordinate of point Rst is : " + Rst.getAffineY());
        if(Rst.getAffineX().equals(xst))
            System.out.println("Adding is correct");

        //Verifying Doubling
        // Specified value of x of point R for doubling  in NIST Routine example
        BigInteger xr = new BigInteger("30c5bc6b8c7da25354b373dc14dd8a0eba42d25a3f6e6962", 16);
        BigInteger yr = new BigInteger("0dde14bc4249a721c407aedbf011e2ddbbcb2968c9d889cf", 16);
        // Specified value of y of point R for doubling  in NIST Routine example
        ECPoint R2s = new ECPoint(xr, yr);
        System.out.println("\nx-coordinate of point R2s is : " + R2s.getAffineX());
        System.out.println("\ny-coordinate of point R2s is : " + R2s.getAffineY());
        System.out.println("\nx-coordinate of calculated point is : " + ECArithmetics.doublePoint(S).getAffineX());
        System.out.println("\ny-coordinate of calculated point is : " +    ECArithmetics.doublePoint(S).getAffineY());
        if(R2s.getAffineX().equals(ECArithmetics.doublePoint(S).getAffineX()) &&
                R2s.getAffineY().equals(ECArithmetics.doublePoint(S).getAffineY()))
            System.out.println("Doubling is correct");

        // Specified value of x of point R for scalar Multiplication  in NIST Routine example
        xr = new BigInteger("1faee4205a4f669d2d0a8f25e3bcec9a62a6952965bf6d31", 16);
        // Specified value of y of point R for scalar Multiplication  in NIST Routine example
        yr = new BigInteger("5ff2cdfa508a2581892367087c696f179e7a4d7e8260fb06", 16);
        ECPoint Rds = new ECPoint(xr, yr);
        BigInteger d = new BigInteger("a78a236d60baec0c5dd41b33a542463a8255391af64c74ee", 16);

        ECPoint Rs = ECArithmetics.scalmult(S, d);

        System.out.println("\nx-coordinate of point Rds is : " + Rds.getAffineX());
        System.out.println("\ny-coordinate of point Rds is : " + Rds.getAffineY());
        System.out.println("\nx-coordinate of calculated point is : " + Rs.getAffineX());
        System.out.println("\ny-coordinate of calculated point is : " + Rs.getAffineY());


        if(Rds.getAffineX().equals(Rs.getAffineX()) &&
                Rds.getAffineY().equals(Rs.getAffineY()))
            System.out.println("Scalar Multiplication is correct");

    }
}