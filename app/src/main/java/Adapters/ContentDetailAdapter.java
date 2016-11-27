package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import FromJsoup.ContentDetail;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class ContentDetailAdapter extends RecyclerView.Adapter<ContentDetailViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private String title, content, username;
    private Bitmap idImage;
    private Context context;
    private List<ContentDetail> contentDetailList;


    public ContentDetailAdapter(Context context, List<ContentDetail> contentDetailList, String title, String content, String username,Bitmap idImage) {
        this.context = context;
        this.contentDetailList = contentDetailList;
        this.title = title;
        this.content = content;
        this.username = username;
        this.idImage=idImage;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else return TYPE_NORMAL;
    }

    @Override
    public ContentDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Adapter", viewType + "");

        if (viewType == TYPE_HEADER) {
            return new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.headcardview, parent, false), TYPE_HEADER);
        } else {
            return new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.detailcardview, parent, false), TYPE_NORMAL);
        }
    }

    @Override
    public void onBindViewHolder(ContentDetailViewHolder holder, int position) {
        if (position == 0) {
            holder.contentDetailTitle.setText(title);
            holder.contentDetailContent.setText(content);
            holder.contentDetailUsername.setText(username);
            holder.contentDetailIdImage.setImageBitmap(idImage);
        } else {
            ContentDetail contentDetail = contentDetailList.get(position);
            holder.contentDetailContent.setText(contentDetail.getString("replyContent"));
            holder.contentDetailUsername.setText(contentDetail.getString("username"));
            holder.contentDetailIdImage.setImageBitmap(contentDetail.getIdImage());
        }
    }

    @Override
    public int getItemCount() {
        return contentDetailList.size();
    }
}

class ContentDetailViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    protected TextView contentDetailUsername, contentDetailDetail, contentDetailContent, contentDetailTitle;
    protected ImageView contentDetailIdImage;

    public ContentDetailViewHolder(View itemView, int viewType) {
        super(itemView);
        if (viewType == TYPE_HEADER) {
            contentDetailTitle = (TextView) itemView.findViewById(R.id.contentDetailTitleHead);
            contentDetailUsername = (TextView) itemView.findViewById(R.id.contentDetailUsernameHead);
            contentDetailDetail = (TextView) itemView.findViewById(R.id.contentDetailDetailHead);
            contentDetailContent = (TextView) itemView.findViewById(R.id.contentDetailContentHead);
            contentDetailIdImage = (ImageView) itemView.findViewById(R.id.contentDetailIdImageHead);
        } else {
            contentDetailUsername = (TextView) itemView.findViewById(R.id.contentDetailUsername);
            contentDetailDetail = (TextView) itemView.findViewById(R.id.contentDetailDetail);
            contentDetailContent = (TextView) itemView.findViewById(R.id.contentDetailContent);
            contentDetailIdImage = (ImageView) itemView.findViewById(R.id.contentDetailIdImage);
        }
    }
}
