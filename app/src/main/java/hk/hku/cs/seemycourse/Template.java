package hk.hku.cs.seemycourse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Template {
    public static class TemplateItem {
        private int imageId;
        private String intro;

        public TemplateItem(int imageId, String intro) {
            this.imageId = imageId;
            this.intro = intro;
        }

        public int getImageId() {
            return imageId;
        }

        public String getIntro() {
            return intro;
        }
    }


    public static class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.TemplateItemView> {
        private ArrayList<TemplateItem> list;
        private Context ctx;
        private OnItemClickListener listener;

        public TemplateAdapter(Context ctx) {
            this.ctx = ctx;
            list = new ArrayList<>();
        }

        public void addItems(TemplateItem... items) {
            list.addAll(Arrays.asList(items));
        }

        public void setOnItemClickListener(OnItemClickListener itemClickListener) {
            listener = itemClickListener;
        }

        @NonNull
        @Override
        public TemplateItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TemplateItemView(
                    LayoutInflater.from(ctx)
                            .inflate(R.layout.item_template, viewGroup, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull final TemplateItemView templateItemView, int i) {
            // Set Tag in order to get index when click
            templateItemView.itemView.setTag(list.get(i).getImageId());

            // Bind View
            templateItemView.iv.setImageResource(list.get(i).getImageId());
            templateItemView.tv.setText(list.get(i).getIntro());
            templateItemView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = templateItemView.getLayoutPosition();
                    if (listener != null) {
                        listener.OnItemClick(templateItemView.itemView, pos);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        /**
         * View Holder of Template RecycleView
         */
        class TemplateItemView extends RecyclerView.ViewHolder {
            @BindView(R.id.tv)
            TextView tv;
            @BindView(R.id.iv)
            ImageView iv;

            public TemplateItemView(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }
}
