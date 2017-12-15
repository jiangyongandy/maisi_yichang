package org.cocos2dx.lua;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;


/**
 * Created by deng on 2015/11/23.
 */
public class UiUtils {
    public static Toast mToast;
    /**
     * 屏幕的宽高
     */
    public static int mScreenWidth, mScreenHeight;

    /**
     * 根据图片url获取图片字节
     *
     * @param imageUrl
     * @return
     */
    public static byte[] getByteByUrl(String imageUrl) {
        String url = imageUrl;
        try {
            return getImage(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据url获取图片数组
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection httpURLconnection = (HttpURLConnection) url
                .openConnection();
        httpURLconnection.setRequestMethod("GET");
        httpURLconnection.setReadTimeout(10 * 1000);
        InputStream in = null;
        if (httpURLconnection.getResponseCode() == 200) {
            in = httpURLconnection.getInputStream();
            byte[] result = readStream(in);
            in.close();
            return result;

        }
        return null;
    }

    /**
     * 将输入流转成字节数组
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream in) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 根据宽压缩图片
     *
     * @param bytes
     * @param width
     * @return
     * @throws IOException
     *//*
    public static ImageEntity revitionImageSize(byte[] bytes, int width, int type)
            throws IOException {
        // 4为资讯显示页面的大图
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        // 这个参数表示 新生成的图片为原始图片的几分之一。
        if (type != 4)
            options.inSampleSize = options.outWidth / width;
        int mHeight = options.outHeight * width / options.outWidth;
        options.outHeight = mHeight;
        options.outWidth = width;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                options);
        if (type == 4)
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, mHeight,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        ImageEntity model = new ImageEntity(width, mHeight, bitmap);
        return model;
    }*/

    /**
     * 设置hint大小
     *
     * @param size
     * @param v
     * @param res
     */
    public static void setViewHintSize(int size, TextView v, int res) {
        SpannableString ss = new SpannableString(getResources().getString(res));
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
        // 附加属性到文本  
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint  
        v.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获得资源
     */
    public static Resources getResources() {
        return APPAplication.instance.getResources();
    }

    /**
     * 得到字符数组
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }


    /**
     * 获得上下文
     *
     * @return
     */
    public static Context getContext() {
        return APPAplication.instance;
    }


    /**
     * 从dimens中获得尺寸
     *
     * @param homePicHeight
     * @return
     */

    public static int getDimens(int homePicHeight) {
        return (int) getResources().getDimension(homePicHeight);
    }

    /**
     * 从dimens中获得尺寸
     *
     * @param
     * @return
     */

    public static float getDimens(String dimenNmae) {
        return getResources().getDimension(getResources().getIdentifier(dimenNmae, "dimen", getContext().getPackageName()));
    }

    /**
     * 从String 中获得字符
     *
     * @return
     */

    public static String getString(int stringID) {
        return getResources().getString(stringID);
    }

    /**
     * 从String 中获得字符
     *
     * @return
     */

    public static String getString(String strName) {
        return getString(getResources().getIdentifier(strName, "string", getContext().getPackageName()));
    }

    /**
     * findview
     *
     * @param view
     * @param viewName
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewByName(View view, String viewName) {
        int id = getResources().getIdentifier(viewName, "id", getContext().getPackageName());
        T v = (T) view.findViewById(id);
        return v;
    }

    /**
     * findview
     *
     * @param activity
     * @param viewName
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewByName(Activity activity, String viewName) {
        int id = getResources().getIdentifier(viewName, "id", getContext().getPackageName());
        T v = (T) activity.findViewById(id);
        return v;
    }

    /**
     * 根据lauout名字获得id
     *
     * @param layoutName
     * @return
     */
    public static int findLayout(String layoutName) {
        int id = getResources().getIdentifier(layoutName, "layout", getContext().getPackageName());
        return id;
    }

    /**
     * 填充view
     *
     * @param detailScreen
     * @return
     */
    public static View inflate(int detailScreen) {
        return View.inflate(getContext(), detailScreen, null);
    }

    /**
     * 单列toast
     *
     * @param string
     */

    public static void makeText(String string) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), string, Toast.LENGTH_SHORT);
        }
        mToast.setText(string);
        mToast.show();
    }


    /**
     * 通过资源id获得drawable
     *
     * @param rID
     * @return
     */
    public static Drawable getDrawablebyResource(int rID) {
        return getResources().getDrawable(rID);
    }


    public static int getLayoutId(String layoutName) {
        return getResources().getIdentifier(layoutName, "layout", getContext().getPackageName());
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕的高度
     *
     * @return
     */
    public static int getScreenHeidth() {
        return getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获得颜色
     */
    public static int getColor(int rid) {
        return getResources().getColor(rid);
    }

    /**
     * 获得颜色
     */
    public static int getColor(String colorName) {
        return getColor(getResources().getIdentifier(colorName, "color", getContext().getPackageName()));
    }

    /**
     * 移除孩子
     *
     * @param view
     */
    public static void removeChild(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(view);
        }
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }


    /**
     * MD5
     *
     * @param string
     * @return
     * @throws Exception
     */
    public static String MD5encode(String string) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 全屏，并且沉侵式状态栏
     *
     * @param activity
     */
    public static void statuInScreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     */
    public static void configRecycleView(final RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
