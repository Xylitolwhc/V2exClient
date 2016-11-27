package FromJsoup;

import android.graphics.Bitmap;

/**
 * Created by 吴航辰 on 2016/11/27.
 */

public class ContentDetail {
    private String username = null, replyContent = null, imageUrl = null;
    private Bitmap idImage = null;

    public void setString(String query, String string) {
        switch (query) {
            case "username": {
                username = string;
                break;
            }
            case "replyContent": {
                replyContent = string;
                break;
            }
            case "imageUrl": {
                imageUrl = string;
                break;
            }
        }
    }

    public void setBitmap(Bitmap bitmap) {
        idImage = bitmap;
    }

    public String getString(String query) {
        switch (query) {
            case "username": {
                return username;
            }
            case "replyContent": {
                return replyContent;
            }
            case "imageUrl": {
                return imageUrl;
            }
        }
        return null;
    }

    public Bitmap getIdImage() {
        return idImage;
    }
}
