package com.smartdesigns.smarthomehci.repository;

import java.net.MalformedURLException;
import java.net.URL;

public class ApiConnection {

    private URL apiUrl;

    public ApiConnection(String url) throws MalformedURLException {
        this.apiUrl = new URL(url);
    }



}
