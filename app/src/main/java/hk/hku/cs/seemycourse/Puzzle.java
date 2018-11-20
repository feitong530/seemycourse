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

import butterknife.BindView;
import butterknife.ButterKnife;

public class Puzzle {

    public static class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.PuzzleItemView> {
        private ArrayList<BitmapPiece> list;
        private Context ctx;

        public PuzzleAdapter(Context ctx, ArrayList<BitmapPiece> bitmapPieces) {
            this.ctx = ctx;
            list = bitmapPieces;
        }

        public ArrayList<BitmapPiece> getDataList() {
            return list;
        }

        @NonNull
        @Override
        public PuzzleItemView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new PuzzleItemView(
                    LayoutInflater.from(ctx)
                        .inflate(R.layout.item_puzzle, viewGroup, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull PuzzleItemView puzzleItemView, int i) {
            puzzleItemView.iv.setImageBitmap(list.get(i).getBitmap());
//            puzzleItemView.tv.setText(list.get(i).getIndex());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class PuzzleItemView extends RecyclerView.ViewHolder {
            @BindView(R.id.iv) ImageView iv;
//            @BindView(R.id.tv) TextView tv;

            public PuzzleItemView(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }
    }
}
