package com.trendyol.posintegrationapi.tybank;

import com.trendyol.posintegrationapi.config.ConfigProperties;
import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.PostResponse;
import com.trendyol.posintegrationapi.model.refund.RefundRequest;
import com.trendyol.posintegrationapi.model.refund.RefundResponse;
import com.trendyol.posintegrationapi.service.tybank.TyBankPosService;
import com.trendyol.posintegrationapi.service.tybank.model.TyBankRefundResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TyBankServiceTest {
    @InjectMocks
    private TyBankPosService tyBankPosService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ConfigProperties appConf;

    @BeforeEach
    void start() {
        ConfigProperties.Est estConfig = new ConfigProperties.Est();
        estConfig.setAuth("https://www.est.com/pos/api/v1/auth");
        estConfig.setRefund("https://www.est.com/pos/api/v1/refund");

        ConfigProperties.Adyen adyenConfig = new ConfigProperties.Adyen();
        adyenConfig.setRefund("https://www.adyen.com/api/v1/refund");

        ConfigProperties.TyBank tyBankConfig = new ConfigProperties.TyBank();
        tyBankConfig.setRefund("https://www.tybank.com/pos/api/v1/tybank/refund");

        ConfigProperties.EndPoints endPoints = new ConfigProperties.EndPoints();
        endPoints.setEst(estConfig);
        endPoints.setAdyen(adyenConfig);
        endPoints.setTyBank(tyBankConfig);

        Mockito.when(appConf.getEndPoints()).thenReturn(endPoints);
    }

    @Test
    void refund_When_Successfully() throws PosException {

        RefundRequest request = Mockito.mock(RefundRequest.class);

        TyBankRefundResponse tyBankRefundResponse = new TyBankRefundResponse();
        tyBankRefundResponse.setResponse("[refund-received]");
        tyBankRefundResponse.setMessage("OK");
        tyBankRefundResponse.setErrorCode("");

        PostResponse<TyBankRefundResponse> postResponse = new PostResponse<TyBankRefundResponse>();
        postResponse.setResponse(tyBankRefundResponse);

        ResponseEntity<PostResponse<TyBankRefundResponse>> entityResp = new ResponseEntity<PostResponse<TyBankRefundResponse>>(postResponse, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(entityResp);

        RefundResponse refundResponse = tyBankPosService.refund(request);

        assertEquals(tyBankRefundResponse.isSuccess(), refundResponse.getSuccess());
        assertEquals(tyBankRefundResponse.getMessage(), refundResponse.getResultMessage());
    }
}
