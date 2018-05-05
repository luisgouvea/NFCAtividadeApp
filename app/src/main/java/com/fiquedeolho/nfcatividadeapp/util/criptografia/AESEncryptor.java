package com.fiquedeolho.nfcatividadeapp.util.criptografia;


import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptor {

    //private static SecretKey skey;
    /**
     * CHAVE DEVE CONTER ATE 8 CARACTERES
     */
    public static final String ALGORITMO_DES = "DES";
    /**
     * CHAVE DEVE CONTER ATE 16 CARACTERES
     */
    public static final String ALGORITMO_AES = "AES";
    private static Map tamanhosChaves = new HashMap();

    static {
        tamanhosChaves.put(ALGORITMO_DES, new Long(8));
        tamanhosChaves.put(ALGORITMO_AES, new Long(16));
    }

    public static String encrypt(String text, String chave, String algoritmo) {
        try {
            SecretKey skey = getSecretKey(chave, algoritmo);
            Cipher cipher = Cipher.getInstance(algoritmo);
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            return new String(Base64.encode(cipher.doFinal(text.getBytes()), Base64.DEFAULT), "UTF-8");
        } catch (Exception e) {
            try {
                throw new Exception("Ocorreu um erro: " + e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return "";
    }

    public static String decrypt(String text, String chave, String algoritmo) {
        StringBuffer ret = new StringBuffer();
        try {
            SecretKey skey = getSecretKey(chave, algoritmo);
            Cipher cipher = Cipher.getInstance(algoritmo);
            cipher.init(Cipher.DECRYPT_MODE, skey);
            byte[] b = Base64.decode(text, Base64.DEFAULT);
            ret.append(new String(cipher.doFinal(b), "UTF-8"));
        } catch (Exception e) {
            return "Chave Incorreta";
        }
        return ret.toString();
    }

    public static SecretKey getSecretKey(String chave, String algoritmo) {
        String keyString = chave;
        int tam = new Long(tamanhosChaves.get(algoritmo).toString()).intValue();
        byte[] keyB = new byte[tam];
        for (int i = 0; i < keyString.length() && i < keyB.length; i++) {
            keyB[i] = (byte) keyString.charAt(i);
        }
        SecretKey skey = new SecretKeySpec(keyB, algoritmo);
        return skey;
    }

    /*String ec = AESEncryptor.encrypt("1524936148", "teste", "AES");
    String decryp = AESEncryptor.decrypt(ec,"teste", "AES");*/
}
