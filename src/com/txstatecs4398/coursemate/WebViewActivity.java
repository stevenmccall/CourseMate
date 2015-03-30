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

public class WebViewActivity extends Activity {

    private WebView webView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        // intercept calls to console.log
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
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
            @Override
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
}
