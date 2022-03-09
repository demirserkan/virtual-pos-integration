package com.trendyol.posintegrationapi.service;

import com.trendyol.posintegrationapi.exception.PosException;
import com.trendyol.posintegrationapi.model.auth.AuthRequest;
import com.trendyol.posintegrationapi.model.auth.AuthResponse;

public interface Auth {
    AuthResponse auth(AuthRequest request) throws PosException;
}
