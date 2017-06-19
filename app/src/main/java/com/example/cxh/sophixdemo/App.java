package com.example.cxh.sophixdemo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Sophix （阿里）和 Tinker（微信） 都支持 类、res、so库的修复，都不支持配置清单、四大组件的修改，都支持多渠道、加固的修复，都支持全版本
 * S 需要主动查询补丁
 * T 补丁自动下发
 * @author Hai (haigod7[at]gmail[dot]com)
 *         2017/6/16
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initHotfix();
    }

    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

        Log.e("Version:", appVersion); // 这里的版本号要已控制台的版本号一致

        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
//                .setAesKey("24408057")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {

                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Toast.makeText(getApplicationContext(), "补丁加载成功", Toast.LENGTH_SHORT).show();

                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            Toast.makeText(getApplicationContext(), "新补丁生效需要重启哦", Toast.LENGTH_SHORT).show();
                            SophixManager.getInstance().killProcessSafely();

                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            SophixManager.getInstance().cleanPatches();
                            Toast.makeText(getApplicationContext(), " 内部引擎异常，正在清空本地补丁", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }

                    }
                }).initialize();

        SophixManager.getInstance().queryAndLoadNewPatch();

    }
}
