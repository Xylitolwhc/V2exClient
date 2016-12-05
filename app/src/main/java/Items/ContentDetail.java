package Items;

import android.graphics.Bitmap;
import android.text.Spanned;

/**
 * Created by 吴航辰 on 2016/11/27.
 */

public class ContentDetail {
    private String username = null, imageUrl = null, detail = null, title = null, replyContentHtml = null;
    private Bitmap idImage = null;
    private Spanned replyContent = null;

    public String getReplyContentHtml() {
        return replyContentHtml;
    }

    public void setReplyContentHtml(String replyContentHtml) {
        this.replyContentHtml = replyContentHtml;
    }

    public Spanned getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(Spanned replyContent) {
        this.replyContent = replyContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
