package Net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;


import java.net.URL;

/**
 * Created by 吴航辰 on 2016/12/5.
 */

public class imgGetter implements Html.ImageGetter {
    private int width;

    public imgGetter(int width) {
        this.width = width;
    }

    @Override
    public Drawable getDrawable(final String source) {
        Drawable drawable = null;
        try {
            Bitmap bitmap = ConnectInternet.getPicture(source);
            drawable = new BitmapDrawable(bitmap);//加载网络图片资源核心语句
            drawable.setBounds(0, 0, width, bitmap.getHeight()*width/bitmap.getWidth());
        } catch (Exception e) {
            Log.d("Source", source);
            e.printStackTrace();
        }
        return drawable;
    }
}
