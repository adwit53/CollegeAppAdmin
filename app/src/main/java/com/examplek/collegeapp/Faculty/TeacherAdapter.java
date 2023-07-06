package com.examplek.collegeapp.Faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CollegeApp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter>   {
        private List<TeacherData> list;
        private Context context;
        private String category;
    public TeacherAdapter(List<TeacherData> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category=category;
    }

    @NonNull
        @Override
        public TeacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.teacher_faculty_item_layout,parent,false);
            return new TeacherViewAdapter(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TeacherViewAdapter holder, int position) {
    TeacherData item=list.get(position);
    holder.name.setText(item.getName());
    holder.email.setText(item.getEmail());
    holder.post.setText(item.getPost());
            try {
                Picasso.get().load(item.getImage()).into(holder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,updateTeacher.class);
                    intent.putExtra("name",item.getName());
                    intent.putExtra("email",item.getEmail());
                    intent.putExtra("post",item.getPost());
                    intent.putExtra("image",item.getImage());
                    intent.putExtra("key",item.getKey());
                    intent.putExtra("category",category);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            try {
                return list.size();
            } catch (Exception e) {
                return 0;
            }
        }

        public class TeacherViewAdapter extends RecyclerView.ViewHolder {
        private TextView name,email,post;
        private Button update;
        private ImageView image;

            public TeacherViewAdapter(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.teacherNames);
                post=itemView.findViewById(R.id.teacherPosts);
                update=itemView.findViewById(R.id.techerUpdate);
                image=itemView.findViewById(R.id.teachersImage);
                email=itemView.findViewById(R.id.teacherEmails);

            }
        }
}
