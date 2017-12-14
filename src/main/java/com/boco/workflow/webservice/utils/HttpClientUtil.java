package com.boco.workflow.webservice.utils;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/** 
     * 发送HTTP_POST请求 
     * @see 该方法会自动关闭连接,释放资源 
     * @param requestURL    请求地址(含参数) 
     * @param decodeCharset 解码字符集,解析响应数据时用之,其为null时默认采用UTF-8解码 
     * @return 远程主机响应正文 
     */  
    public static String sendPostRequest(String reqURL,NameValuePair[] nvps,String decodeCharset){  
    	
        String responseContent = null; //响应内容  
        
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
        		.setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build()).build(); //创建默认的httpClient实例  
        CloseableHttpResponse response = null;
        HttpUriRequest httpPost = RequestBuilder.post()  
        		.setUri(reqURL)
                .addParameters(nvps)
                .build();  
        try{  
            response = httpClient.execute(httpPost); //执行POST请求  
            HttpEntity entity = response.getEntity();            //获取响应实体  
            responseContent = EntityUtils.toString(entity, decodeCharset == null ? "UTF-8" : decodeCharset);  

            logger.debug("响应内容: " + responseContent);  
        }catch(Exception e){  
        	
            logger.error("网络异常", e);  
        }finally{  
        	
        	HttpClientUtils.closeQuietly(response); //关闭连接,释放资源  
        }  
        return responseContent;  
    }  
}
