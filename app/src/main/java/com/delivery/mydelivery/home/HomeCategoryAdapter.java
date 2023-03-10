package com.delivery.mydelivery.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.store.StoreListActivity;

import java.util.List;

// 카테고리 어댑터
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private List<CategoryVO> categoryList; // 카테고리 리스트
    Context context; // context

    // 카테고리 리스트에 데이터 추가
    public void setCategoryList(List<CategoryVO> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        if (categoryList != null) {
            return categoryList.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_category, parent, false);
        context = view.getContext();

        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIV;
        TextView categoryTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // xml변수 초기화
            categoryIV = itemView.findViewById(R.id.categoryIV);
            categoryTV = itemView.findViewById(R.id.categoryTV);

            // 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        String category = categoryList.get(position).getCategoryName();
                        changeView(category);
                    }
                }
            });
        }

        // 카테고리 이미지, 이름 지정
        void onBind(CategoryVO category) {
            Glide.with(itemView).load("https://picsum.photos/200").placeholder(R.drawable.ic_launcher_background).into(categoryIV);
            categoryTV.setText(category.getCategoryName());
        }
    }

    // 매장 리스트 이동 메소드
    private void changeView(String category) {
        Intent intent = new Intent(context, StoreListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("category", category);
        context.startActivity(intent);
    }

}
