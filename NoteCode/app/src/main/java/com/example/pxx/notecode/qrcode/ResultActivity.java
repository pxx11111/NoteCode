package com.example.pxx.notecode.qrcode;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pxx.notecode.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ResultActivity extends Activity {

    private ImageView mResultImage;
    private TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle extras = getIntent().getExtras();



        if (null != extras) {
            int width = extras.getInt("width");
            int height = extras.getInt("height");

            LayoutParams lps = new LayoutParams(width, height);
            lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
            lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());



            String result = extras.getString("result");

            Pattern pattern=Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
            Matcher matcher=pattern.matcher(result);
            if(matcher.find()){
                System.out.print(result + "扫描结果为网址！");
                Toast.makeText(getApplicationContext(),"扫描结果为链接网址："+result,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"正在为您加载："+result,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"加载速度取决于网络访问速度，请您耐心等候",Toast.LENGTH_LONG).show();
//                Uri rsUri = Uri.parse(result);
                WebView webView = (WebView)findViewById(R.id.webView_rsQR);
                webView.loadUrl(result);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                });
            }else{
                Intent rsQrtextShow =new Intent(ResultActivity.this,ResultTextShowActivity.class);
                rsQrtextShow.putExtra("rsText",result);
                Toast.makeText(getApplicationContext(),"扫描结果为文本信息",Toast.LENGTH_SHORT).show();
                startActivity(rsQrtextShow);

            }




        }
    }

    /**
     * Created by Administrator on 2016/2/19.
     */
    public static class ScannerActivity {
    }
}
