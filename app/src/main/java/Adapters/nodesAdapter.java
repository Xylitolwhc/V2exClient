package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import Items.Nodes;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/12/11.
 */

public class nodesAdapter extends RecyclerView.Adapter<nodesAdapter.NodesViewHolder> {
    private Context context;
    private List<Nodes> nodesList;
    private SharedPreferences sharedPreferences;

    public nodesAdapter(Context context, List<Nodes> nodesList) {
        this.context = context;
        this.nodesList = nodesList;
        sharedPreferences = context.getSharedPreferences("nodes", Context.MODE_APPEND);
    }

    @Override
    public NodesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NodesViewHolder holder = new NodesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_nodes, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NodesViewHolder holder, int position) {
        final Nodes node = nodesList.get(position);
        holder.nodes_name.setText(node.getName());
        if (node.getHeader() != null) {
            holder.nodes_content_header.setText(node.getHeader());
            if (node.getFooter() != null) {
                holder.nodes_content_footer.setText(Html.fromHtml(node.getFooter()));
            } else {
                holder.nodes_content_footer.setVisibility(View.GONE);
            }
        } else {
            holder.nodes_devider.setVisibility(View.GONE);
            holder.nodes_content_header.setVisibility(View.GONE);
            holder.nodes_content_footer.setVisibility(View.GONE);
        }
        holder.nodes_topic_number.setText(node.getTopics() + "");
        if (sharedPreferences.contains(node.getName())){
            holder.nodes_switch.setChecked(true);
        }else{
            holder.nodes_switch.setChecked(false);
        }
        holder.nodes_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    Log.d("Data", sharedPreferences.getAll().toString());
                    int nodes_number = sharedPreferences.getInt("nodes_number", 1);

                    editor.putString("" + (nodes_number + 1), node.getName());
                    editor.putString(node.getName(), node.getUrl());
                    editor.putInt("nodes_number", nodes_number + 1);
                } else {
                    editor.remove(node.getName());
                    for (int i = 1; i <= sharedPreferences.getInt("nodes_number", 0); i++) {
                        if (sharedPreferences.getString(i + "", " ").equals(node.getName())) {
                            editor.remove("" + i);
                        }
                    }
                    editor.putInt("nodes_number", sharedPreferences.getInt("node_number", 1) - 1);
                }
                editor.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return nodesList.size();
    }

    class NodesViewHolder extends RecyclerView.ViewHolder {
        TextView nodes_name, nodes_topic_number, nodes_content_header, nodes_content_footer;
        Switch nodes_switch;
        View nodes_devider;

        public NodesViewHolder(View itemView) {
            super(itemView);
            nodes_name = (TextView) itemView.findViewById(R.id.nodes_name);
            nodes_topic_number = (TextView) itemView.findViewById(R.id.nodes_topic_number);
            nodes_content_header = (TextView) itemView.findViewById(R.id.nodes_content_header);
            nodes_content_footer = (TextView) itemView.findViewById(R.id.nodes_content_footer);
            nodes_switch = (Switch) itemView.findViewById(R.id.nodes_switch);
            nodes_devider=itemView.findViewById(R.id.nodes_devider);
        }
    }
}