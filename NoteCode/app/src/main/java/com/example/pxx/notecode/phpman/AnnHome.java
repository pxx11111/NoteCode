package com.example.pxx.notecode.phpman;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.pxx.notecode.R;
import com.example.pxx.notecode.qrcode.QRcodeIndexActivity;

import java.util.List;



public class AnnHome extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann_home);

//        Toast.makeText(getApplicationContext(),"非常感谢您的下载与安装",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"初始版本，做的粗糙",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"本程序不耗费CPU和内存，暂时没有需求敬请保留",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"极客契约网站上线之后，APP正式版将完爆所有安卓编程学习工具",Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),"本屌正在玩命开发中，让我们一起期待吧",Toast.LENGTH_LONG).show();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //工具栏点击定义
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//            }
//        });
        final WebView webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
//        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
//        toolbar1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    webView.getTop();
//            }
//        });
    }
public void refesh(){
    onCreate(null);
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ann_home, menu);
        return true;
    }
//安浪检测安卓程序包安装了没有的方法
private boolean isAvilible( Context context, String packageName )
{
    final PackageManager packageManager = context.getPackageManager();
    // 获取所有已安装程序的包信息
    List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
    for ( int i = 0; i < pinfo.size(); i++ )
    {
        if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
            return true;
    }
    return false;
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent setIntent = new Intent(AnnHome.this,SettingsActivity.class);
            startActivity(setIntent);
            return true;
        }
        if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(AnnHome.this,AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        if (id == R.id.action_website) {
            Uri anUri = Uri.parse("http://www.anline.cn");
            startActivity(new Intent(Intent.ACTION_VIEW,anUri));
           return true;
        }
        if (id == R.id.action_scanQRcode) {
//                扫一扫
//            Toast.makeText(getApplicationContext(),"网站暂未上线，扫码链接暂时无法处理",Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"网站上线，将支持客户端扫描网站二维码",Toast.LENGTH_SHORT).show();
            Intent goQRcode = new Intent(AnnHome.this, QRcodeIndexActivity.class);
            startActivity(goQRcode);
            return true;
        }
        if (id == R.id.action_compile) {
//                编译器
//            Toast.makeText(getApplicationContext(),"编译器将在下一个版本中推出",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"正在启动PHP编译器",Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"极客契约网站上线之后，将推出完美阅读版本",Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"这将需要很长的一段开发时间",Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"此版本下载量越大，新版本我们将有更大的信心和热情，所有请多多支持！！！",Toast.LENGTH_SHORT).show();
            Intent goPHPcompile = new Intent(AnnHome.this,PHPcompileActivity.class);
            startActivity(goPHPcompile);
            return true;
        }
        if (id == R.id.action_taobao) {
//                打开安浪创想淘宝店

        Uri tbUri = Uri.parse("taobao://b.mashort.cn/h.QjVPt?cv=AACqFyoU&sm=f109bb");
        Uri tbUri2 = Uri.parse("http://b.mashort.cn/h.QjVPt?cv=AACqFyoU&sm=f109bb");
//            检测淘宝客户端是否安装 安装则启动淘宝客户端
            if(isAvilible(AnnHome.this, "com.taobao.taobao")) {
                startActivity(new Intent(Intent.ACTION_VIEW, tbUri));
            }else {
//                未安装则打开本地浏览器
                startActivity(new Intent(Intent.ACTION_VIEW, tbUri2));
            }
            return true;
        }
        /*
        if (id == R.id.action_wechat) {
//            Uri wxUri = Uri.parse("wechat://http://weixin.qq.com/r/gUwXDwzEdFELrUhs9xmT");
            Uri wxUri = Uri.parse("wechat://contacts/profile/gh_9541b7a3ebfa");

//            startActivity(new Intent(Intent.ACTION_VIEW, wxUri));
            Intent intent = new Intent(Intent.ACTION_VIEW, wxUri);
            ComponentName cmp = new ComponentName(" com.tencent.mm ","com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);


//打开安浪创想微信
//            "http://qm.qq.com/cgi-bin/qm/qr?k=Xg6LMa-Wnw_Iu1rSQmBhNVu4rlMdHA8W"
            return true;
        }
        */
        if (id == R.id.action_exit) {
            Toast.makeText(getApplicationContext(),"程序正在退出",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"清除此次占用内存....",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"记得经常来学习哦~_~",Toast.LENGTH_SHORT).show();
//            try
//            {
//                Thread.sleep(5000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
//                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
