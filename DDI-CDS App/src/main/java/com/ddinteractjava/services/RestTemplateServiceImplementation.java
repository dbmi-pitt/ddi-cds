package com.ddinteractjava.services;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for getting Rest Templates. This service ignores SSL Certificate errors if running with profile
 * development or test.
 */
@Service
class RestTemplateServiceImpl implements RestTemplateService {

    /**
     * Spring environment.
     */
    @Autowired
    Environment env;

    /**
     * Get a rest template. This ignores SSL Certificate errors if running with profile
     * development or test.
     *
     * @return a RestTemplate that ignores SSL Certificate errors if running with profile development or test.
     */
    public RestTemplate getRestTemplate() {
        HttpClientBuilder b = HttpClientBuilder.create();

//        if (env.activeProfiles.contains("development") || env.activeProfiles.contains("test")) {
//            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//                boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//                    return true
//                }
//
//                @Override
//                boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                    return true
//                }
//            }).build()
//            b.setSslcontext(sslContext)
//
//            // or SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
//            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
//
//            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier)
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
//                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                    .register("https", sslSocketFactory)
//                    .build()
//
//            // allows multi-threaded use
//            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry)
//            b.setConnectionManager(connMgr)
//        }
        HttpClient client = b.build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);
        RestTemplate template = new RestTemplate(factory);
        return template;
    }

}
