package com.example.spiritualtablet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class    TeluguBooksList extends AppCompatActivity {

    ListView teluguList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telugu_books_list);

        teluguList = findViewById(R.id.telugu_books_list);

        final ArrayList<String> booksList = new ArrayList<>();

        booksList.add("Mudu Rakala Sevalu");
        booksList.add("Nalugu Setruvulu");
        booksList.add("7 Shakthi Kendralu");
        booksList.add("Aadhyathmika Putrulu");
        booksList.add("Arishadvargalu");
        booksList.add("Nissahayata");
        booksList.add("Dhayna Vidya Sanjeevini Vidya");
        booksList.add("Prathyaksha Gnanam Paroksha Gnanam");
        booksList.add("Aacharya Saangatyam");
        booksList.add("Aathma Kutumba Dharmam");
        booksList.add("Chalakudi nundi Saasanudu");
        booksList.add("Jeevitha Dhyeyam");
        booksList.add("Mudu Gunaalu");
        booksList.add("Lakshmi Paravathi Saraswathi");
        booksList.add("Dhyanam");
        booksList.add("Yogaparampara");
        booksList.add("Paraspara Sambandhalu / Teagani Sambandhalu");
        booksList.add("Pancha Bhutalu");
        booksList.add("5 Saamrajyaalu");
        booksList.add("Pidikili");
        booksList.add("5 Pedda Tappulu");
        booksList.add("2 Tablets");
        booksList.add("12 Tablets");
        booksList.add("Athma Scanning");
        booksList.add("Nijamaina Seva");
        booksList.add("Samasya Aathyathmika Pariskaram");
        booksList.add("Gnaname Avushadamu");
        booksList.add("Mudu Dharmaalu");
        booksList.add("7 Anubhavaalu");
        booksList.add("Adrushtam Duradrushtam");
        booksList.add("Dhyana Jeevitham");
        booksList.add("Garbaasayam");
        booksList.add("Manchi Kashtalu");
        booksList.add("Shaktivalayalu");
        booksList.add("Sugar");
        booksList.add("Swadyayam");
        booksList.add("Thyroid");
        booksList.add("Traffic Signals");

        ArrayAdapter bookListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, booksList);

        teluguList.setAdapter(bookListAdapter);

        teluguList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(TeluguBooksList.this, ViewBook.class);
                intent.putExtra("language","Telugu");
                intent.putExtra("book_no", ""+position);
                intent.putExtra("book_name", booksList.get(position));
                startActivity(intent);
            }
        });
    }
}
