package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.theHottestTitle.setText(theHottestList.get(position).getString("title"));
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
    TextView theHottestTitle;
    public MyViewHolder(View itemView) {
        super(itemView);
        theHottestTitle=(TextView) itemView.findViewById(R.id.theHottestTitle);
    }
}