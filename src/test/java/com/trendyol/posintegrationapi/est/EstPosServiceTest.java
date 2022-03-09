package com.trendyol.posintegrationapi.est;

import com.trendyol.posintegrationapi.config.ConfigProperties;
import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.PostResponse;
import com.trendyol.posintegrationapi.model.auth.AuthRequest;
import com.trendyol.posintegrationapi.model.auth.AuthResponse;
import com.trendyol.posintegrationapi.model.refund.RefundRequest;
import com.trendyol.posintegrationapi.model.refund.RefundResponse;
import com.trendyol.posintegrationapi.service.est.EstPosService;
import com.trendyol.posintegrationapi.service.est.model.EstAuthResponse;
import com.trendyol.posintegrationapi.service.est.model.EstRefundResponse;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EstPosServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EstPosService posService = new EstPosService();

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

        EstRefundResponse estRefundResponse = new EstRefundResponse();
        estRefundResponse.setResponse("[refund-ok]");
        estRefundResponse.setMessage("OK");
        estRefundResponse.setErrorCode("");

        PostResponse<EstRefundResponse> postResponse = new PostResponse<EstRefundResponse>();
        postResponse.setResponse(estRefundResponse);

        ResponseEntity<PostResponse<EstRefundResponse>> entityResp = new ResponseEntity<PostResponse<EstRefundResponse>>(postResponse, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(entityResp);

        RefundResponse refundResponse = posService.refund(request);

        assertEquals(refundResponse.getSuccess(), estRefundResponse.isSuccess());
        assertEquals(refundResponse.getResultMessage(), estRefundResponse.getMessage());
    }

    @Test
    void auth_When_Successfully() throws PosException {

        AuthRequest request = Mockito.mock(AuthRequest.class);
        EstAuthResponse estAuthResponse = new EstAuthResponse();
        estAuthResponse.setResponse("Approved");
        estAuthResponse.setAuthCode("00");
        estAuthResponse.setOrderId("123");

        PostResponse<EstAuthResponse> postResponse = new PostResponse<EstAuthResponse>();
        postResponse.setResponse(estAuthResponse);

        ResponseEntity<PostResponse<EstAuthResponse>> entityResp = new ResponseEntity<PostResponse<EstAuthResponse>>(postResponse, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(entityResp);

        AuthResponse response = posService.auth(request);

        assertTrue(response.getSuccess());
        assertEquals(response.getAuthCode(), estAuthResponse.getAuthCode());
    }
}
