package com.openu.security.stealth.app.Protocol;

/**
 * Encrypted proxy protocol
 *
 * TrustedServer - responds to: GET_RELAYS, ADD_RELAY
 *                 sends back: RELAY, -
 *
 * Client        - responds to: -
 *                 sends: GET_RELAYS, HTTP_GET_REQUEST, ROUTE
 *                 recives back: RELAY, -
 *
 * Relay         - responds to: HTTP_GET_REQUEST
 *                 sends back: RESPONSE
 *                 sends: ADD_RELAY, HTTP_GET_REQUEST, ROUTE
 */
public enum ProtocolHeader {
    GET_RELAYS("GET_RELAYS"),
    HTTP_GET_REQUEST("HTTP_GET_REQUEST"),
    ADD_RELAY("ADD_RELAY"),
    RELAY("RELAY"),
    ROUTE("ROUTE"),
    RESPONSE("RESPONSE");

    private final String name;

    private ProtocolHeader(String header) {
        this.name = header;
    }

    public String getName() {
        return name;
    }
}