package Net;

import android.graphics.drawable.Drawable;
import android.text.Html;


import java.net.URL;

/**
 * Created by 吴航辰 on 2016/12/5.
 */

public class imgGetter implements Html.ImageGetter {
    @Override
    public Drawable getDrawable(final String source) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(source).openStream(), "");//加载网络图片资源核心语句
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }
}
