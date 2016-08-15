package com.example.pxx.notecode;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pxx.notecode.handler.CDocumentHandler;
import com.example.pxx.notecode.handler.CppDocumentHandler;
import com.example.pxx.notecode.handler.CssDocumentHandler;
import com.example.pxx.notecode.handler.DocumentHandler;
import com.example.pxx.notecode.handler.HtmlDocumentHandler;
import com.example.pxx.notecode.handler.JavaDocumentHandler;
import com.example.pxx.notecode.handler.JavascriptDocumentHandler;
import com.example.pxx.notecode.handler.LispDocumentHandler;
import com.example.pxx.notecode.handler.LuaDocumentHandler;
import com.example.pxx.notecode.handler.MlDocumentHandler;
import com.example.pxx.notecode.handler.MxmlDocumentHandler;
import com.example.pxx.notecode.handler.PerlDocumentHandler;
import com.example.pxx.notecode.handler.PythonDocumentHandler;
import com.example.pxx.notecode.handler.RubyDocumentHandler;
import com.example.pxx.notecode.handler.SqlDocumentHandler;
import com.example.pxx.notecode.handler.TextDocumentHandler;
import com.example.pxx.notecode.handler.VbDocumentHandler;
import com.example.pxx.notecode.handler.XmlDocumentHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;



/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2016/7/23 22:11
 * Time: 22:11
 * Description:
 */
public class CodeDetailFragment extends Fragment {

    public static CodeDetailFragment newInstance() {
        CodeDetailFragment fragment = new CodeDetailFragment();
        Bundle args = new Bundle();
        args.putInt("count", 2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView tv_msg;
    private WebView mWebView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebChrome2(getActivity()));
        WebSettings s = mWebView.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        s.setUseWideViewPort(false);
        s.setAllowFileAccess(true);
        s.setBuiltInZoomControls(true);
        s.setLightTouchEnabled(true);
        s.setLoadsImagesAutomatically(true);
//        s.setPluginsEnabled(false);
        s.setSupportZoom(true);
        s.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        s.setSupportMultipleWindows(true);
        s.setJavaScriptEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_code_detail, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private String currentFile;

    @Subscribe
    public void onEvent(String event) {
        tv_msg.setText(event);
        currentFile = event;
        Uri uri = Uri.parse("file://"
                + new File(event).getAbsolutePath());
        loadFile(uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Load the HTML file into the webview by converting it to a data: URL. If
     * there were any relative URLs, then they will fail as the webview does not
     * allow access to the file:/// scheme for accessing the local file system,
     * <p>
     * Note: Before actually loading the info in webview, i add the prettify
     * libraries to do the syntax highlight also i organize the data where it
     * has to be. works fine now but it needs some work
     *
     * @param uri file URI pointing to the content to be loaded
     */
    void loadFile(Uri uri) {
        this.mWebView.freeMemory();

        if (uri.toString().equals(this.currentFile)) {
            return;
        }
        this.currentFile = uri.toString();

        String path = uri.getPath();

        DocumentHandler handler = getHandlerByExtension(path);
        File f = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(f),"utf-8"), 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!f.exists()) {
            Log.e("CodeDetailFragment", "File doesnt exists: " + path);
            return;
        }

        if (handler == null) {
            Log.e("CodeDetailFragment", "Filetype not supported");
            Toast.makeText(getActivity(), "文件类型不支持",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        StringBuilder contentString = new StringBuilder();

//        setTitle("CodePad - " + uri.getLastPathSegment());
        boolean isInstance = handler instanceof TextDocumentHandler;
        if (!isInstance) {
            contentString.append("<html><head><title>" + uri.getLastPathSegment()
                    + "</title>");
            contentString
                    .append("<link href='file:///android_asset/prettify.css' rel='stylesheet' type='text/css'/> ");
            contentString
                    .append("<script src='file:///android_asset/prettify.js' type='text/javascript'></script> ");
            contentString.append(handler.getFileScriptFiles());
            contentString
                    .append("</head><body onload='prettyPrint()'><code class='"
                            + handler.getFilePrettifyClass() + "'>");
            StringBuilder codeString = new StringBuilder();
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!codeString.toString().equals("")) {
                        codeString.append("\r\n");
                    }
                    codeString.append(line);
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
            contentString.append(handler.getFileFormattedString(codeString
                    .toString()));
            contentString
                    .append("</code><br /><br /><br /><br /><br /></body></html> ");
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.loadDataWithBaseURL("file:///android_asset/",
                    contentString.toString(), handler.getFileMimeType(), "", "");
        }else{
            StringBuilder codeString = new StringBuilder();
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!codeString.toString().equals("")) {
                        codeString.append("\r\n");
                    }
                    codeString.append(line);
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
            mWebView.loadData(codeString.toString(),"text/html; charset=UTF-8",null);
        }

        Log.v("CodeDetailFragment", "File Loaded: " + path);
    }


    /**
     * Get a Document Handler Depending on the filename extension
     *
     * @param filename The filename to retrieve the handler from
     * @return The new document handler
     */
    public DocumentHandler getHandlerByExtension(String filename) {
        DocumentHandler handler = null;

        if (filename.endsWith(".java"))
            handler = new JavaDocumentHandler();
        if (filename.endsWith(".cpp") || filename.endsWith(".cc"))
            handler = new CppDocumentHandler();
        if (filename.endsWith(".c"))
            handler = new CDocumentHandler();
        if (filename.endsWith(".html") || filename.endsWith(".htm")
                || filename.endsWith(".xhtml"))
            handler = new HtmlDocumentHandler();
        if (filename.endsWith(".js"))
            handler = new JavascriptDocumentHandler();
        if (filename.endsWith(".mxml"))
            handler = new MxmlDocumentHandler();
        if (filename.endsWith(".pl"))
            handler = new PerlDocumentHandler();
        if (filename.endsWith(".py"))
            handler = new PythonDocumentHandler();
        if (filename.endsWith(".rb"))
            handler = new RubyDocumentHandler();
        if (filename.endsWith(".xml"))
            handler = new XmlDocumentHandler();
        if (filename.endsWith(".css"))
            handler = new CssDocumentHandler();
        if (filename.endsWith(".el") || filename.endsWith(".lisp")
                || filename.endsWith(".scm"))
            handler = new LispDocumentHandler();
        if (filename.endsWith(".lua"))
            handler = new LuaDocumentHandler();
        if (filename.endsWith(".ml"))
            handler = new MlDocumentHandler();
        if (filename.endsWith(".vb") || filename.endsWith(".bas"))
            handler = new VbDocumentHandler();
        if (filename.endsWith(".sql"))
            handler = new SqlDocumentHandler();
        if (filename.endsWith(".txt")||filename.endsWith(".md"))
            handler = new TextDocumentHandler();
//        if (handler ==null){
//            handler = new TextDocumentHandler();
//        }
        Log.v("CodeDetailFragment", " Handler: " + filename);
        return handler;
    }
}
