package com.openu.security.tor.app.Protocol;

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
public enum ProtocolHeader {
    GET_RELAYS("GET_RELAYS"),
    HTTP_GET_REQUEST("HTTP_GET_REQUEST"),
    ADD_RELAY("ADD_RELAY"),
    RELAY("RELAY"),
    RESPONSE("RESPONSE");

    private final String name;

    private ProtocolHeader(String header) {
        this.name = header;
    }

    public String getName() {
        return name;
    }
}