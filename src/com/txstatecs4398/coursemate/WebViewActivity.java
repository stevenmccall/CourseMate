/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

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
                if (cmsg.message().startsWith("MAGIC")) 
                {
                    passStream(cmsg.message().substring(5));
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

    public void passStream(String HTMLStream) {
        webView.loadUrl("javascript:window.HtmlViewer.showHTML"
                + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

        Logger.getLogger(WebViewActivity2.class.getName()).log(Level.SEVERE, null);
       
        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        intent.putExtra("HTMLStream", HTMLStream);  //used to pass data
        startActivity(intent);
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
