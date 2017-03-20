/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.security;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密模块
 * 
 * @author liangjie.li
 * @version $Id: SecureIdentityLoginModule.java, v 0.1 2012-8-8 下午5:12:38 liangjie.li Exp $
 */
public class SecureIdentityLoginModule {

    // dev key
    private static byte[] ENC_KEY_BYTES      = "jaas is the way".getBytes();

    // prod key
    private static byte[] ENC_KEY_BYTES_PROD = "gQzLk5tTcGYlQ47GG29xQxfbHIURCheJ".getBytes();

    public static String encode(String secret) throws NoSuchPaddingException,
                                              NoSuchAlgorithmException, InvalidKeyException,
                                              BadPaddingException, IllegalBlockSizeException {
        return SecureIdentityLoginModule.encode(null, secret);
    }

    public static String encode(String encKey, String secret) throws InvalidKeyException,
                                                             NoSuchAlgorithmException,
                                                             NoSuchPaddingException,
                                                             IllegalBlockSizeException,
                                                             BadPaddingException {
        byte[] kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES_PROD;
        if (isNotBlank(encKey)) {
            kbytes = encKey.getBytes();
        }

        // 默认采用prod key加密与解密,线下环境会异常;
        try {
            return initEncode(kbytes, secret);
        } catch (InvalidKeyException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        } catch (NoSuchAlgorithmException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        } catch (NoSuchPaddingException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        } catch (IllegalBlockSizeException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        } catch (BadPaddingException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        }

        return initEncode(kbytes, secret);
    }

    static final String initEncode(byte[] kbytes, String secret) throws NoSuchAlgorithmException,
                                                                NoSuchPaddingException,
                                                                InvalidKeyException,
                                                                IllegalBlockSizeException,
                                                                BadPaddingException {
        SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encoding = cipher.doFinal(secret.getBytes());
        BigInteger n = new BigInteger(encoding);
        return n.toString(16);
    }

    public static char[] decode(String secret) throws NoSuchPaddingException,
                                              NoSuchAlgorithmException, InvalidKeyException,
                                              BadPaddingException, IllegalBlockSizeException {
        return SecureIdentityLoginModule.decode(null, secret).toCharArray();
    }

    public static String decode(String encKey, String secret) throws NoSuchPaddingException,
                                                             NoSuchAlgorithmException,
                                                             InvalidKeyException,
                                                             BadPaddingException,
                                                             IllegalBlockSizeException {

        byte[] kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES_PROD;
        if (isNotBlank(encKey)) {
            kbytes = encKey.getBytes();
        }

        try {
            return iniDecode(kbytes, secret);
        } catch (InvalidKeyException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        } catch (BadPaddingException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        } catch (IllegalBlockSizeException e) {
            kbytes = SecureIdentityLoginModule.ENC_KEY_BYTES;
        }
        return iniDecode(kbytes, secret);
    }

    static final String iniDecode(byte[] kbytes, String secret) throws NoSuchPaddingException,
                                                               NoSuchAlgorithmException,
                                                               InvalidKeyException,
                                                               BadPaddingException,
                                                               IllegalBlockSizeException {
        SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");
        BigInteger n = new BigInteger(secret, 16);
        byte[] encoding = n.toByteArray();
        // SECURITY-344: fix leading zeros
        if (encoding.length % 8 != 0) {
            int length = encoding.length;
            int newLength = ((length / 8) + 1) * 8;
            int pad = newLength - length; //number of leading zeros
            byte[] old = encoding;
            encoding = new byte[newLength];
            for (int i = old.length - 1; i >= 0; i--) {
                encoding[i + pad] = old[i];
            }
        }
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decode = cipher.doFinal(encoding);
        return new String(decode);
    }

    static final boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    static final boolean isBlank(String str) {
        int strLen = 0;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException,
                                          NoSuchAlgorithmException, BadPaddingException,
                                          IllegalBlockSizeException {
        //        System.out.println(encode("ali88"));
        System.out.println(decode("-19c84bf1dcbecee0917eaefd81d23fbf"));
    }

}
