package com.swordfall.invoking;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author: Yang JianQiu
 * @Date: 2019/4/26 11:34
 * jdk类HttpURLConnection调用第三方http接口
 *
 * 通常分get和post两种方式
 *
 * 参考资料：
 * http://www.cnblogs.com/angusbao/p/7727649.html
 */
public class HttpUrlConnectionToInterface {

    /**
     * 以post或get方式调用对方接口方法，
     * @param pathUrl
     */
    public static void doPostOrGet(String pathUrl, String data){
        OutputStreamWriter out = null;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(pathUrl);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //请求方式
            conn.setRequestMethod("POST");
            //conn.setRequestMethod("GET");

            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            //DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            conn.setDoOutput(true);
            conn.setDoInput(true);

            /**
             * 下面的三句代码，就是调用第三方http接口
             */
            //获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            //发送请求参数即数据
            out.write(data);
            //flush输出流的缓冲
            out.flush();

            /**
             * 下面的代码相当于，获取调用第三方http接口后返回的结果
             */
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null){
                result += str;
            }
            System.out.println(result);
            //关闭流
            is.close();
            //断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null){
                    out.close();
                }
                if (br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        /**
         *手机信息查询接口：http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=手机号
　　　　 *　　　　　　http://api.showji.com/Locating/www.showji.com.aspx?m=手机号&output=json&callback=querycallback
         */
        doPostOrGet("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15658183565", "");
    }
}
