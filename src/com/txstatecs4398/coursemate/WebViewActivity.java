/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

public class WebViewActivity extends Activity {

    private WebView webView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        DefaultHttpClient testClient = (DefaultHttpClient) getHttpClient();

        // intercept calls to console.log
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                // check secret prefix
                if (cmsg.message().startsWith("MAGIC")) {
                    String msg = cmsg.message().substring(5); // strip off prefix

                    /* process HTML */
                    return true;
                }

                return false;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String address) {
                // have the page spill its guts, with a secret prefix
                view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
            }
        });

        webView.loadUrl("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin");
    }

    private static String getPageM1() {
        DefaultHttpClient testClient = (DefaultHttpClient) getHttpClient();
        HttpPost post = new HttpPost("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin");

        try {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
            pairs.add(new BasicNameValuePair("sid", "sm1712"));
            pairs.add(new BasicNameValuePair("PIN", "password"));
            post.setEntity(new UrlEncodedFormEntity(pairs));

            HttpEntity entity = testClient.execute(post).getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            return responseString;
        } catch (Exception e) {
            return "getPageM1 Failed: " + e.toString();
        }
    }

    private static HttpClient getHttpClient() {
        DefaultHttpClient ret = null;

        //sets up parameters
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);

        //registers schemes for both http and https
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        registry.register(new Scheme("https", sslSocketFactory, 443));

        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
        ret = new DefaultHttpClient(manager, params);
        return ret;
    }
}
