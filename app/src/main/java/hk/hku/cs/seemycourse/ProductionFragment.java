package hk.hku.cs.seemycourse;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import hk.hku.cs.seemycourse.Template.TemplateItem;
import hk.hku.cs.seemycourse.Template.TemplateAdapter;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TemplateAdapter adapter = new TemplateAdapter(getContext());
        adapter.addItems(
                new Template.TemplateItem(R.mipmap.template_01, "template 01"),
                new Template.TemplateItem(R.mipmap.template_02, "template 02"),
                new Template.TemplateItem(R.mipmap.template_09, "template 09"),
                new Template.TemplateItem(R.mipmap.template_10, "template 10"),
                new Template.TemplateItem(R.mipmap.template_05, "template 05"),
                new Template.TemplateItem(R.mipmap.template_06, "template 06"),
                new Template.TemplateItem(R.mipmap.template_11, "template 11"),
                new Template.TemplateItem(R.mipmap.template_12, "template 12"),
                new Template.TemplateItem(R.mipmap.template_13, "template 13"),
                new Template.TemplateItem(R.mipmap.template_14, "template 14")
        );
        recyclerView.setAdapter(adapter);
    }

}
