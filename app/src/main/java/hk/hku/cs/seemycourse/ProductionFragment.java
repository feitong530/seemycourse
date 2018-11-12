package hk.hku.cs.seemycourse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductionFragment extends Fragment {


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frag_production , container, false);
            initLayout(view);
            return view;
        }

        private void initLayout(View v) {
            ImageView a = v.findViewById(R.id.imageView3);
            a.setImageResource(R.drawable.wjw);

            ImageView b = v.findViewById(R.id.imageView2);
            b.setImageResource(R.drawable.wjw);

//            ImageView c = v.findViewById(R.id.imageView3);
//            c.setImageResource(R.drawable.wjw);
//
            ImageView d = v.findViewById(R.id.imageView4);
            d.setImageResource(R.drawable.wjw);
//
            ImageView e = v.findViewById(R.id.imageView5);
            e.setImageResource(R.drawable.wjw);
//
            ImageView f = v.findViewById(R.id.imageView6);
            f.setImageResource(R.drawable.wjw);
//
            ImageView g = v.findViewById(R.id.imageView7);
            g.setImageResource(R.drawable.wjw);
//
            ImageView h = v.findViewById(R.id.imageView8);
            h.setImageResource(R.drawable.wjw);



        }

}
