/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

/**
 *
 * @author Thomas Bocek
 */
public class KeyTO extends BaseTO<KeyTO> {

    //TODO: use something like: https://gist.github.com/orip/3635246
    private byte[] publicKey;

    public KeyTO publicKey(byte[] publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public byte[] publicKey() {
        return publicKey;
    }
}
