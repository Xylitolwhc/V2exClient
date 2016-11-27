package Adapters;

import android.content.Context;
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

    private Context context;
    private List<ContentDetail> contentDetailList;

    public ContentDetailAdapter(Context context, List<ContentDetail> contentDetailList) {
        this.context=context;
        this.contentDetailList=contentDetailList;
    }

    @Override
    public ContentDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Adapter",viewType+"");
        ContentDetailViewHolder holder=new ContentDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.detailcardview,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentDetailViewHolder holder, int position) {
        ContentDetail contentDetail=contentDetailList.get(position);
        holder.contentDetailContent.setText(contentDetail.getString("replyContent"));
    }

    @Override
    public int getItemCount() {
        return contentDetailList.size();
    }
}
    class ContentDetailViewHolder extends RecyclerView.ViewHolder{
        protected TextView contentDetailUsername,contentDetailDetail,contentDetailContent;
        protected ImageView contentDetailIdImage;

        public ContentDetailViewHolder(View itemView) {
            super(itemView);
            contentDetailUsername=(TextView)itemView.findViewById(R.id.contentDetailUsername);
            contentDetailDetail=(TextView)itemView.findViewById(R.id.contentDetailDetail);
            contentDetailContent=(TextView)itemView.findViewById(R.id.contentDetailContent);
            contentDetailIdImage=(ImageView)itemView.findViewById(R.id.contentDetailIdImage);
        }
}
