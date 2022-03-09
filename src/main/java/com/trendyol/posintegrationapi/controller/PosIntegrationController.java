package com.trendyol.posintegrationapi.controller;

import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.auth.AuthRequest;
import com.trendyol.posintegrationapi.model.auth.AuthResponse;
import com.trendyol.posintegrationapi.model.refund.RefundRequest;
import com.trendyol.posintegrationapi.model.refund.RefundResponse;
import com.trendyol.posintegrationapi.service.adyen.sofort.SofortPosService;
import com.trendyol.posintegrationapi.service.est.EstPosService;
import com.trendyol.posintegrationapi.service.tybank.TyBankPosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/makePayment")
public class PosIntegrationController {

    @Autowired
    private SofortPosService sofortPosService;

    @Autowired
    private EstPosService estPosService;

    @Autowired
    private TyBankPosService tyBankPosService;


    @PostMapping("/adyen/refund")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RefundResponse> refundWithAdyenSofort(@RequestBody RefundRequest request) throws PosException {
        return ResponseEntity.ok(sofortPosService.refund(request));
    }

    @PostMapping("/est/auth")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<AuthResponse> authWithEst(@RequestBody AuthRequest request) throws PosException {
        return ResponseEntity.ok(estPosService.auth(request));
    }

    @PostMapping("/est/refund")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RefundResponse> refundWithEst(@RequestBody RefundRequest request) throws PosException {
        return ResponseEntity.ok(estPosService.refund(request));
    }

    @PostMapping("/tyBank/refund")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<RefundResponse> refundWithTyBank(@RequestBody RefundRequest request) throws PosException {
        return ResponseEntity.ok(tyBankPosService.refund(request));
    }
}
