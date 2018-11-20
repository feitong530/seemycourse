package hk.hku.cs.seemycourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TemplateSelectActivity extends AppCompatActivity {

    public static final int REQUEST_TEMPLATE_SELECTION = 0b1000;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_select);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Template.TemplateAdapter adapter = new Template.TemplateAdapter(this);
        adapter.addItems(
                new Template.TemplateItem(R.mipmap.template_01, "template 01"),
                new Template.TemplateItem( R.mipmap.template_02, "template 02"),
                new Template.TemplateItem(R.mipmap.template_03, "template 03"),
                new Template.TemplateItem(R.mipmap.template_04, "template 04"),
                new Template.TemplateItem(R.mipmap.template_05, "template 05"),
                new Template.TemplateItem(R.mipmap.template_06, "template 06")
        );
        adapter.setOnItemClickListener(new Template.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("template", (int)view.getTag());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
