package Items;

import android.graphics.Bitmap;

/**
 * Created by 吴航辰 on 2016/11/27.
 */

public class ContentDetail {
    private String username = null, replyContent = null, imageUrl = null, detail = null;
    private Bitmap idImage = null;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Bitmap getIdImage() {
        return idImage;
    }

    public void setIdImage(Bitmap idImage) {
        this.idImage = idImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
