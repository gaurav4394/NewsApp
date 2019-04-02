package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<NewsDataModel> newsModelArrayList;

    private Context context;


    public NewsAdapter(Context ctx, ArrayList<NewsDataModel> newsModelArrayList){
        context=ctx;
        inflater = LayoutInflater.from(ctx);
        this.newsModelArrayList = newsModelArrayList;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.news_list_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.MyViewHolder holder, int position) {

        Picasso.get().load(newsModelArrayList.get(position).getUrlToImage()).into(holder.iv);
        holder.source.setText(newsModelArrayList.get(position).getSource());
        holder.title.setText(newsModelArrayList.get(position).getTitle());
        holder.description.setText(newsModelArrayList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView source, title, description;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            source = (TextView) itemView.findViewById(R.id.sourceName);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            iv = (ImageView) itemView.findViewById(R.id.iv);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        NewsDataModel clickedDataItem = newsModelArrayList.get(pos);
                        //Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getUrl(), Toast.LENGTH_SHORT).show();
                        CheckInternetConnection internetConnection=new CheckInternetConnection(context);

                        if(internetConnection.isOnline()){
                            Intent intent = new Intent(context, DetailNewsWebView.class);
                            intent.putExtra("URL",clickedDataItem.getUrl());
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }

    }
}
