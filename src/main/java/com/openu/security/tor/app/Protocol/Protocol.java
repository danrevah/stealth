package com.openu.security.tor.app;

/**
 * Encrypted proxy protocol
 *
 * TrustedServer - responds to: GET_RELAYS, ADD_RELAY
 *                 sends back: RELAY, -
 *
 * Client        - responds to: -
 *                 sends: GET_RELAYS, HTTP_GET_REQUEST
 *                 recives back: RELAY, -
 *
 * Relay         - responds to: HTTP_GET_REQUEST
 *                 sends back: RESPONSE
 *                 sends: ADD_RELAY, HTTP_GET_REQUEST
 */
public class Protocol {
    public static final String GET_RELAYS = "GET_RELAYS";
    public static final String HTTP_GET_REQUEST = "HTTP_GET_REQUEST";
    public static final String ADD_RELAY = "ADD_RELAY";
    public static final String RELAY = "RELAY";
    public static final String RESPONSE = "RESPONSE";
}