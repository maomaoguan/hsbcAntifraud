package com.hsbc.util;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpUtils {
    private static PoolingHttpClientConnectionManager poolingmgr;
    static {
        init();
    }

    public synchronized static CloseableHttpClient getHttpClient() {
        if (null == poolingmgr) {
            init();
        }
        return HttpClients.custom().setConnectionManager(poolingmgr).build();
    }

    /**
     * 初始化pool
     */
    public static void init() {
        X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(SSLContexts.createDefault(), hostnameVerifier);
        poolingmgr = new PoolingHttpClientConnectionManager(
            RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build());
        // 设置最大连接100
        poolingmgr.setMaxTotal(1000);
        // 设置单个路由最大连接20
        poolingmgr.setDefaultMaxPerRoute(1000);
    }
}
