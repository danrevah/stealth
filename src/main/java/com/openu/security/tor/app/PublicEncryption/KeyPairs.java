package com.openu.security.tor.app.PublicEncryption;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyPairs {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(RSAConstants.ALGORITHM_BITS);
        KeyPair pair = keyGen.generateKeyPair();

        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setPublicKeyFromBase64(String key) {
        try{
            byte[] byteKey = DatatypeConverter.parseBase64Binary(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            publicKey = kf.generatePublic(keySpec);
        }
        catch(Exception e){
            publicKey = null;
        }
    }

    public void setPrivateKeyFromBase64(String key) {
        try{
            byte[] byteKey = DatatypeConverter.parseBase64Binary(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            privateKey = kf.generatePrivate(keySpec);
        }
        catch(Exception e){
            privateKey = null;
        }
    }

    public String getPublicKeyAsBase64(){
        return DatatypeConverter.printBase64Binary(publicKey.getEncoded());
    }

    public String getPrivateKeyAsBase64(){
        return DatatypeConverter.printBase64Binary(privateKey.getEncoded());
    }
}