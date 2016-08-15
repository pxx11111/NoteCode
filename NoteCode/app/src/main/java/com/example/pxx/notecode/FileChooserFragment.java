package com.example.pxx.notecode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pxx.notecode.activity.Main4Activity;
import com.example.pxx.notecode.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2016/7/23 21:30
 * Time: 21:30
 * Description:
 */
public class FileChooserFragment extends Fragment {
    private TextView _filePath;
    private List<FileInfo> _files = new ArrayList<FileInfo>();
    private String _rootPath = FileUtil.getSDPath();
    private String _currentPath = _rootPath;
    private BaseAdapter adapter = null;

    private Main4Activity mainActivity;

    public static FileChooserFragment newInstance() {
        FileChooserFragment fragment = new FileChooserFragment();
        Bundle args = new Bundle();
        args.putInt("count", 1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (Main4Activity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_chooser, null);
    }

    ListView lv_files;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _filePath = (TextView) view.findViewById(R.id.file_path);
        lv_files = (ListView) view.findViewById(R.id.lv_files);
        // 绑定数据
        adapter = new FileAdapter(getContext(), _files);
        lv_files.setAdapter(adapter);
        lv_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo f = _files.get(position);
                if (f.IsDirectory) {
                    viewFiles(f.Path);
                } else {
                    openFile(f.Path);
                }
            }
        });
        // 获取当前目录的文件列表
        viewFiles(_currentPath);
    }

    /**
     * 获取该目录下所有文件
     **/
    private void viewFiles(String filePath) {
        ArrayList<FileInfo> tmp = FileActivityHelper.getFiles(getActivity(), filePath);
        if (tmp != null) {
            // 清空数据
            _files.clear();
            _files.addAll(tmp);
            tmp.clear();

            // 设置当前目录
            _currentPath = filePath;
//            _filePath.setText(filePath);

            Toolbar toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
            toolbar.setTitle(filePath);

            // this.onContentChanged();
            adapter.notifyDataSetChanged();
        }else{
            mainActivity.exit();
        }
    }

    /**
     * 打开文件
     **/
    private void openFile(String path) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//
//        File f = new File(path);
//        String type = FileUtil.getMIMEType(f.getName());
//        intent.setDataAndType(Uri.fromFile(f), type);
//        startActivity(intent);
        if (!TextUtils.isEmpty(path)) {
            PreferencesUtils.putString(getContext(), "path", path);
            EventBus.getDefault().post(path);
        } else {
            Toast.makeText(getContext(), "文件为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 重命名回调委托
     **/
    private final Handler renameFileHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                viewFiles(_currentPath);
        }
    };

    /**
     * 创建文件夹回调委托
     **/
    private final Handler createDirHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                viewFiles(_currentPath);
        }
    };

    /**
     * 粘贴文件
     **/
    private void pasteFile(String path, String action) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("CURRENTPASTEFILEPATH", path);
        bundle.putString("ACTION", action);
        intent.putExtras(bundle);
        intent.setClass(getActivity(), PasteFile.class);
        // 打开一个Activity并等待结果
        startActivityForResult(intent, 0);
    }

    public void onBackPress() {
        File f = new File(_currentPath);
        String parentPath = f.getParent();
        if (parentPath != null) {
            viewFiles(parentPath);
        } else
        {
            mainActivity.exit();
        }
    }
}
