package hk.hku.cs.seemycourse;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListFragment extends Fragment {

    @BindView(R.id.tv) TextView tv;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_todolist, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    /**
     * Initialize
     */
    private void init() {
        tv.setText("Write functions of the History Records part here!");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TodoListAdapter adapter = new TodoListAdapter(getContext());
        adapter.addItems(
                new TodoListTtem("message 01"),
                new TodoListTtem("message 02"),
                new TodoListTtem("message 03"),
                new TodoListTtem("message 04"),
                new TodoListTtem("message 05"),
                new TodoListTtem("message 06"),
                new TodoListTtem("message 07"),
                new TodoListTtem("message 08"),
                new TodoListTtem("message 09"),
                new TodoListTtem("message 10")
        );
        recyclerView.setAdapter(adapter);
        setupTouchEvent(adapter);
    }

    /**
     * Setup Touch Helper to handle Sort & Delete using Gesture
     * @param adapter Recycle View Adapter
     */
    private void setupTouchEvent(final TodoListAdapter adapter) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // Drag
                int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                // Swipe
                int swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                // Declare which direction to listen
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //滑动事件
                Collections.swap(adapter.getDataList(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //侧滑事件
                adapter.getDataList().remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public class TodoListTtem {
        private String text;

        public TodoListTtem(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListItemView> {
        private ArrayList<TodoListTtem> list;
        private Context ctx;

        public TodoListAdapter(Context ctx) {
            this.ctx = ctx;
            list = new ArrayList<>();
        }

        public void addItems(TodoListTtem... items) {
            list.addAll(Arrays.asList(items));
        }

        public ArrayList<TodoListTtem> getDataList() {
            return list;
        }

        @NonNull
        @Override
        public TodoListItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TodoListItemView(
                    LayoutInflater.from(ctx).inflate(
                            R.layout.item_history, viewGroup, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull TodoListItemView todoListItemView, int i) {
            todoListItemView.tv.setText(list.get(i).getText());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class TodoListItemView extends RecyclerView.ViewHolder {
            @BindView(R.id.tv) TextView tv;

            public TodoListItemView(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

    }

}
