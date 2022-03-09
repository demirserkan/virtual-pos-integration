package com.trendyol.posintegrationapi.service.est;


import com.trendyol.posintegrationapi.config.ConfigProperties;
import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.PostResponse;
import com.trendyol.posintegrationapi.model.auth.AuthRequest;
import com.trendyol.posintegrationapi.model.auth.AuthResponse;
import com.trendyol.posintegrationapi.model.refund.RefundRequest;
import com.trendyol.posintegrationapi.model.refund.RefundResponse;
import com.trendyol.posintegrationapi.service.Auth;
import com.trendyol.posintegrationapi.service.PosService;
import com.trendyol.posintegrationapi.service.est.model.EstAuthResponse;
import com.trendyol.posintegrationapi.service.est.model.EstRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EstPosService extends PosService implements Auth {

    @Autowired
    private RestTemplate rt;

    @Autowired
    private ConfigProperties appConf;


    public RefundResponse refund(RefundRequest request) throws PosException {

        try {
            PostResponse<EstRefundResponse> postResponse = post(appConf.getEndPoints().getEst().getRefund(), request, new ParameterizedTypeReference<PostResponse<EstRefundResponse>>() {
            });
            EstRefundResponse response = postResponse.getResponse();
            return new RefundResponse(
                    response.isSuccess(),
                    response.getErrorCode(),
                    response.getMessage(),
                    null,
                    response.getRawBody()
            );
        } catch (Exception e) {
            throw new PosException(String.format("Error occurred when refunding transaction, ReferenceId:%s. Message: %s", request.getRefundReferenceNumber(), e.getMessage()), e);
        }
    }

    @Override
    public AuthResponse auth(AuthRequest request) throws PosException {

        try {
            PostResponse<EstAuthResponse> postResponse = post(appConf.getEndPoints().getEst().getAuth(), request, new ParameterizedTypeReference<PostResponse<EstAuthResponse>>() {
            });
            EstAuthResponse response = postResponse.getResponse();
            return new AuthResponse(
                    response.isSuccess(),
                    response.getProcReturnCode(),
                    response.getErrMsg(),
                    response.getTransactionId(),
                    response.getRawBody(),
                    response.getAuthCode(),
                    response.getHostRefNum()
            );
        } catch (Exception e) {
            throw new PosException(
                    String.format("Error occurred when selling transaction, ReferenceId: %s. Message: %s",
                            request.getReferenceNumber(), e.getMessage()), e);
        }
    }
}
