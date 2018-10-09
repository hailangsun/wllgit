package com.wll.elasticsearchDemo;


import lombok.Getter;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Description: ES api封装类
 * 因为transportclient 内部链接es使用线程池 此处使用单例
 */
@Repository
public class ESApiClientHelper {
    private Logger logger = LoggerFactory.getLogger(ESApiClientHelper.class);
    private static final String COMMA = ",";
    private static final String COLON = ":";

    @Value("${es.hosts}")
    private String clusterNodes;
    @Value("${es.cluster.name}")
    private String clusterName;
    @Getter
    private TransportClient client;


    @PostConstruct
    public void buildClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName).build();
        client = new PreBuiltTransportClient(settings);
        for (String clusterNode : split(clusterNodes, COMMA)) {
            String hostName = substringBeforeLast(clusterNode, COLON);
            int port = Integer.parseInt(substringAfterLast(clusterNode, COLON));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName),port));
        }

    }

    @PreDestroy
    public void destroy() {
        try {
            logger.info("Closing elasticSearch  client");
            if (client != null) {
                client.close();
            }
        } catch (final Exception e) {
            logger.error("Error closing ElasticSearch client: ", e);
        }
    }

}
