package com.gosun.core.utils.http;

import org.apache.http.HttpResponse;

public class HttpWrapResponse {
    private int httpCode = -1;
    private String httpResponseContext = "通信失败";
    private HttpResponse response;
    
    public int getHttpCode() {
        return httpCode;
    }
    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
    public String getHttpResponseContext() {
        return httpResponseContext;
    }
    public void setHttpResponseContext(String httpResponseContext) {
        this.httpResponseContext = httpResponseContext;
    }
    public HttpResponse getResponse() {
        return response;
    }
    public void setResponse(HttpResponse response) {
        this.response = response;
    }
}
