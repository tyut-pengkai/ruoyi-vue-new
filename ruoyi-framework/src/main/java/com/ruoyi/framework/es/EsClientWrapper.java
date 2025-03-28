package com.ruoyi.framework.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

/**
 * @author liangyq
 * @date 2025-03-28 10:36
 */
@Slf4j
public class EsClientWrapper {

    private final RestClient restClient;

    private final ElasticsearchClient esClient;

    public EsClientWrapper(EsConfiguration config) {
        String[] hosts = config.getHosts().split(",");
        String scheme = config.isSsl() ? "https" : "http";
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        int i = 0;
        for (String host : hosts) {
            String[] args = host.split(":");
            httpHosts[i++] = (new HttpHost(args[0], Integer.parseInt(args[1]), scheme));
        }
        RestClientBuilder clientBuilder;
        if (null != config.getUsername() && null != config.getPassword()) {
            clientBuilder = RestClient.builder(httpHosts).setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.setMaxConnTotal(config.getMaxConnTotal());
                httpClientBuilder.setMaxConnPerRoute(config.getMaxConnPerRoute());
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(config.getUsername(), config.getPassword()));
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        } else {
            clientBuilder = RestClient.builder(httpHosts).setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.setMaxConnTotal(config.getMaxConnTotal());
                httpClientBuilder.setMaxConnPerRoute(config.getMaxConnPerRoute());
                return httpClientBuilder;
            });
        }
        restClient = clientBuilder.build();
        // 使用 Jackson 映射器创建传输
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        esClient = new ElasticsearchClient(transport);
    }

    /**
     * 获取es客户端
     *
     * @return
     */
    public ElasticsearchClient getEsClient() {
        return esClient;
    }
}
