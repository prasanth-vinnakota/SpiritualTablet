package com.example.spiritualtablet.dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.spiritualtablet.R;
import com.example.spiritualtablet.about.PrimaryCenters;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
    }

    public void registeredOffice(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Registered Office");
        builder.setMessage("SPIRITUAL TABLET ADMISSION CENTER\n" +
                "D NO 27-13-354 DIBBAPALEM COLONY\n" +
                "SRINAGAR, GAJUWAKA\n" +
                "VISAKHAPATNAM-530026");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void primaryCenters(View view) {

        startActivity(new Intent(this, PrimaryCenters.class));
    }

    public void admissionCenters(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Admission Centers");
        builder.setMessage("MODUGULA VISHWA KANTHI PYRAMID SAKTHI KSHETRAM\n" +
                "B .KIRANMAI(INCHARGE)\n" +
                "contact number : 9704452754\n" +
                "Address: (VISHWA KANTHI PYRAMIDSPIRITUAL TRUST(VIZAG)\n" +
                "REG NO:58/2011 VADDADHI, MADUGULA ,VISAKHAPATNAM.\n" +
                "(VISHWA KANTHI PYRAMID SAKTHI KSHETRAM)");

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void holisticCenters(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Holistic Centers");
        builder.setMessage("Pyramid Valley, Bengaluru\n" +
                "www.pyamidvalley.org\n" +
                "\n" +
                "\n" +
                "Kadtal Maheswara Maha Pyramid\n" +
                "www.maheshwarapyramid.org");

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
