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
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_todolist, container, false);
        ButterKnife.bind(this, view);
        init(getContext());
        return view;
    }

    /**
     * Initialize
     */
    private void init(Context ctx) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TodoListAdapter adapter = new TodoListAdapter(getContext());

        // Load Data
        ArrayList<String> list = Util.loadSchedule(ctx);
        adapter.setItems(list);

        recyclerView.setAdapter(adapter);
        setupTouchEvent(adapter, ctx);
    }

    /**
     * Setup Touch Helper to handle Sort & Delete using Gesture
     * @param adapter Recycle View Adapter
     */
    private void setupTouchEvent(final TodoListAdapter adapter, final Context ctx) {
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
                // dragging event
                Collections.swap(adapter.getDataList(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Util.saveSchedule(ctx, adapter.getStringList());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                // swiping event
                adapter.getDataList().remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Util.saveSchedule(ctx, adapter.getStringList());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public class TodoListItem {
        private String text;

        public TodoListItem(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListItemView> {
        private ArrayList<TodoListItem> list;
        private Context ctx;

        public TodoListAdapter(Context ctx) {
            this.ctx = ctx;
            list = new ArrayList<>();
        }

        public void setItems(ArrayList<String> items) {
            list = new ArrayList<>(items.size());
            for (String item : items) {
                list.add(new TodoListItem(item));
            }
        }

        public ArrayList<String> getStringList() {
            ArrayList<String> stringList = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); ++i) {
                stringList.add(list.get(i).getText());
            }
            return stringList;
        }

        public ArrayList<TodoListItem> getDataList() {
            return list;
        }

        @NonNull
        @Override
        public TodoListItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TodoListItemView(
                    LayoutInflater.from(ctx).inflate(
                            R.layout.item_todo, viewGroup, false)
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
