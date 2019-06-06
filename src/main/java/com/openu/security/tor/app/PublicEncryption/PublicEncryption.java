package com.openu.security.tor.app.PublicEncryption;

import javax.crypto.Cipher;
import java.security.Key;

public class PublicEncryption {

    public static byte[] encrypt(String original, Key publicKey) {
        if (original != null && publicKey != null) {
            byte[] bs = original.getBytes();
            return convert(bs, publicKey, Cipher.ENCRYPT_MODE);
        }

        return null;
    }

    public static String decrypt(byte[] encrypted, Key privateKey) {
        if (encrypted != null && privateKey != null) {
            byte[] bytes = convert(encrypted, privateKey, Cipher.DECRYPT_MODE);

            if (bytes == null) {
                return null;
            }

            return new String(bytes);
        }

        return null;
    }

    private static byte[] convert(byte[] data, Key key, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(RSAConstants.ALGORITHM);
            cipher.init(mode, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}