package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Items.TopicsFromJsoup;
import whc.uniquestudio.v2exclient.ContentDetailActivity;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/11/25.
 */

public class JsoupItemAdapter extends RecyclerView.Adapter<MyJsoupViewHolder> {
    private List<TopicsFromJsoup> topicsFromJsoupList;
    private Context context;

    public JsoupItemAdapter(Context context, List<TopicsFromJsoup> topicsFromJsoupList) {
        this.context = context;
        this.topicsFromJsoupList = topicsFromJsoupList;
    }


    @Override
    public void onBindViewHolder(MyJsoupViewHolder holder, final int position) {
        final TopicsFromJsoup topicsFromJsoup = topicsFromJsoupList.get(position);
        holder.theJsoupUsername.setText(topicsFromJsoup.getUsername());
        holder.theJsoupTitle.setText(topicsFromJsoup.getTitle());
        holder.theJsoupDetail.setText(topicsFromJsoup.getDetail());
        holder.theJsoupLastReply.setText(topicsFromJsoup.getLastReply());
        holder.theJsoupUserImg.setImageBitmap(topicsFromJsoup.getBitmap());
        holder.theJsoupCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentDetailActivity.class);
                intent.putExtra("url", topicsFromJsoup.getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public MyJsoupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyJsoupViewHolder holder = new MyJsoupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic_jsoup, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return topicsFromJsoupList.size();
    }

}

class MyJsoupViewHolder extends RecyclerView.ViewHolder {
    TextView theJsoupTitle, theJsoupUsername, theJsoupDetail, theJsoupLastReply, theJsoupReplies;
    ImageView theJsoupUserImg;
    CardView theJsoupCardView;

    public MyJsoupViewHolder(View itemView) {
        super(itemView);
        theJsoupTitle = (TextView) itemView.findViewById(R.id.theJsoupTitle);
        theJsoupUsername = (TextView) itemView.findViewById(R.id.theJsoupUsername);
        theJsoupDetail = (TextView) itemView.findViewById(R.id.theJsoupDetail);
        theJsoupLastReply = (TextView) itemView.findViewById(R.id.theJsoupLastReply);
        theJsoupReplies = (TextView) itemView.findViewById(R.id.theJsoupReplies);
        theJsoupUserImg = (ImageView) itemView.findViewById(R.id.theJsoupUserImg);
        theJsoupCardView = (CardView) itemView.findViewById(R.id.theJsoupCardView);
    }
}