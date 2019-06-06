package com.openu.security.tor.app.PublicEncryption;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PublicEncryptionTest {

    private final static String MESSAGE = "foo bar";
    private final static String ENCRYPTED_MESSAGE = "GHA08UiP/ImCDsDylT8e0MJGaPIJlQk/pu8kn+pZNA1hiFpDDUEAnD3QHJUqWIzNBisC9EgYAOw6epxQPK9+X9WHDmIHXZB9f0VQqqpsf7616AmuWB8iX1cFrymFCfYzu0gMzHfvJivu0jcwfbxNx2a/qSNYZdBozFuWJgIF/XuF2MauQomlz9VN6B/SvVOz2tbCHUOoRSs4aVbcBJjrLMRuFRbviHx83434oAipFsR289FFuwQDOQtI+s99VU2Mjeuwi/4Gj0G1UYaffT+eBkvj/LZiId2L6GDok0YeRRPWiqZC8dZI9SOv6nGvSa1+rDWM907tmC73vMqnFlgI9A==";
    private final static String PUBLIC_KEY_FOR_TESTS = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg93Y5AcqkOU4m+dsTT36b4fsi1qUBamg7UqJvIsHCSpJ7H2jhLKBBWLmbD4+0SN5wnecgbjZu6JvpuotfmdCxpqEZ1fC1SCteeLuDsSWsej0jiErHoIumqf36sZTXn8HmM1qdrfJIt9hlk83wzHHaipbatP3B2q17jkKMRWsFc9pewGVWPVhFwbf0q+KdXkYVqBLhkj/0ZwpEdSoPYx3Tjk32Szq4w/v5/uT3EYVCYZcYL57EEcPHEoI0kv70fHNzPXEgbK0lbb9kxe1iCpYcIweoOv4c5lH2Sv1Ki3x+36MERtnf2hwVX+3+8ZnFX8X/fMOqFqy5FXQpeFbwbrQQwIDAQAB";
    private final static String PRIVATE_KEY_FOR_TESTS = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCD3djkByqQ5Tib52xNPfpvh+yLWpQFqaDtSom8iwcJKknsfaOEsoEFYuZsPj7RI3nCd5yBuNm7om+m6i1+Z0LGmoRnV8LVIK154u4OxJax6PSOISsegi6ap/fqxlNefweYzWp2t8ki32GWTzfDMcdqKltq0/cHarXuOQoxFawVz2l7AZVY9WEXBt/Sr4p1eRhWoEuGSP/RnCkR1Kg9jHdOOTfZLOrjD+/n+5PcRhUJhlxgvnsQRw8cSgjSS/vR8c3M9cSBsrSVtv2TF7WIKlhwjB6g6/hzmUfZK/UqLfH7fowRG2d/aHBVf7f7xmcVfxf98w6oWrLkVdCl4VvButBDAgMBAAECggEAZ0Am2m0uDCQfDhvHdjeoRvoD4h17N2/ZfNNXQ9UMHkYJee3qQjXOxlvpFioq33DKntUvK3eErOGEUQqdspLB+2XzdEOIX37ltkFUqyWM5SMFkeAwtgOy4A/TE/ZUQvJuDVwSF4DSMcb8z0eZ3vG0NdGSoPP1Qc5xQ2VQMJzKkIwYKgzsERsbhIIfmdjQ+8f2vBh6d6LFQbb8gdBGFOmgDHHYAVZpFFKX7ejCceDU+2Z2SQ7YxUXVJ39/HO/VNwF98k2fS/nnRJmboxeMt8KLulXIaJ+uI90dtnDOf4UqaFFk1Za/kUrboHBFnOhrU0zt+RweuJMAf+iXsZfkYOj56QKBgQD7EdVf5uidsNvByaTMuNBVjY1hIDSvWU3gIFqfppWtRc6Gmr9v2Nyyrp+zQtnuGiRIjy4WoXSd94sn28dsT60ykW7GQCIVfJxLERkcdNJJQH8d6j/kRafZomcM6fopiO0rcO2C5IHOdDNo2q6kTGmaSD3DyO+OLMJCLx2RMa2pNQKBgQCGdMLf8UIdhgnkM0uC83TXBQsKXcqx+n9GFzaJIpJo66/UbxTQDNxeNmMatel/CoFGH35LOAwLooejyIBehlIC8hMPgmX3T/oGRWom/sip+cKbLflJXgYYoiU4oO08n8mM7mfY11958DSB3pEsCRV8FVAe3PSYJfU/woBz4N46lwKBgQCr/LVsMqOakjKX92e7Dcc5M/RrWkJ88/IS3Fb4Eodp4O3u60bEeYwFovWnON5M6v977QknW4alR5O9xCJCIfqFL3bwu7IdgfUWvymygpwirdnmRjdqSgUHYuaww/8oQLInFf69sYbyREuLQnJr/iRBGcelvJnNjP+tktTwR1WtGQKBgBgZITxF31WvSmKOFwCXZd+L1di7yrJvdRQ44NtfSOJpK5BlgFNxmqWAFe8Zn4nXdif9SQNuPipvrOPb8bX/WP+SJ3xmIKRcvzEUi9wFWi2syAZMDRvrMCiGFym5Hgv5j4QkzpP8te4C8UY9f8NmtWUR5NRDQctrMjOFRsR+DxZtAoGBAO/tNsTvfKJ4COf4PEbdLtC1MLuNH5y4uRkM17apgkd/V/VQVKzDr2ZcxWESKAX8TrMlVRpn9VGr6MS5+JovpNfHys19XI8dT5/HuxascK3njNcP6SH52hNscqlKd9QgNUn0CpTB0l1mtwrMu6VSbgf6gm3uxICh5u1jwY6UkXz7";

    @Test
    public void shouldDecryptMessage()
    {
        try {
            KeyPairs keyPairs = new KeyPairs();

            keyPairs.setPublicKeyFromBase64(PublicEncryptionTest.PUBLIC_KEY_FOR_TESTS);
            keyPairs.setPrivateKeyFromBase64(PublicEncryptionTest.PRIVATE_KEY_FOR_TESTS);

            byte[] encrypted = DatatypeConverter.parseBase64Binary(PublicEncryptionTest.ENCRYPTED_MESSAGE);
            String decryptedString = PublicEncryption.decrypt(encrypted, keyPairs.getPrivateKey());

            assertEquals(PublicEncryptionTest.MESSAGE, decryptedString);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldEncryptAndDecrypt()
    {
        try {
            KeyPairs keyPairs = new KeyPairs();

            keyPairs.generateKeyPair();

            byte[] encryptedBytes = PublicEncryption.encrypt(PublicEncryptionTest.MESSAGE, keyPairs.getPublicKey());
            String base64encrypted = DatatypeConverter.printBase64Binary(encryptedBytes);

//            System.out.println(base64encrypted);
//            System.out.println(keyPairs.getPublicKeyAsBase64());
//            System.out.println(keyPairs.getPrivateKeyAsBase64());

            byte[] encryptedBytesDecode = DatatypeConverter.parseBase64Binary(base64encrypted);

            assertArrayEquals(encryptedBytesDecode, encryptedBytes);

            String decryptedString = PublicEncryption.decrypt(encryptedBytesDecode, keyPairs.getPrivateKey());

            assertEquals(PublicEncryptionTest.MESSAGE, decryptedString);

        } catch (Exception e) {
            fail();
        }
    }
}
