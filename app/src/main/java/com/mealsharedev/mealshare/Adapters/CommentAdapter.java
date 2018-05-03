package com.mealsharedev.mealshare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mealsharedev.mealshare.Models.Comment;
import com.mealsharedev.mealshare.Models.Meal;
import com.mealsharedev.mealshare.R;

import java.util.ArrayList;

/**
 * Created by Laura on 03-05-2018.
 */


public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();}

    @Override
    public Object getItem(int position) {return comments.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater cityWeatherInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = cityWeatherInflator.inflate(R.layout.list_comments_item, null);
        }

        Comment comment = comments.get(position);
        if(comment !=null){
            TextView txtUsername = (TextView) convertView.findViewById(R.id.txtUser);
            txtUsername.setText(comment.username);
            TextView txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            txtComment.setText(comment.comment);
        }
        return convertView;
    }


}

