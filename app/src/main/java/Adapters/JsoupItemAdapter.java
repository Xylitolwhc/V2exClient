package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        holder.theId.setText(topicsFromJsoup.getUsername());
        holder.theHottestTitle.setText(topicsFromJsoup.getTitle());
        holder.theHottestContent.setVisibility(View.GONE);
        holder.theId.setText(topicsFromJsoup.getUsername());
        holder.theReplies.setText("Replies:"+topicsFromJsoup.getReplies());
        holder.theIdImage.setImageBitmap(topicsFromJsoup.getBitmap());
        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
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
        MyJsoupViewHolder holder = new MyJsoupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return topicsFromJsoupList.size();
    }

}

class MyJsoupViewHolder extends RecyclerView.ViewHolder {
    TextView theHottestTitle, theHottestContent, theId, theReplies;
    ImageView theIdImage;
    LinearLayout linearLayoutItem;

    public MyJsoupViewHolder(View itemView) {
        super(itemView);
        theHottestTitle = (TextView) itemView.findViewById(R.id.theHottestTitle);
        theHottestContent = (TextView) itemView.findViewById(R.id.theHottestContent);
        theId = (TextView) itemView.findViewById(R.id.theId);
        theReplies = (TextView) itemView.findViewById(R.id.theReplies);
        theIdImage = (ImageView) itemView.findViewById(R.id.theIdImage);
        linearLayoutItem=(LinearLayout)itemView.findViewById(R.id.linearLayoutItem);
    }
}