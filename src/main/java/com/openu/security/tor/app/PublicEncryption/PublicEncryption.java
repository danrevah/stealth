package com.openu.security.tor.app.PublicEncryption;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.base.Joiner;

public class PublicEncryption {

    public static byte[] encrypt1(String original, Key publicKey) {
        if (original != null && publicKey != null) {
            byte[] bs = original.getBytes();
            return convert(bs, publicKey, Cipher.ENCRYPT_MODE);
        }

        return null;
    }

    public static String encryptChunks(String packet, PublicKey publicKey) {
        final int CHUNK_SIZE = 200;

        byte[] fullBytes = packet.getBytes();

        ArrayList<String> encryptedChunks = new ArrayList<>();

        // Chunk size 200 since RSA with 2048 bits can only encrypt up to: 2048/11-8 bytes.

        for (int i = 0; i < fullBytes.length; i += CHUNK_SIZE) {
            encryptedChunks.add(
                    DatatypeConverter.printBase64Binary(
                            PublicEncryption.encrypt1(
                                    new String(Arrays.copyOfRange(fullBytes, i, i + CHUNK_SIZE < fullBytes.length ? i + CHUNK_SIZE : fullBytes.length)),
                                    publicKey
                            )
                    )
            );
        }

       return Joiner.on("@").join(encryptedChunks);
    }

    public static String decrypt1(byte[] encrypted, Key privateKey) {
        if (encrypted != null && privateKey != null) {
            byte[] bytes = convert(encrypted, privateKey, Cipher.DECRYPT_MODE);

            if (bytes == null) {
                return null;
            }

            return new String(bytes);
        }

        return null;
    }

    public static String decryptChunks(String data, PrivateKey privateKey) {
        String[] chunks = data.split("@");
        String decrypted = "";

        for (String chunk : chunks) {
            decrypted += PublicEncryption.decrypt1(DatatypeConverter.parseBase64Binary(chunk), privateKey);
        }

        return decrypted;
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