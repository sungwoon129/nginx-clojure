package com.drminside;

import nginx.clojure.java.ArrayMap;
import nginx.clojure.java.NginxJavaRingHandler;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpHandler implements NginxJavaRingHandler {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {


        String url = "https://www.google.com" + request.get("uri") +  (request.get("query-string") != null ? "?" + request.get("query-string") : "");
        HttpGet httpGet = new HttpGet(url);

        // request에서 헤더 값만 가져와서 설정
        @SuppressWarnings("unchecked")
        Map<String, String> requestHeaders = (Map<String, String>) request.get("headers");
        if (requestHeaders != null) {
            requestHeaders.forEach((key, value) -> {
                if (!key.equalsIgnoreCase("host") && !key.equalsIgnoreCase("accept-encoding")) {
                    httpGet.setHeader(key, value);
                }
            });
        }

        // 요청 보내기
        HttpResponse proxyResponse = httpClient.execute(httpGet);
        HttpEntity entity = proxyResponse.getEntity();

        Header[] responseHeaders = proxyResponse.getAllHeaders();
        Map<String, String> headerMap = new ArrayMap<>();
        for (Header header : responseHeaders) {
            if(!header.getName().equalsIgnoreCase("transfer-Encoding")) {
                headerMap.put(header.getName(), header.getValue());
            }
        }

        // CORS 설정
        headerMap.put("Access-Control-Allow-Origin", "*");

        byte[] responseBody = null;
        if (entity != null) {
            responseBody = EntityUtils.toByteArray(entity);
        }

        int statusCode = proxyResponse.getStatusLine().getStatusCode();

        // content type 가져오기
        String contentType = headerMap.get("Content-Type") != null ? headerMap.get("Content-Type") : "";

        // 이미지일 경우 바이너리 데이터로 응답
        if (contentType.startsWith("image/") || contentType.equals("application/octet-stream")) {
            return new Object[]{
                    statusCode,
                    headerMap,
                    responseBody
            };
        } else {
            // 텍스트/html일 경우 문자열로 변환하여 응답
            String content = responseBody == null ? null : new String(responseBody, StandardCharsets.UTF_8);
            if (content != null) {
                content = content.replaceAll("<img class=\"jfN4p\"[^>]*>",
                        "<img id=\"dimg_YZ9pZqjaCIvJ1e8PqdP9wAM_29\" " +
                                "src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAC0AAAA4CAMAAABe34GAAAAAZlBMVEUAAAD////u7u6Dg4OTk5NkZGSsrKzg4OD19fWLi4vZ2dkVFRUyMjL6+vppaWnm5uZXV1d5eXnS0tIqKipeXl47Ozu2trafn5/BwcEiIiIaGhrLy8uZmZlSUlKlpaVvb28LCwtFRUUINVvnAAABgUlEQVRIiZXW63qCMAwG4EDlaDkqiEPB3f9NjtJGixz6Lf/o3ofFpG0g7zB60dysRzqy3Z2ITqAOSEUI6UzOmGpINxovwK5ODK4QXRhMEaLzrVfv6Zg2st7VrcEFpPVvPH/hPT2nnayWd7T4KYd4vWzrLO3rr/+dtkPYRWsdP0ynRcpLXcCVqdKl5tapkOGURGSvEAXxRxdnWsb38xQp63T9t42otfYhrFql9AXDctYnDAdz3rEbqrjqmmCvvpt6bxRrI3R7KMLzmHQI6d7oCtK8q0oEP1lLRAvWL0TzKUKs9W6o3Dnrxm2JLqyvUCr+v+ptBgQNkDbtoR7TuoYEbm+9U8iDmkl85hM31NEqXaOaAp/Q5quQk37AeiD88qGpft5nIrkin++qwg3niPStOUI4MHcs1k+f72/kmrh9ZsPTiaU1Sdy5FPbccfW/XUwp7/hUiOVMW176cqxG+3jzl4Q1Xbkwl1BP4Szkrf/+bLNnsZ80r19hf0fVSSnLk/9+/gNKNAxPI/SsfAAAAABJRU5ErkJggg==\"" +
                                " class=\"YQ4gaf zr758c wA1Bge\" height=\"56\" width=\"45\" alt=\"\" data-csiid=\"13\" data-atf=\"1\">");
            }

            return new Object[]{
                    statusCode,
                    headerMap,
                    content
            };
        }

    }
}

