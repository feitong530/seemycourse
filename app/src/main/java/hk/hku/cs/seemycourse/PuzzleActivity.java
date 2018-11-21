package hk.hku.cs.seemycourse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PuzzleActivity extends AppCompatActivity {
    public static final int REQUEST_PUZZLE_GAME = 0b1001;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        // Bitmaps not loaded
        if (Util.puzzleList == null) {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        }

        int spanCount = getIntent().getIntExtra("spanCount", 3);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        Puzzle.PuzzleAdapter adapter = new Puzzle.PuzzleAdapter(this, Util.puzzleList);
        recyclerView.setAdapter(adapter);
        setupTouchEvent(adapter);
    }

    private void setupTouchEvent(final Puzzle.PuzzleAdapter adapter) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // Drag
                int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // Drag event
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                ArrayList<BitmapPiece> list = adapter.getDataList();
                BitmapPiece p = list.remove(from);
                list.add(to, p);
                adapter.notifyItemMoved(from, to);

                boolean accurate = true;
                for (int i = 0; i < list.size(); ++i) {
                    accurate &= String.valueOf(i).equals(list.get(i).getIndex());
                    // TODO: remove debugging output
                    Log.e("puzzle", "[" + i + "]: " + list.get(i).getIndex());
                }
                Log.e("puzzle", accurate ? "Success!": "Not yet!");

                if (accurate) {
                    setResult(RESULT_OK, new Intent());
                    finishAfterTransition();
                }
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                // Do nothing
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
