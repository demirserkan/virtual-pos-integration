package com.trendyol.posintegrationapi.adyen;

import com.trendyol.posintegrationapi.config.ConfigProperties;
import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.PostResponse;
import com.trendyol.posintegrationapi.model.refund.RefundRequest;
import com.trendyol.posintegrationapi.model.refund.RefundResponse;
import com.trendyol.posintegrationapi.service.adyen.model.AdyenRefundResponse;
import com.trendyol.posintegrationapi.service.adyen.sofort.SofortPosService;
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
public class SofortPosServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SofortPosService posService = new SofortPosService();

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

        AdyenRefundResponse adyenRefundResponse = new AdyenRefundResponse();
        adyenRefundResponse.setResponse("[refund-received]");
        adyenRefundResponse.setMessage("OK");
        adyenRefundResponse.setPspReference("1234");
        adyenRefundResponse.setErrorCode("");

        PostResponse<AdyenRefundResponse> postResponse = new PostResponse<>();
        postResponse.setResponse(adyenRefundResponse);

        ResponseEntity<PostResponse<AdyenRefundResponse>> entityResp = new ResponseEntity<PostResponse<AdyenRefundResponse>>(postResponse, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(entityResp);

        RefundResponse refundResponse = posService.refund(request);

        assertEquals(refundResponse.getSuccess(), adyenRefundResponse.isSuccess());
        assertEquals(refundResponse.getResultMessage(), adyenRefundResponse.getMessage());
    }

}
