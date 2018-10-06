package com.example.masst.nearbyme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button hospital, petrol, atm, hotel, police;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hospital = (Button) findViewById(R.id.hospital);
        petrol = (Button) findViewById(R.id.petrol);
        atm = (Button) findViewById(R.id.atm);
        hotel = (Button) findViewById(R.id.hotel);
        police = (Button) findViewById(R.id.police);

        hospital.setOnClickListener(this);
        petrol.setOnClickListener(this);
        atm.setOnClickListener(this);
        hotel.setOnClickListener(this);
        police.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.hospital:
                Intent hos = new Intent(getApplicationContext(), ListActivity.class);
                hos.putExtra("SEARCH_TYPE", "hospital|private hospital");
                startActivity(hos);
                break;
            case R.id.petrol:
                Intent pet = new Intent(getApplicationContext(), ListActivity.class);
                pet.putExtra("SEARCH_TYPE", "petrol bunk");
                startActivity(pet);
                break;
            case R.id.atm:
                Intent atm = new Intent(getApplicationContext(), ListActivity.class);
                atm.putExtra("SEARCH_TYPE", "atm");
                startActivity(atm);
                break;
            case R.id.hotel:
                Intent hot = new Intent(getApplicationContext(), ListActivity.class);
                hot.putExtra("SEARCH_TYPE", "hotel");
                startActivity(hot);
                break;
            case R.id.police:
                Intent pol = new Intent(getApplicationContext(), ListActivity.class);
                pol.putExtra("SEARCH_TYPE", "police station");
                startActivity(pol);
                break;
            default:
                break;
        }
    }
}
