package com.txstatecs4398.coursemate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebViewActivity extends Activity {

    private WebView webView;
    public boolean done = false;
    String username = "username";
    String password = "password";
    public boolean doneTrail = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
        
        setContentView(R.layout.webview);
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9100 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");

        // intercept calls to console.log
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                if (cmsg.message().startsWith("MAGIC") && done) {
                    passStream(cmsg.message().substring(5));
                    //return true;
                }
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && done) {
                    view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
                } else if (newProgress == 100 && !doneTrail) {
                    doneTrail = true;
                    view.loadUrl("javascript:document.getElementById('UserID').value = '" + username + "';");
                            /* + "var frms = document.getElementById('PIN').value = '" + password + "';" *///+ "};");
                            //+ "var frms = document.getElementsByName('loginform');"
                            //+ "frms[0].submit(); };");
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //if(done)doneTrail = true;
                if (url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_GenMenu?name=bmenu.P_MainMnu&msg=WELCOME")) {
                    done = true;
                    url = "https://ssb.txstate.edu/prod/bwskfshd.P_CrseSchd";
                    view.loadUrl(url);
                } 
                else if(url.contains("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin") && !doneTrail){
                    view.loadUrl(url);
                }
                else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl("https://ssb.txstate.edu/prod/twbkwbis.P_ValLogin");
    }

    public void passStream(String HTMLStream) {
        webView.loadUrl("javascript:window.HtmlViewer.showHTML"
                + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

        Logger.getLogger(WebViewActivity.class.getName()).log(Level.SEVERE, null);

        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        intent.putExtra("HTMLStream", HTMLStream);  //used to pass data
        startActivity(intent);
    }
    
    public void parse(String strbuf){
		
		int lastIndex = 0, lastIndex2 = 0;
		ArrayList<String> classSched = new ArrayList<String>();
		ArrayList<String> schedStep = new ArrayList<String>();
		ArrayList<String> dayStep = new ArrayList<String>();
		ArrayList<Integer> startStep = new ArrayList<Integer>();
		ArrayList<Integer> finishStep = new ArrayList<Integer>();
		String findstr = "pm";
		String start= null, finish = null, day = null;
		int startime, endtime;
                int pmcount = 0, colcount = 0;
		//static time tt;
	

		//
		//search string for times... which are when in class
		//am</TD> pm</TD>
		
		int foundIndex = 0;
		String step = null;
		foundIndex = strbuf.indexOf("This layout table", foundIndex);
		if(foundIndex != -1){
			step = strbuf.substring(foundIndex);
		}
		while(lastIndex != -1 || lastIndex2 != -1){
			lastIndex = step.indexOf("<TR>", lastIndex);
			lastIndex2 = step.indexOf("</TR>", lastIndex2);
			if (lastIndex != -1 && lastIndex2 != -1){
				String times = step.substring(lastIndex, lastIndex2+ 5);
				classSched.add(times);
				//System.out.println(times);
				lastIndex2 +=5;
				lastIndex = lastIndex2;
			}
		}
		
		int count = 0;
		foundIndex = 0;
		String timestep = null;
		
		for(String s: classSched){
			lastIndex = 0;
			lastIndex2 = 0; 
			count = 0;
			day = null;
			while(lastIndex != -1 || lastIndex2 != -1){
				lastIndex = s.indexOf("<TD", lastIndex);
				lastIndex2 = s.indexOf("</TD>", lastIndex2);
				if (lastIndex != -1 && lastIndex2 != -1){
					count ++;
					timestep = s.substring(lastIndex, lastIndex2+ 5);
					foundIndex = timestep.indexOf("prod/bwsk", foundIndex);
					if(foundIndex != -1){
						schedStep.add(timestep);
						if(count == 1){
							if(day == null){
								day = "M";
							}
						}else if(count == 2){
							if(day == null){
								day = "T";
							}
						}else if(count == 3){
							if(day == null){
								day = "W";
							}else{
								day += "W";
							}
						}else if(count == 4){
							if(day == null){
								day = "R";
							}else{
								day += "R";
							}
						}else if(count == 5){
							if(day == null){
								day = "F";
							}else{
								day += "F";
							}
						}
					}
					lastIndex2 += 5;
					lastIndex = lastIndex2;
				}
			}
			if(day != null){
				dayStep.add(day);
			}
		}
		int i = 1;
		for(String st: schedStep){
			
			lastIndex = 0;
			pmcount = 0;
			while(lastIndex != -1){
				lastIndex = st.indexOf(findstr, lastIndex);
				if(lastIndex != -1){
					pmcount ++;
					lastIndex += findstr.length();
				}
			}
			
			colcount = 0;
			lastIndex = 0;
			boolean found = false;
			
			while(lastIndex != -1){
				lastIndex = st.indexOf(":", lastIndex);
				
				if(lastIndex != -1){
					colcount++;
					if(colcount == 1){
						start = st.substring(lastIndex - 2, lastIndex + 3);
						start = start.replaceAll("[^0-9]+", "");
						startime = Integer.parseInt(start);
						if(pmcount == 2){
							if(startime > 100 && startime < 1200){
								startime += 1200;
							}
						}
						
						for(int ii: startStep){
							if(ii == startime){
								found = true;
							}
						}
						if(found == false){
							startStep.add(startime);
						}
						lastIndex ++;
					}else if (colcount == 2){
						finish = st.substring(lastIndex-2, lastIndex + 3);
						finish = finish.replaceAll("[^0-9]+", "");
						endtime = Integer.parseInt(finish);
						if(pmcount > 0){
							if(endtime > 100 && endtime < 1200){
								endtime += 1200;
							}
						}
						
						for(Integer ii: finishStep){
							if(ii == endtime){
								found = true;
							}
						}
						if(found == false){
							finishStep.add(endtime);
						}
						lastIndex ++;
						break;
					}
				}
				i++;
			}
		}
			
		for(i = 1; i < dayStep.size(); i++){
			System.out.println(dayStep.get(i));
			System.out.println(startStep.get(i-1));
			System.out.println(finishStep.get(i-1));	
			//tt = new time(datStep.get(i), startStep.get(i-1), finishStep.get(i-1), null);
			//addtime(tt);
		}
		
	}	
}
