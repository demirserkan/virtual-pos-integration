package com.trendyol.posintegrationapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class ConfigProperties {
    private EndPoints endPoints;

    public EndPoints getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(EndPoints endPoints) {
        this.endPoints = endPoints;
    }

    public static class EndPoints {
        private Adyen adyen;
        private Est est;
        private TyBank tyBank;

        public Adyen getAdyen() {
            return adyen;
        }

        public Est getEst() {
            return est;
        }

        public TyBank getTyBank() {
            return tyBank;
        }

        public void setAdyen(Adyen adyen) {
            this.adyen = adyen;
        }

        public void setEst(Est est) {
            this.est = est;
        }

        public void setTyBank(TyBank tyBank) {
            this.tyBank = tyBank;
        }
    }

    public static class Adyen {
        private String refund;

        public String getRefund() {
            return refund;
        }

        public void setRefund(String refund) {
            this.refund = refund;
        }
    }

    public static class Est {
        private String auth;
        private String refund;

        public String getAuth() {
            return auth;
        }

        public String getRefund() {
            return refund;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public void setRefund(String refund) {
            this.refund = refund;
        }
    }

    public static class TyBank {
        private String refund;

        public String getRefund() {
            return refund;
        }

        public void setRefund(String refund) {
            this.refund = refund;
        }
    }

}
