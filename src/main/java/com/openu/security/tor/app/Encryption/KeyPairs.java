package com.openu.security.tor.app.Encryption;

import java.security.*;

/**
 * KeyPairs
 *
 * Represents public and private keys
 */
public class KeyPairs {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(EncryptionConstants.ALGORITHM);
        keyGen.initialize(EncryptionConstants.BITS);
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

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}