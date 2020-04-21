package com.example.spiritualtablet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;



public class EnglishBooksList extends AppCompatActivity {

    ListView englishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_books_list);

        englishList = findViewById(R.id.english_books_list);

        final ArrayList<String> booksList = new ArrayList<>();

        booksList.add("Direct & Indirect Knowledge");
        booksList.add("Lakshmi Paravathi Saraswathi");
        booksList.add("Meditation");
        booksList.add("Purpose of Life");
        booksList.add("Six Passions");
        booksList.add("Soul Family");
        booksList.add("Thyroid");

        ArrayAdapter bookListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, booksList);
        englishList.setAdapter(bookListAdapter);

        englishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(EnglishBooksList.this, ViewBook.class);
                intent.putExtra("language","English");
                intent.putExtra("book_no", ""+position);
                intent.putExtra("book_name", booksList.get(position));
                startActivity(intent);
            }
        });
    }
}
