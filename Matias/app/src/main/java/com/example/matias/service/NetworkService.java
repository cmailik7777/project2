package com.example.matias.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public  class NetworkService {

    public void executeRequest(String url, HttpEntity entity) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        HttpResponse resArr = httpClient.execute(httpPost);
        HttpEntity entity = resArr.getEntity();
        is = entity.getContent();
    }
}
