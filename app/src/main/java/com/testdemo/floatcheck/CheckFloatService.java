package com.testdemo.floatcheck;

import android.app.AppOpsManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liguanyi on 15-11-27.
 *
 */
public class CheckFloatService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lgy", "CheckFloatService onStartCommand");
        int result = checkPermission(24, getApplicationContext());
        if(result == CHECK_OP_SUCCESS){
            Toast.makeText(getApplicationContext(), "方案1 允许使用悬浮窗", Toast.LENGTH_SHORT).show();
        }else if(result == CHECK_OP_FAILED){
            Toast.makeText(getApplicationContext(), "方案2 禁止使用悬浮窗", Toast.LENGTH_SHORT).show();
        }else{
            checkFloatWindowAllowShow(getApplicationContext(), new ICheckResult() {
                @Override
                public void onResult(boolean isAllow) {
                    if (isAllow) {
                        Toast.makeText(getApplicationContext(), "方案2 允许使用悬浮窗", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "方案2 禁止使用悬浮窗", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 检测当前是否可以显示悬浮窗（与是否有悬浮窗权限有一定区别 华为手机当有activity存在时，无论是否有权限都允许弹出悬浮窗。）
     * @param context
     * @param result {@link }
     * @return true 开始检测  false检测失败
     */
    public static boolean checkFloatWindowAllowShow(Context context,final ICheckResult result){
        final WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if(windowManager == null){
            return false;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.width = 0;
        params.height = 0;
        final Handler handler = new Handler();
        final View view = new View(context){

            @Override
            public void onWindowFocusChanged(boolean hasWindowFocus) {
                handler.removeCallbacksAndMessages(null);
                if(getParent() != null) {
                    windowManager.removeView(this);
                }
                //允许弹出悬浮窗
                result.onResult(true);
            }
        };
        windowManager.addView(view, params);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                windowManager.removeView(view);
                handler.removeCallbacksAndMessages(null);
                //禁止弹出悬浮窗
                result.onResult(false);
            }
        }, 100);
        return true;
    }

    /**
     * 用于反馈检查结果
     */
    public interface ICheckResult{
        void onResult(boolean isAllow);
    }


    public static final int CHECK_OP_SUCCESS = 0;
    public static final int CHECK_OP_FAILED = 1;
    public static final int CHECK_OP_ERROR = 2;

    /**
     * 检查权限
     * @param op 权限代码 AppOpsManager.OP_
     * @param context
     * @return
     */
    public static int checkPermission(int op, Context context){
        if(Build.VERSION.SDK_INT > 18) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Method checkOp = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return (int)checkOp.invoke(appOpsManager, 24, Binder.getCallingUid(), context.getPackageName());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return CHECK_OP_ERROR;
    }

}
