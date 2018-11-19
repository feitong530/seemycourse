package hk.hku.cs.seemycourse;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductionFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_production , container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
//                StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TemplateAdapter adapter = new TemplateAdapter(getContext());
        adapter.addItems(
                new TemplateItem(R.mipmap.template_01, "template 01"),
                new TemplateItem(R.mipmap.template_02, "template 02"),
                new TemplateItem(R.mipmap.template_03, "template 03"),
                new TemplateItem(R.mipmap.template_04, "template 04"),
                new TemplateItem(R.mipmap.template_05, "template 05"),
                new TemplateItem(R.mipmap.template_06, "template 06")
        );
        recyclerView.setAdapter(adapter);
    }

    public class TemplateItem {
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

    public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.TemplateItemView> {
        private ArrayList<TemplateItem> list;
        private Context ctx;

        public TemplateAdapter(Context ctx) {
            this.ctx = ctx;
            list = new ArrayList<>();
        }

        public void addItems(TemplateItem... items) {
            list.addAll(Arrays.asList(items));
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
        public void onBindViewHolder(@NonNull TemplateItemView templateItemView, int i) {
            templateItemView.iv.setImageResource(list.get(i).getImageId());
            templateItemView.tv.setText(list.get(i).getIntro());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        /**
         * View Holder of Template RecycleView
         */
        class TemplateItemView extends RecyclerView.ViewHolder {
            @BindView(R.id.tv) TextView tv;
            @BindView(R.id.iv) ImageView iv;

            public TemplateItemView(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

    }

}
