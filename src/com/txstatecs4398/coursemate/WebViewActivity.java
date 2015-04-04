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
    public boolean done = false;
    String username = "username";
    String password = "password";//if password contains \ must replace with 4 \ in a row.
    public boolean doneTrail = false;
    public String tempStream = "";
    private Thread httpThreadGet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            password = extras.getString("password");
            password = password.replace("\\", "\\\\");
        }

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);//potentially useless
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9100 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");

        // intercept calls to console.log
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                if (cmsg.message().startsWith("MAGIC") && done) {
                    passStream(cmsg.message().substring(5));
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && done) {
                    view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
                } else if (newProgress == 100 && !doneTrail) {
                    doneTrail = true;

                    view.loadUrl("javascript: {"
                            + "document.getElementById('UserID').value = '" + username + "';"
                            + "document.getElementById('PIN').value = '" + password + "';"
                            + "var frms = document.getElementsByName('loginform');"
                            + "frms[0].submit(); };");
                } else if (newProgress == 100 && !done) {
                    Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu&msg=WELCOME")) {
                    done = true;
                    url = "https://ssb.txstate.edu/prod/bwskfshd.P_CrseSchd";
                    view.loadUrl(url);
                } else if (url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin") && !doneTrail) {
                    view.loadUrl(url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        /*//if we cared to run the network on a seperate thread.
         httpThreadGet = new Thread() {
         @Override
         public void run() {
         try {
         tempStream = "<FORM ACTION=\"https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin\" METHOD=\"POST\" NAME=\"loginform\" AUTOCOMPLETE=\"ON\">\n"
         + "<TABLE  CLASS=\"dataentrytable\" SUMMARY=\"This data entry table is used to format the user login fields\">\n"
         + "<TR>\n"
         + "<TD CLASS=\"delabel\" scope=\"row\" ><LABEL for=UserID><SPAN class=\"fieldlabeltext\">NetID:</SPAN></LABEL></TD>\n"
         + "<TD CLASS=\"dedefault\"><INPUT TYPE=\"text\" NAME=\"sid\" SIZE=\"34\" MAXLENGTH=\"32\" ID=\"UserID\" >&nbsp; &nbsp; (<a href=\"http://www.tr.txstate.edu/itac/netid.html\" target=\"_blank\" tabindex=\"-1\">What is a NetID?</a>) </TD><TD></TD>\n"
         + "</TR>\n"
         + "<TR>\n"
         + "<TD CLASS=\"delabel\" scope=\"row\" ><LABEL for=PIN><SPAN class=\"fieldlabeltext\">Password:</SPAN></LABEL></TD>\n"
         + "<TD CLASS=\"dedefault\" ><INPUT TYPE=\"password\" NAME=\"PIN\" SIZE=\"64\" MAXLENGTH=\"63\" ID=\"PIN\"></TD>\n"
         + "</TR>\n"
         + "</TABLE>\n"
         + "<P>\n"
         + "<INPUT TYPE=\"submit\" VALUE=\"Login\">\n"
         + "&nbsp;\n"
         + "<br><br>\n"
         + "<a href=\"https://tim.txstate.edu/onlinetoolkit/Home/ChallengeResponse.aspx?RequestType=ActivateNetID\" target=\"_blank\">Activate your NetID</a>&nbsp; &nbsp; &nbsp; &nbsp; <a href=\"https://tim.txstate.edu/onlinetoolkit/Home/ChallengeResponse.aspx?RequestType=ForgotPassword\" target=\"_blank\">Forgot Password?</a>\n"
         + "<A HREF=\"/wtlhelp/twbhhelp.htm\" onMouseOver=\"window.status='Click Here for Help With Login ?';  return true\"  onMouseOut=\"window.status='';  return true\" onFocus=\"window.status='';  return true\" onBlur=\"window.status='';  return true\"></A>\n"
         + "</FORM>";

         webView.loadDataWithBaseURL("https://ssb.txstate.edu/prod/", tempStream, "text/html", "utf-8", "https://ssb.txstate.edu/prod/twbkwbis.P_WWWLogin");
         } catch (Exception e) {
         e.printStackTrace();
         }
         }
         };
         httpThreadGet.start();
         */
        tempStream = "<FORM ACTION=\"https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin\" METHOD=\"POST\" NAME=\"loginform\" AUTOCOMPLETE=\"ON\">\n"
                + "<TABLE  CLASS=\"dataentrytable\" SUMMARY=\"This data entry table is used to format the user login fields\">\n"
                + "<TR>\n"
                + "<TD CLASS=\"delabel\" scope=\"row\" ><LABEL for=UserID><SPAN class=\"fieldlabeltext\">NetID:</SPAN></LABEL></TD>\n"
                + "<TD CLASS=\"dedefault\"><INPUT TYPE=\"text\" NAME=\"sid\" SIZE=\"34\" MAXLENGTH=\"32\" ID=\"UserID\" >&nbsp; &nbsp; (<a href=\"http://www.tr.txstate.edu/itac/netid.html\" target=\"_blank\" tabindex=\"-1\">What is a NetID?</a>) </TD><TD></TD>\n"
                + "</TR>\n"
                + "<TR>\n"
                + "<TD CLASS=\"delabel\" scope=\"row\" ><LABEL for=PIN><SPAN class=\"fieldlabeltext\">Password:</SPAN></LABEL></TD>\n"
                + "<TD CLASS=\"dedefault\" ><INPUT TYPE=\"password\" NAME=\"PIN\" SIZE=\"64\" MAXLENGTH=\"63\" ID=\"PIN\"></TD>\n"
                + "</TR>\n"
                + "</TABLE>\n"
                + "<P>\n"
                + "<INPUT TYPE=\"submit\" VALUE=\"Login\">\n"
                + "&nbsp;\n"
                + "<br><br>\n"
                + "<a href=\"https://tim.txstate.edu/onlinetoolkit/Home/ChallengeResponse.aspx?RequestType=ActivateNetID\" target=\"_blank\">Activate your NetID</a>&nbsp; &nbsp; &nbsp; &nbsp; <a href=\"https://tim.txstate.edu/onlinetoolkit/Home/ChallengeResponse.aspx?RequestType=ForgotPassword\" target=\"_blank\">Forgot Password?</a>\n"
                + "<A HREF=\"/wtlhelp/twbhhelp.htm\" onMouseOver=\"window.status='Click Here for Help With Login ?';  return true\"  onMouseOut=\"window.status='';  return true\" onFocus=\"window.status='';  return true\" onBlur=\"window.status='';  return true\"></A>\n"
                + "</FORM>";

        webView.loadDataWithBaseURL("https://ssb.txstate.edu/prod/", tempStream, "text/html", "utf-8", "https://ssb.txstate.edu/prod/twbkwbis.P_WWWLogin");
    }

    public void passStream(String HTMLStream) {
        webView.loadUrl("javascript:window.HtmlViewer.showHTML"
                + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

        Logger.getLogger(WebViewActivity.class.getName()).log(Level.SEVERE, null);

        Intent intent = new Intent(WebViewActivity.this, PostLoginMainActivity.class);
        intent.putExtra("HTMLStream", HTMLStream);  //used to pass data
        intent.putExtra("netID", username);  //used to pass data
        startActivity(intent);
    }
}
