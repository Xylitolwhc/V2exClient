package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import FromGson.TheHottest;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/11/25.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private List<TheHottest> theHottestList;
    private Context context;

    public MyAdapter(Context context,List<TheHottest> theHottestList) {
        this.context=context;
        this.theHottestList=theHottestList;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TheHottest theHottest=theHottestList.get(position);
        holder.theHottestTitle.setText(theHottest.getString("title"));
        holder.theHottestContent.setText(theHottest.getString("content"));
        holder.theId.setText(theHottest.getString("username"));
        holder.theReplies.setText("Replies:"+theHottest.getInt("replies"));
        holder.theIdImage.setImageBitmap(theHottest.getBitmap("avatar_mini"));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardviewitem, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return theHottestList.size();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView theHottestTitle,theHottestContent,theId,theReplies;
    ImageView theIdImage;
    public MyViewHolder(View itemView) {
        super(itemView);
        theHottestTitle=(TextView) itemView.findViewById(R.id.theHottestTitle);
        theHottestContent=(TextView) itemView.findViewById(R.id.theHottestContent);
        theId=(TextView) itemView.findViewById(R.id.theId);
        theReplies=(TextView) itemView.findViewById(R.id.theReplies);
        theIdImage=(ImageView)itemView.findViewById(R.id.theIdImage);
    }
}