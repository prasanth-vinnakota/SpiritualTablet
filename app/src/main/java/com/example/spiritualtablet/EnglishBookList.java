package com.example.spiritualtablet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class EnglishBookList extends AppCompatActivity {

    List<DataItem> lstDataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        lstDataItem =new ArrayList<>();
        lstDataItem.add(new DataItem("Direct And Indirect Knowledge", "English", R.drawable.direct_and_indirect_knowledge));
        lstDataItem.add(new DataItem("Lakshmi Paravathi Saraswathi", "English", R.drawable.lakshmi_parvathi_sarswathi));
        lstDataItem.add(new DataItem("Meditation", "English", R.drawable.meditation));
        lstDataItem.add(new DataItem("Purpose of Life", "English", R.drawable.purpose_of_life));
        lstDataItem.add(new DataItem("Six Passions", "English", R.drawable.six_passions));
        lstDataItem.add(new DataItem("Soul Family", "English", R.drawable.soul_family));
        lstDataItem.add(new DataItem("Thyroid", "English", R.drawable.thyriod));

        RecyclerView myrv = findViewById(R.id.recycler_view_id);
        BooksRecyclerViewAdapter myAdapter = new BooksRecyclerViewAdapter(getApplicationContext(), lstDataItem);
        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        myrv.setAdapter(myAdapter);
    }
}
