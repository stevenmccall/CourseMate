/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate;

import android.app.Activity;
import static android.content.ContentValues.TAG;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

public class Take5HttpClient extends Activity {

    private static DefaultHttpClient httpClient = null;
    private static BasicCookieStore cookieStore = null;
    private static BasicHttpContext localContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        final WebView webview = (WebView) findViewById(R.id.webView1);
        webview.getSettings().setJavaScriptEnabled(true);
        initHttpClient();

        WebViewClient webViewClient = new WebViewClient() {
            /**
             * Called on page load
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String htmlContent = getPageM1(url);//getRemoteContent(url);
                syncCookies();//if(htmlContent.equals(null))
                webview.loadData(htmlContent, "text/html", "utf-8");
            }

            /**
             * Called on page load completion
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                // similar code as above method will go here .
            }
        };

        webview.setWebViewClient(webViewClient);
        webview.loadUrl("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin");
    }

    /**
     * this method will hit the remote url and get the content. this content
     * will be set in the webview.
     */
    private String getRemoteContent(String url) {
        HttpGet pageGet = new HttpGet(url);

        ResponseHandler<String> handler = new ResponseHandler<String>() {
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                HttpEntity entity = response.getEntity();
                String html;

                if (entity != null) {
                    html = EntityUtils.toString(entity);
                    return html;
                } else {
                    return null;
                }
            }
        };

        String pageHTML = null;
        try {
            pageHTML = httpClient.execute(pageGet, handler);
            //if you want to manage http sessions then you have to add localContext as a third argument to this method and have uncomment below line to sync cookies.
            //syncCookies();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageHTML;
    }

    private static String getPageM1(String Url) {

        try {
            HttpEntity entity = httpClient.execute(new HttpGet(Url)).getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            return responseString;
        } catch (Exception e) {
            return "getPageM1 Failed ";//+e.toString();
        }
    }

    private static void initHttpClient() {
        httpClient = new DefaultHttpClient();
        ClientConnectionManager cm = httpClient.getConnectionManager();
        SchemeRegistry sr = cm.getSchemeRegistry();

        sr.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        sr.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        cookieStore = new BasicCookieStore();
        localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    private void syncCookies() {
        Cookie sessionInfo;
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        Log.d(TAG, "cookies = " + cookies);

        if (!cookies.isEmpty()) {
            WebView webView = (WebView) findViewById(R.id.webView1);
            CookieSyncManager.createInstance(webView.getContext());
            CookieManager cookieManager = CookieManager.getInstance();

            for (Cookie cookie : cookies) {
                sessionInfo = cookie;
                String cookieString = sessionInfo.getName() + "=" + sessionInfo.getValue() + "; domain=" + sessionInfo.getDomain();
                Log.d(TAG, "cookieString = " + cookieString);
                cookieManager.setCookie("https://ssb.txstate.edu/prod/", cookieString);
                CookieSyncManager.getInstance().sync();
            }
        }
    }
}
