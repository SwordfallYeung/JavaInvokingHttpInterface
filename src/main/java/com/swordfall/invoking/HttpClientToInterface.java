package com.swordfall.invoking;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Yang JianQiu
 * @Date: 2019/4/28 11:07
 *
 * HttpClient模拟get、post请求并发送请求参数（json等）
 * 【参考资料】
 * https://javasam.iteye.com/blog/2117845
 * https://blog.csdn.net/qq_28379809/article/details/82898792
 */
public class HttpClientToInterface {

    /**
     * httpClient的get请求方式
     * 使用GetMethod来访问一个URL对应的网页实现步骤：
     * 1.生成一个HttpClient对象并设置相应的参数；
     * 2.生成一个GetMethod对象并设置响应的参数；
     * 3.用HttpClient生成的对象来执行GetMethod生成的Get方法；
     * 4.处理响应状态码；
     * 5.若响应正常，处理HTTP响应内容；
     * 6.释放连接。
     * @param url
     * @param charset
     * @return
     */
    public static String doGet(String url, String charset){
        /**
         * 1.生成HttpClient对象并设置参数
         */
        HttpClient httpClient = new HttpClient();
        //设置Http连接超时为5秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        /**
         * 2.生成GetMethod对象并设置参数
         */
        GetMethod getMethod = new GetMethod(url);
        //设置get请求超时为5秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        String response = "";

        /**
         * 3.执行HTTP GET 请求
         */
        try {
            int statusCode = httpClient.executeMethod(getMethod);

            /**
             * 4.判断访问的状态码
             */
            if (statusCode != HttpStatus.SC_OK){
                System.err.println("请求出错：" + getMethod.getStatusLine());
            }

            /**
             * 5.处理HTTP响应内容
             */
            //HTTP响应头部信息，这里简单打印
            Header[] headers = getMethod.getResponseHeaders();
            for (Header h: headers){
                System.out.println(h.getName() + "---------------" + h.getValue());
            }
            //读取HTTP响应内容，这里简单打印网页内容
            //读取为字节数组
            byte[] responseBody = getMethod.getResponseBody();
            response = new String(responseBody, charset);
            System.out.println("-----------response:" + response);
            //读取为InputStream，在网页内容数据量大时候推荐使用
            //InputStream response = getMethod.getResponseBodyAsStream();

        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e){
            //发生网络异常
            System.out.println("发生网络异常!");
        }finally {
            /**
             * 6.释放连接
             */
            getMethod.releaseConnection();
        }
        return response;
    }

    /**
     * post请求
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, JSONObject json){
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        postMethod.addRequestHeader("accept", "*/*");
        postMethod.addRequestHeader("connection", "Keep-Alive");
        //设置json格式传送
        postMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
        //必须设置下面这个Header
        postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
        //添加请求参数
        postMethod.addParameter("commentId", json.getString("commentId"));

        String res = "";
        try {
            int code = httpClient.executeMethod(postMethod);
            if (code == 200){
                res = postMethod.getResponseBodyAsString();
                System.out.println(res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        doGet("http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13026194071", "UTF-8");
        System.out.println("-----------分割线------------");
        System.out.println("-----------分割线------------");
        System.out.println("-----------分割线------------");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commentId", "13026194071");
        doPost("http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13026194071", jsonObject);
    }
}
