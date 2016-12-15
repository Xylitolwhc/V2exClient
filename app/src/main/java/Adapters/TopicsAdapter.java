package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Items.Topics;
import whc.uniquestudio.v2exclient.TopicDetailActivity;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/11/25.
 */

public class TopicsAdapter extends RecyclerView.Adapter<TopicsViewHolder> {
    private List<Topics> topicsFromJsonList;
    private Context context;

    public TopicsAdapter(Context context, List<Topics> topicsFromJsonList) {
        this.context = context;
        this.topicsFromJsonList = topicsFromJsonList;
    }


    @Override
    public void onBindViewHolder(TopicsViewHolder holder, final int position) {
        final Topics topicsFromJson = topicsFromJsonList.get(position);
        holder.theHottestTitle.setText(topicsFromJson.getTitle());
        holder.theHottestContent.setText(topicsFromJson.getContent());
        holder.theId.setText(topicsFromJson.member.getUsername());
        holder.theReplies.setText("Replies:" + topicsFromJson.getReplies());
        holder.theIdImage.setImageBitmap(topicsFromJson.getAvatar_mini());

        holder.theIdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Image:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TopicDetailActivity.class);
                intent.putExtra("url", topicsFromJson.getUrl());
                context.startActivity(intent);
            }
        });
    }

    private String toHTTPs(String url){
        return "https"+url.substring(4);
    }
    @Override
    public TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TopicsViewHolder holder = new TopicsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return topicsFromJsonList.size();
    }

}

class TopicsViewHolder extends RecyclerView.ViewHolder {
    TextView theHottestTitle, theHottestContent, theId, theReplies;
    ImageView theIdImage;
    LinearLayout linearLayoutItem;

    public TopicsViewHolder(View itemView) {
        super(itemView);
        theHottestTitle = (TextView) itemView.findViewById(R.id.theHottestTitle);
        theHottestContent = (TextView) itemView.findViewById(R.id.theHottestContent);
        theId = (TextView) itemView.findViewById(R.id.theId);
        theReplies = (TextView) itemView.findViewById(R.id.theReplies);
        theIdImage = (ImageView) itemView.findViewById(R.id.theIdImage);
        linearLayoutItem=(LinearLayout)itemView.findViewById(R.id.linearLayoutItem);
    }
}