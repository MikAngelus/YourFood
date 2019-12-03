package com.example.yourfood.ui.lista;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourfood.R;

import java.util.ArrayList;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.viewHolder> {


    private ArrayList<item> mExampleList;

    private OnItemClickListener mListener;
    private static int mPosition;
    private static boolean mResponse;

    public interface OnItemClickListener {

        void onItemClick(int position);

        void onDeleteClick(int position);

        void onConsumatoClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {


        mListener = listener;

    }


    public static class viewHolder extends RecyclerView.ViewHolder {

        public ImageView mDelete;
        public ImageView mConsumato;
        public TextView labelConsumato;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView labelConsumo;
        public Guideline g;

        public viewHolder(@NonNull View itemView, final OnItemClickListener listener, final boolean mResponse) {
            super(itemView);
            mDelete = itemView.findViewById(R.id.remove);
            mConsumato = itemView.findViewById(R.id.imageConsumato);
            mTextView1 = itemView.findViewById(R.id.line1);
            mTextView2 = itemView.findViewById(R.id.line2);
            labelConsumato = itemView.findViewById(R.id.label_consumato);
            labelConsumo = itemView.findViewById(R.id.label_consumo);
            g = itemView.findViewById(R.id.guideline72);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            listener.onItemClick(position);


                        }

                    }

                }
            });


            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            listener.onDeleteClick(position);
                        }

                    }
                }
            });


            mConsumato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {

                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            listener.onConsumatoClick(position);

                        }

                    }
                }
            });

        }

    }

    public ExampleAdapter(ArrayList<item> exampleList) {

        mExampleList = exampleList;


    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        viewHolder evh = new viewHolder(v, mListener, mResponse);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        item currentItem = mExampleList.get(position);

        holder.mConsumato.setImageResource(currentItem.getmImageConsumato());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.labelConsumo.setText(currentItem.getText4());
        holder.labelConsumato.setText(currentItem.getText3());

        float x = 0.99f;
        if (currentItem.getPerc() > x) {
            holder.g.setGuidelinePercent(currentItem.getPerc());
        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}
