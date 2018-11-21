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

    /* Activity Request Code */
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
        // TODO: load template locking status from storage
        adapter.addItems(
            new Template.TemplateItem(R.mipmap.template_01, "template 01"),
            new Template.TemplateItem(R.mipmap.template_02, "template 02"),
            new Template.TemplateItem(R.mipmap.template_03, "template 03", 3),
            new Template.TemplateItem(R.mipmap.template_04, "template 04", 3),
            new Template.TemplateItem(R.mipmap.template_05, "template 05"),
            new Template.TemplateItem(R.mipmap.template_06, "template 06"),
            new Template.TemplateItem(R.mipmap.template_07, "template 07", 3),
            new Template.TemplateItem(R.mipmap.template_08, "template 08", 3),
            new Template.TemplateItem(R.mipmap.template_09, "template 09", 3),
            new Template.TemplateItem(R.mipmap.template_10, "template 10", 3),
            new Template.TemplateItem(R.mipmap.template_11, "template 11", 3),
            new Template.TemplateItem(R.mipmap.template_12, "template 12", 3),
            new Template.TemplateItem(R.mipmap.template_13, "template 13", 3),
            new Template.TemplateItem(R.mipmap.template_14, "template 14", 3)
        );
        adapter.setOnItemClickListener(new Template.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("template", (int)view.getTag(R.id.tag_resource_id));
                intent.putExtra("locked", (boolean)view.getTag(R.id.tag_locked_status));
                intent.putExtra("span", (int)view.getTag(R.id.tag_span_count));
                setResult(RESULT_OK, intent);
                finishAfterTransition();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
