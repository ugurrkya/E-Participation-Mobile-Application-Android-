package com.ep.sb.e_participation;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHoldervote extends RecyclerView.ViewHolder {

    View mView;

    public ViewHoldervote(@NonNull View itemView) {
        super(itemView);

        mView = itemView;


        //item click

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });


        //item long click

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }


    public void setDetails (Context ctx, String date, String userid, String name, String description, String title, long votednumber, long yes, long no, long counter){


        TextView mytitlename = mView.findViewById(R.id.titlename);
        TextView myDate = mView.findViewById(R.id.date);
        TextView myusername = mView.findViewById(R.id.usname);
        TextView myvotedno = mView.findViewById(R.id.votedno);
        TextView myyes = mView.findViewById(R.id.yesnumber);
        TextView myno = mView.findViewById(R.id.nonumber);

        mytitlename.setText(title);
        myDate.setText(date);
        myusername.setText(name);
        myvotedno.setText(String.valueOf((int) votednumber));
        myyes.setText(String.valueOf((int) yes));
        myno.setText(String.valueOf((int) no));

    }

    private ViewHoldervote.ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHoldervote.ClickListener clickListener){

        mClickListener = clickListener;

    }
}



