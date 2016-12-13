package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.markdown4j.Markdown4jProcessor;

import java.util.List;

import Items.ContentDetail;
import whc.uniquestudio.v2exclient.R;

import static Adapters.ContentDetailAdapter.TYPE_HEADER;
import static Adapters.ContentDetailAdapter.TYPE_NORMAL;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class ContentDetailAdapter extends RecyclerView.Adapter<ContentDetailViewHolder> {
    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_NORMAL = 1;
    protected static final int TYPE_EMPTY = 2;
    protected static final int TYPE_END = 3;
    private Context context;
    private List<ContentDetail> contentDetailList;


    public ContentDetailAdapter(Context context, List<ContentDetail> contentDetailList) {
        this.context = context;
        this.contentDetailList = contentDetailList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (contentDetailList.size() != 1 && position < contentDetailList.size()) {
            return TYPE_NORMAL;
        } else if (contentDetailList.size() == 1) {
            return TYPE_EMPTY;
        } else {
            return TYPE_END;
        }
    }

    @Override
    public ContentDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_HEADER:
                return new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.item_header, parent, false), TYPE_HEADER);
            case TYPE_NORMAL:
                return new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contentdetail, parent, false), TYPE_NORMAL);
            case TYPE_EMPTY:
                return new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.item_noreply, parent, false), TYPE_EMPTY);
            case TYPE_END:
                return new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.item_end, parent, false), TYPE_END);
            default:
                return new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.item_end, parent, false), TYPE_END);
        }
    }

    @Override
    public void onBindViewHolder(ContentDetailViewHolder holder, int position) {
        if (contentDetailList.size() != 0) {
            switch (holder.TYPE) {
                case TYPE_HEADER: {
                    ContentDetail contentDetail = contentDetailList.get(position);
                    holder.contentDetailTitle.setText(contentDetail.getTitle());
                    holder.contentDetailTitle.getPaint().setFakeBoldText(true);
                    if (contentDetail.getDetail()==null) {
                        holder.contentDetailDetail.setVisibility(View.GONE);
                    } else {
                        holder.contentDetailDetail.setText(contentDetail.getDetail());
                    }
                }
                case TYPE_NORMAL: {
                    ContentDetail contentDetail = contentDetailList.get(position);
                    holder.contentDetailContent.setText(contentDetail.getReplyContent());
                    holder.contentDetailContent.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.contentDetailUsername.setText(contentDetail.getUsername());
                    holder.contentDetailUsername.getPaint().setFakeBoldText(true);
                    if (position > 0) {
                        holder.contentDetailFloor.setText(contentDetail.getFloor() + "楼");
                    }
                    if (contentDetail.getIdImage() != null) {
                        holder.contentDetailIdImage.setImageBitmap(contentDetail.getIdImage());
                    } else {
                        holder.contentDetailIdImage.setImageResource(R.mipmap.v2exlogo);
                    }
                    holder.contentDetailDetail.setText(contentDetail.getDetail());
                    break;
                }
                case TYPE_EMPTY: {
                    break;
                }
                case TYPE_END: {
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return contentDetailList.size() + 1;
    }
}

class ContentDetailViewHolder extends RecyclerView.ViewHolder {

    protected TextView contentDetailUsername, contentDetailDetail, contentDetailContent, contentDetailTitle, contentDetailFloor;
    protected ImageView contentDetailIdImage;
    protected int TYPE;

    public ContentDetailViewHolder(View itemView, int viewType) {
        super(itemView);
        TYPE = viewType;
        if (viewType == TYPE_HEADER) {
            contentDetailTitle = (TextView) itemView.findViewById(R.id.contentDetailTitleHead);
            contentDetailUsername = (TextView) itemView.findViewById(R.id.contentDetailUsernameHead);
            contentDetailDetail = (TextView) itemView.findViewById(R.id.contentDetailDetailHead);
            contentDetailContent = (TextView) itemView.findViewById(R.id.contentDetailContentHead);
            contentDetailIdImage = (ImageView) itemView.findViewById(R.id.contentDetailIdImageHead);
        } else if (viewType == TYPE_NORMAL) {
            contentDetailUsername = (TextView) itemView.findViewById(R.id.contentDetailUsername);
            contentDetailDetail = (TextView) itemView.findViewById(R.id.contentDetailDetail);
            contentDetailContent = (TextView) itemView.findViewById(R.id.contentDetailContent);
            contentDetailIdImage = (ImageView) itemView.findViewById(R.id.contentDetailIdImage);
            contentDetailFloor = (TextView) itemView.findViewById(R.id.contentDetailFloor);
        }
    }
}
