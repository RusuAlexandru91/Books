package com.example.andoid.probe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CartiAdapter extends ArrayAdapter<Carti> {

    public List<Carti> cartList;
    public List<Carti> origList;

    public CartiAdapter(Context context, List<Carti> carti) {
        super(context, 0, carti);
        cartList = carti;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Carti> results = new ArrayList<>();
                if (origList == null)
                    origList = cartList;
                if (constraint != null) {
                    if (origList != null && origList.size() > 0) {
                        for (final Carti g : origList) {
                            if (g.getTitle().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cartList = (ArrayList<Carti>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_layout, parent, false);
        }

        Carti currentCarti = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_main);
        nameTextView.setText(currentCarti.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.publisher_main);
        authorTextView.setText(currentCarti.getAuthor());

        TextView pagesTextView = (TextView) listItemView.findViewById(R.id.pages_main);
        pagesTextView.setText(currentCarti.getPages());

        return listItemView;
    }

    public List<Carti> getItems(){
        return cartList;
    }
}