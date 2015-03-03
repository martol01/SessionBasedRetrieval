package com.ucl.search.sbr.channels.main;

import com.ucl.search.sbr.services.dexterEntityLinking.DexterConnection;

/**
 * Created by Martin on 04/02/2015.
 */
public class EntryPoint {
    public static void main(String args[]) {
        System.out.println("Hello World!");
        DexterConnection conn = new DexterConnection();
        String url = "http://localhost:8080/dexter-webapp/api/rest/get-id?title=Johnny_Cash";
        conn.extractEntityId(url);
    }
}
