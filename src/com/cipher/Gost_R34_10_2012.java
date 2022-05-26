package com.cipher;

import ru.zinal.gosthash.GostHash;

import java.math.BigInteger;
import java.security.spec.ECPoint;
import java.util.Random;

/**
 * Created by ilya on 27/09/14.
 */
public class Gost_R34_10_2012 {

    //variables that set the elliptic curve E (through J(E))
    private static final BigInteger a = new BigInteger("7",10);
    private static final BigInteger b = new BigInteger("43308876546767276905765904595650931995942111794451039583252968842033849580414",10);

    //simple value p > 3
    private static final BigInteger p = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564821041",10);

    //group order of points in elliptic curve E
    private static final BigInteger q = new BigInteger("57896044618658097711785492504343953927082934583725450622380973592137631069619",10);

    //Elliptic point P - used to calc elliptic point C
    private static final BigInteger xP = new BigInteger("2",10);
    private static final BigInteger yP = new BigInteger("4018974056539037503335449422937059775635739389905545080690979365213431566280",10);
    private static final ECPoint P = new ECPoint(xP,yP);

    public Gost_R34_10_2012(){}

    public static String Sign(byte[] hash, BigInteger d)
    {
        //Step 1 - find the hash of the message
        //Step 2 - find the alpha
        BigInteger alpha = new BigInteger(hash);
        BigInteger e = alpha.mod(q);
        if(e == BigInteger.ZERO)
            e = BigInteger.ONE;

        //e = new BigInteger("20798893674476452017134061561508270130637142515379653289952617252661468872421",10);

        BigInteger r;
        BigInteger s;
        BigInteger k;

        do {
            //Step 3 - generate pseudo random k => 0<k<q
            do {
                k = new BigInteger(q.bitLength(), new Random());
            } while ((k.compareTo(q) != -1) || (k.compareTo(BigInteger.ZERO) != 1));

            //k = new BigInteger("53854137677348463731403841147996619241504003434302020712960838528893196233395",10);

            //Step 4 - calculate the point C = k*P;
            ECPoint C = ECArithmetics.scalmult(P, k);
            r = C.getAffineX().mod(q);

            //Step 5 - calculate value s
            // s = (r*k + k*e) % q
            s = ((r.multiply(d)).add(k.multiply(e))).mod(q);
        }while(r.compareTo(BigInteger.ZERO) == 0 || s.compareTo(BigInteger.ZERO) == 0);

        //Step 6 - calculate Rvector and Svector
        String Rvector = padding(r.toString(16), q.bitLength() / 4);
        String Svector = padding(s.toString(16), q.bitLength() / 4);

        //signeture;
        return Rvector+Svector;
    }

    public static boolean SignCheck(byte[] hash, String sign, ECPoint Q)
    {
        //Step 1 - get r and s from signature
        String Rvector = sign.substring(0, q.bitLength() / 4);
        String Svector = sign.substring(q.bitLength()/4);

        BigInteger r = new BigInteger(Rvector,16);
        BigInteger s = new BigInteger(Svector,16);
        //r = new BigInteger("29700980915817952874371204983938256990422752107994319651632687982059210933395",10);
        //s = new BigInteger("574973400270084654178925310019147038455227042649098563933718999175515839552",10);

        if(r.compareTo(BigInteger.ZERO) != 1 || r.compareTo(q) != -1 || s.compareTo(BigInteger.ZERO) != 1 || s.compareTo(q) != -1 )
            return false;
        //Step 2 - calculate e and alpha from hash
        BigInteger alpha = new BigInteger(hash);
        BigInteger e = alpha.mod(q);

        if(e.compareTo(BigInteger.ZERO) == 0)
            e = BigInteger.ONE;

        //e = new BigInteger("20798893674476452017134061561508270130637142515379653289952617252661468872421",10);

        BigInteger v = e.modInverse(q);//.mod(q);
        BigInteger z1 = (s.multiply(v)).mod(q);
        BigInteger z2 = (BigInteger.valueOf(-1).multiply(r.multiply(v))).mod(q);

        ECPoint A = ECArithmetics.scalmult(P,z1);
        ECPoint B = ECArithmetics.scalmult(Q,z2);
        ECPoint C = ECArithmetics.addPoint(A,B);

        BigInteger R = C.getAffineX().mod(q);

        if(R.compareTo(r) == 0)
            return true;

        return false;
    }

    public static ECPoint getEllipticPointP()
    {return P;}

    public static BigInteger getP()
    {return p;}

    public static BigInteger getA()
    {return a;}

    private static String padding(String input, int size)
    {
        if (input.length() < size)
        {
            do
            {
                input = "0" + input;
            } while (input.length() < size);
        }
        return input;
    }


    public static BigInteger getB() {
        return b;
    }
}
