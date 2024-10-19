package org.skypro.be.javagptbot.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

@Configuration
public class GigaChatConfig {

    @Bean(name = "gigaChatAuthorizationKey")
    public String gigaChatAuthorizationKey() {
        return System.getenv("GIGACHAT_AUTHORIZATION_KEY");
    }

    @Bean(name = "gigaChatKeyScope")
    public String gigaChatKeyScope() {
        return "scope=GIGACHAT_API_PERS";
    }

    @Bean(name = "gigaChatClient")
    public OkHttpClient gigaChatClient() {
        return createUnsafeOkHttpClient();
    }

    //Create a trust manager that does not validate certificate chains
    private static OkHttpClient createUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            builder.connectTimeout(180, TimeUnit.SECONDS);
            builder.readTimeout(180, TimeUnit.SECONDS);
            builder.writeTimeout(180, TimeUnit.SECONDS);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
