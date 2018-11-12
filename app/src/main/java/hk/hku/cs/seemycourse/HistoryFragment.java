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
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryFragment extends Fragment {


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frag_history, container, false);
            initLayout(view);

            return view;
        }

        private void initLayout(View v) {
            TextView tv = v.findViewById(R.id.history_text);
            tv.setText("Write functions of the History Records part here!");

            RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            HistoryAdapter adapter = new HistoryAdapter(getContext());
            adapter.addItems(
                    new HistoryItem("123"),
                    new HistoryItem("456"),
                    new HistoryItem("789"),
                    new HistoryItem("10"),
                    new HistoryItem("13"),
                    new HistoryItem("143")
            );
            recyclerView.setAdapter(adapter);


        }

        public class HistoryItem {
            private String text;

            public HistoryItem(String text) {
                this.text = text;
            }

            public String getText() {
                return text;
            }
        }

        public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemView> {
            private ArrayList<HistoryItem> list;
            private Context ctx;

            public HistoryAdapter(Context ctx) {
                this.ctx = ctx;
                list = new ArrayList<>();
            }

            public void addItems(HistoryItem... items) {
                list.addAll(Arrays.asList(items));
            }

            @NonNull
            @Override
            public HistoryItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new HistoryItemView(
                        LayoutInflater.from(ctx).inflate(R.layout.item_history, viewGroup, false)
                );
            }

            @Override
            public void onBindViewHolder(@NonNull HistoryItemView historyItemView, int i) {
                historyItemView.tv.setText(list.get(i).getText());

            }

            @Override
            public int getItemCount() {
                return list.size();
            }


            class HistoryItemView extends RecyclerView.ViewHolder {

                private TextView tv;

                public HistoryItemView(View v) {
                    super(v);
                    tv = v.findViewById(R.id.tv);
                }
            }

        }

}
