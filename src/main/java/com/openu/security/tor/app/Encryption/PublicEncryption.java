package com.openu.security.tor.app.Encryption;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.base.Joiner;
import com.openu.security.tor.app.Logger.Logger;

public class PublicEncryption {

    // Chunk size 200 since RSA with 2048 bits can only encrypt up to: 256 bytes (2048/11-8 = 256).
    private static final int CHUNK_SIZE = 200;

    /**
     * Encryption method, used to encrypt via a publicKey.
     * Splitting to chunks, since RSA cannot handle length longer than [BITS]/11-8.
     *
     * @param packet - string to encrypt
     * @param publicKey - public key to use
     * @return encrypted string
     */
    public static String encrypt(String packet, PublicKey publicKey) {
        byte[] fullBytes = packet.getBytes();

        ArrayList<String> encryptedChunks = new ArrayList<>();

        for (int i = 0; i < fullBytes.length; i += CHUNK_SIZE) {
            encryptedChunks.add(
                DatatypeConverter.printBase64Binary(
                    PublicEncryption.encryptChunk(
                        new String(Arrays.copyOfRange(fullBytes, i, i + CHUNK_SIZE < fullBytes.length ? i + CHUNK_SIZE : fullBytes.length)),
                        publicKey
                    )
                )
            );
        }

       return Joiner.on("@").join(encryptedChunks);
    }

    /**
     * Decryption method, used to decrypt via a privateKey.
     * Retrieving chunks - and decrypting each.
     *
     * @param data - string to decrypt
     * @param privateKey - private key to use
     * @return decrypted string
     */
    public static String decrypt(String data, PrivateKey privateKey) {
        String[] chunks = data.split("@");
        String decrypted = "";

        for (String chunk : chunks) {
            decrypted += PublicEncryption.decryptChunk(DatatypeConverter.parseBase64Binary(chunk), privateKey);
        }

        return decrypted;
    }


    //-- Private
    private static byte[] encryptChunk(String original, Key publicKey) {
        if (original != null && publicKey != null) {
            byte[] bs = original.getBytes();
            return convert(bs, publicKey, Cipher.ENCRYPT_MODE);
        }

        return null;
    }


    private static String decryptChunk(byte[] encrypted, Key privateKey) {
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
            Cipher cipher = Cipher.getInstance(EncryptionConstants.ALGORITHM);
            cipher.init(mode, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter= new PrintWriter(writer);
            e.printStackTrace(printWriter);
            Logger.error("Encryption failure ::: " + writer.toString());
        }

        return null;
    }
}