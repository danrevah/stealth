package com.openu.security.stealth.app.Encryption;

import javax.xml.bind.DatatypeConverter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * KeyHelper
 *
 * Converting between base64 to public/private keys and vice-versa.
 */
public class KeyHelper {

    public static PublicKey base64ToPublicKey(String key) {
        try{
            byte[] byteKey = DatatypeConverter.parseBase64Binary(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(keySpec);
        }
        catch(Exception e){
            return null;
        }
    }

    public static PrivateKey base64ToPrivateKey(String key) {
        try{
            byte[] byteKey = DatatypeConverter.parseBase64Binary(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePrivate(keySpec);
        }
        catch(Exception e){
            return null;
        }
    }

    public static String privateKeyToBase64(PrivateKey key) {
        return DatatypeConverter.printBase64Binary(key.getEncoded());
    }

    public static String publicKeyToBase64(PublicKey key) {
        return DatatypeConverter.printBase64Binary(key.getEncoded());
    }
}
