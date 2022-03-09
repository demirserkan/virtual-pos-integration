package com.trendyol.posintegrationapi.service.tybank;


import com.trendyol.posintegrationapi.config.ConfigProperties;
import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.PostResponse;
import com.trendyol.posintegrationapi.model.refund.RefundRequest;
import com.trendyol.posintegrationapi.model.refund.RefundResponse;
import com.trendyol.posintegrationapi.service.PosService;
import com.trendyol.posintegrationapi.service.tybank.model.TyBankRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class TyBankPosService extends PosService {

    @Autowired
    private ConfigProperties appConf;

    public RefundResponse refund(RefundRequest request) throws PosException {

        try {
            PostResponse<TyBankRefundResponse> postResponse = post(appConf.getEndPoints().getTyBank().getRefund(), request, new ParameterizedTypeReference<PostResponse<TyBankRefundResponse>>() {
            });
            TyBankRefundResponse response = postResponse.getResponse();
            return new RefundResponse(
                    response.isSuccess(),
                    response.getErrorCode(),
                    response.getMessage(),
                    null,
                    null
            );
        } catch (Exception e) {
            throw new PosException(String.format("Error occurred when refunding transaction, ReferenceId:%s. Message: %s", request.getRefundReferenceNumber(), e.getMessage()), e);
        }
    }
}
