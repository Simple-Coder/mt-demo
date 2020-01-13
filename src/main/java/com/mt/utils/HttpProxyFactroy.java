package com.mt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: HttpProxyFactroy
 * @Description:
 * @Author: xiedong
 * @Date: 2019/12/10 14:08
 */
public class HttpProxyFactroy {
    private Logger log = LoggerFactory.getLogger(getClass());
    private ConcurrentHashMap<String, URL> urlMap = new ConcurrentHashMap();
    private static volatile HttpProxyFactroy instance = null;

    public static HttpProxyFactroy getInstance() {

        if (instance == null) {
            synchronized (HttpProxyFactroy.class) {
                if (instance == null) {
                    instance = new HttpProxyFactroy();
                }
            }
        }

        return instance;
    }

    //构造方法私有化
    private HttpProxyFactroy() {
    }

    public String sendMsg(String targetUrl, String proxyIP, Integer port, String method, String msg, String encoding, Integer connectTimeout, Integer readTimeout, Map<String, String> reqProp) {
        OutputStream os = null;
        InputStream in = null;
        BufferedReader br = null;
        HttpURLConnection httpURLConnection=null;
        try {
            String temp = null;
            StringBuilder sb = new StringBuilder();
            URL url = new URL(targetUrl);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, port));
            httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            httpURLConnection.setConnectTimeout(connectTimeout);
            httpURLConnection.setReadTimeout(readTimeout);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);

            httpURLConnection.setRequestProperty("Content-type", reqProp.get("Content-type"));
            //
            httpURLConnection.setRequestMethod(method);
            //
            httpURLConnection.connect();
            //
            os = httpURLConnection.getOutputStream();
            os.write(msg.getBytes(encoding));
            os.flush();
//            PrintStream out = new PrintStream(httpURLConnection.getOutputStream(), false, encoding);
//            out.print(msg);
//            out.flush();
//            os.close();
            log.info("数据发送成功,响应码:【{}】", httpURLConnection.getResponseCode());

            if (200 == httpURLConnection.getResponseCode()) {
                in = httpURLConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), encoding));
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                return sb.toString();
            } else {
                log.info("响应码不为成功");
                return null;
            }
        } catch (Exception e) {
            log.info("数据发送异常", e);
            return null;
        } finally {

            try {
                if (br != null) {
                    br.close();
                }
                if (in != null) {
                    in.close();
                }
                if (os != null) {
                    os.close();
                }
                if(httpURLConnection!=null){
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                br=null;
                os=null;
                in=null;
                httpURLConnection=null;
            }
        }
    }

    public String sendMsg(String targetUrl, String proxyIP, Integer port, String method, String msg, String encoding) {
        Assert.notNull(encoding, "encoding is required");
        Assert.notNull(targetUrl, "targetUrl is required");
        Assert.notNull(proxyIP, "proxyIP is required");
        Assert.notNull(method, "method is required");
        HashMap<String, String> reqProp = new HashMap<>();
        reqProp.put("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
        return this.sendMsg(targetUrl, proxyIP, port, method, msg, encoding, 60000, 60000, reqProp);
    }

    public static void main(String[] args) {
        String targetUrl = "http://59.151.64.31:8081";
        String proxyIp = "36.9.99.230";
        String port = "808";
        String msg = "测试数据";
        String encoding = "UTF-8";

        String rspData=null;
        while (rspData==null)
        {
            rspData= HttpProxyFactroy.getInstance().sendMsg(targetUrl, proxyIp, Integer.parseInt(port), "POST", msg, encoding);
        }
        System.out.println("返回内容为：" + rspData);


        //String rspData = SocketFactory.newInstance().sendMsg(proxyIp, Integer.parseInt(port), msg, encoding);
        //System.out.println("返回=--"+rspData);


    }
}
