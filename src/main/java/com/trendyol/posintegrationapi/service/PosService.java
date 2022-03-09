package com.trendyol.posintegrationapi.service;


import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.PostResponse;
import com.trendyol.posintegrationapi.model.refund.RefundRequest;
import com.trendyol.posintegrationapi.model.refund.RefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class PosService {

    @Autowired
    private RestTemplate rt;

    public abstract RefundResponse refund(RefundRequest request) throws PosException;

    protected <T> PostResponse<T> post(String url, Object request, ParameterizedTypeReference<PostResponse<T>> typeReference) {

        HttpEntity<Object> httpEntity = new HttpEntity<Object>(request);
        ResponseEntity<PostResponse<T>> response = rt.exchange(url, HttpMethod.POST, httpEntity, typeReference);
        return response.getBody();
    }
}
