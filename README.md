#  Stealth Project

Emulating an encrypted tunnel using relays (Based on TOR concept).

## Table of contents

 - [Overview](#overview)
    - [Relay](#relay)
    - [Client](#client)
    - [Trusted Server](#trusted-server)
 - [Examples](#examples)
 - [Example of a client request flow](#example-of-a-client-request-flow)
 - [Setup](#setup)
     - [Configuration](#configuration)   
     - [Install](#install)   
     - [Compile](#compile)   
     - [Run Tests](#run-tests)   
     - [Run](#run)   
 - [Conclusions of Penetration Tests](#conclusions-of-penetration-tests)   
 - [TODO](#todo)   
 
## Overview

The project is separated to 3 applications:

### Relay
Relay is where the traffic passes through before reaching it destination.
Relays add to the speed and robustness of the network.

### Client
Client gets a list of relays, and creates an encrypted chain of messages with their public keys. 

The encrypted chain encrypts an HTTP GET request that will be passed to the initial relay (and passed on until it reaches it's destination).

### Trusted Server
Trusted server is the main server which hold a list of relay's and their public keys.

Clients interact with a trusted server to get list of relays to use in their tunnel to web.

Relays interact with a trusted server to add itself to the network relay's list.

## Examples 

<p align="center"><b>Client</b></p>
<p align="center">
  <img width="690" height="450" src="docs/gifs/client.gif">
</p>

<p align="center"><b>Trusted Server</b></p>
<p align="center">
  <img width="690" height="450" src="docs/gifs/trusted-server.gif">
</p>

<p align="center"><b>Relay</b></p>
<p align="center">
  <img width="690" height="450" src="docs/gifs/relay.gif">
</p>

## Example of a client request flow

Client flow to make an HTTP GET Request to `https://www.google.com`.

1. Requesting TrustedServer for N relays.

    ```
    GET_RELAYS [N]
    ```
 
2. Getting back N relays, with their public keys.

    ```
    RELAY [IP] [PORT] [PUBLIC_KEY]
    ```
    
3. Building a chain of relays (assuming N=3):
    * Clients generates a new RSA 4096 bit key-pair.
    * Encrypting packet with the LAST in chain relay's public key.
    
      ```
      HTTP_GET_REQUEST https://www.google.com [CLIENT_PUBLIC_KEY]
      ```  
        
    * Encrypting packet with the middle relay's public key, with a route IP and PORT to let the relay know which one is the next relay.
      
      ```
      ROUTE [IP] [PORT] [PREVIOUSLY_ENCRYPTED_MESSAGE]
      ```
    
    * Encrypting packet with the first relay's public key, with a route IP and PORT to let the relay know which one is the next relay.
    
      ```
      ROUTE [IP] [PORT] [PREVIOUSLY_ENCRYPTED_MESSAGE]
      ```
      
4. Sending encrypted message to first relay in chain.
    * First relay 
        * Decrypts the message with it's private key.
        * `ROUTE [IP] [PORT] [ENCRYPTED_MESSAGE]`
        * Sends `[ENCRYPTED_MESSAGE]` to `[IP]:[PORT]`
    * Second relay 
        * Decrypts the message with it's private key.
        * `ROUTE [IP] [PORT] [ENCRYPTED_MESSAGE]`
        * Sends `[ENCRYPTED_MESSAGE]` to `[IP]:[PORT]`
    * Third relay
        * Decrypts the message with it's private key.
        * `HTTP_GET_REQUEST https://www.google.com [CLIENT_PUBLIC_KEY]`
        * Makes an HTTP GET request to the requested url.
        * Encrypts the message with the `CLIENT_PUBLIC_KEY`.
        * Returns back the encrypted response.
5. Encrypted message is being propagated back to the client.
6. Client decrypts the message with it's private key.
    
    

## Setup

Setting up the app's.

### Configuration
In `com/openu/security/stealth/app/Services/Config.java` we should configure TrustedServer IP, PORT and PUBLIC_KEY.
This is hardcoded into the client & relays in order to prevent MITM attacks.

```java
public class Config {
    public final static String TRUSTED_SERVER_HOST = "127.0.0.1";
    public final static String TRUSTED_SERVER_PORT = "3000";
    final static public String TRUSTED_SERVER_PUBLIC_KEY = "...";

}

```
### Install

```sh 
$ mvn install
```

### Compile

```sh 
$ mvn clean compile assembly:single
```

### Run tests

```sh 
$ mvn test
```

### Run

```sh 
$ ./bin/stealth 
```

## Conclusions of Penetration Tests

1. MITM Trusted Server - Trusted server private key is contained in the compiled version of this project (Fix in TODO's #1 + #2).
 
2. Detection - Pushing many relay's into the network could result in the detection of the client. Assuming client uses a 3 relays chain, in-case those 3 relays belongs to the one who was trying to detect users, it is possible to expose the user. The exit relay can know what request was made, and the first relay can identify the client, not for sure, since it can't really tell if it's a client or a relay, but this can easily been checked by getting the relay's list from the TrustedServer (Fix in TODO's #3).

3. Denial of service - It is possible to spam the network with fake relays, making it in-accessible to users (Fix in TODO's #4 + #5).

## TODO

1. In order this to be secured, the TrustedServer should be compiled separately!
Since the private-key is hardcoded and the client and relay jar's should not have access to this information.

2. Private & Public key is included in this repository which is not safe to use, generation of a new key pair is required.

3. Exit relays should be from a list of well-known relays in-order to prevent spoofing & detection.

4. TrustedServer should ping relays every X seconds to validate is alive and responds (maybe route a message and get a response?).

5. TrustedServer should check, before adding a new relay, if there's already a relay with the same IP, if so he should handle this scneario (remove old, add new).

