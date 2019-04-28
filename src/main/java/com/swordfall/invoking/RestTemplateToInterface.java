package com.swordfall.invoking;

import com.alibaba.fastjson.JSONObject;
import com.swordfall.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: Yang JianQiu
 * @Date: 2019/4/28 14:13
 *
 * 使用spring boot封装好的api调用第三方http接口
 *
 * RestTemplate定义了36个与REST资源交互的方法，其中的大多数都对应于HTTP的方法。 
 * 其实，这里面只有11个独立的方法，其中有十个有三种重载形式，而第十一个则重载了六次，这样一共形成了36个方法。
 *
 * delete() 在特定的URL上对资源执行HTTP DELETE操作
 * exchange() 在URL上执行特定的HTTP方法，返回包含对象的ResponseEntity，这个对象是从响应体中映射得到的
 * execute() 在URL上执行特定的HTTP方法，返回一个从响应体映射得到的对象
 * getForEntity() 发送一个HTTP GET请求，返回的ResponseEntity包含了响应体所映射成的对象
 * getForObject() 发送一个HTTP GET请求，返回的请求体将映射为一个对象
 * postForEntity() POST 数据到一个URL，返回包含一个对象的ResponseEntity，这个对象是从响应体中映射得到的
 * postForObject() POST 数据到一个URL，返回根据响应体匹配形成的对象
 * headForHeaders() 发送HTTP HEAD请求，返回包含特定资源URL的HTTP头
 * optionsForAllow() 发送HTTP OPTIONS请求，返回对特定URL的Allow头信息
 * postForLocation() POST 数据到一个URL，返回新创建资源的URL
 * put() PUT 资源到特定的URL
 *
 * 【参考资料】
 * https://blog.csdn.net/qq_15452971/article/details/79416469
 * https://blog.csdn.net/weixin_40461281/article/details/83540604
 */
@Service
public class RestTemplateToInterface {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 以get方式请求第三方http接口 getForEntity
     * @param url
     * @return
     */
    public User doGetWith1(String url){
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(url, User.class);
        User user = responseEntity.getBody();
        return user;
    }

    /**
     * 以get方式请求第三方http接口 getForObject
     * 返回值返回的是响应体，省去了我们再去getBody()
     * @param url
     * @return
     */
    public User doGetWith2(String url){
        User user  = restTemplate.getForObject(url, User.class);
        return user;
    }

    /**
     * 以post方式请求第三方http接口 postForEntity
     * @param url
     * @return
     */
    public String doPostWith1(String url){
        User user = new User("小白", 20);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, user, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    /**
     * 以post方式请求第三方http接口 postForEntity
     * @param url
     * @return
     */
    public String doPostWith2(String url){
        User user = new User("小白", 20);
        String body = restTemplate.postForObject(url, user, String.class);
        return body;
    }

    /**
     * exchange
     * @return
     */
    public String doExchange(String url, Integer age, String name){
        //header参数
        HttpHeaders headers = new HttpHeaders();
        String token = "asdfaf2322";
        headers.add("authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        //放入body中的json参数
        JSONObject obj = new JSONObject();
        obj.put("age", age);
        obj.put("name", name);

        //组装
        HttpEntity<JSONObject> request = new HttpEntity<>(obj, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    public static void main(String[] args) {

    }
}
