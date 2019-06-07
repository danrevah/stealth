package com.openu.security.tor.app.PublicEncryption;

/**
 * Protocol concept behind the chained encryption
 *  1. HTTP_GET_REQUEST [HTTP_URL] [PUBLIC_KEY]
 *  2. ROUTE [IP] [PORT] [ENC_MESSAGE of 1]
 *  3. ROUTE [IP] [PORT] [ENC_MESSAGE of 2]
 *  ...
 *  N. ROUTE [IP] [PORT] [ENC_MESSAGE of N-1]
 *
 *
 * Scenarios:
 *  1. Relay receives a ROUTE request and sends the ENC_MESSAGE next to the specified IP and PORT.
 *     Waiting for response and transferring it back to previous relay.
 *
 *  2. Relay receives an HTTP_GET_REQUEST request and sends a GET request to the specified HTTP_URL,
 *     Encrypting back the data using the PUBLIC_KEY and return back.
 */
public class ChainedEncryption {
}
