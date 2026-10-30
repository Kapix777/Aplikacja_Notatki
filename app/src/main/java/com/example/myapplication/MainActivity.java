package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    String[] notatki = {
            "Zakupy: chleb, masło, ser",
            "Do zrobienia: obiad, umyć podłogi",
            "weekend: kino, spacer z psem"
    };
    ArrayList<String> listaNotatek = new ArrayList<>();

    String Dane = "dane.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Collections.addAll(listaNotatek, notatki);

        Button btn = findViewById(R.id.btn);
        Button reset = findViewById(R.id.reset);
        Button zapisz = findViewById(R.id.zapisz);
        Button wczytaj = findViewById(R.id.wczytaj);

        EditText editText = findViewById(R.id.editText);
        ListView lista = findViewById(R.id.lista);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNotatek);
        lista.setAdapter(arrayAdapter);

        btn.setOnClickListener(v -> {
            String tekst = editText.getText().toString();
            listaNotatek.add(tekst);
            arrayAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Dodano notatke", Toast.LENGTH_SHORT).show();
        });

        reset.setOnClickListener(v -> {
            listaNotatek.clear();
            arrayAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Reset", Toast.LENGTH_LONG).show();
        });

        zapisz.setOnClickListener(v -> {
            try{
                FileOutputStream plik = openFileOutput(Dane, MODE_PRIVATE);
                OutputStreamWriter strumienZnakow = new OutputStreamWriter(plik);
                BufferedWriter writer = new BufferedWriter(strumienZnakow);
                for(int i =0; i<listaNotatek.size();i++){
                    String notatka = listaNotatek.get(i);
                    writer.write(notatka);
                    writer.newLine();
                }
                writer.close();
                Toast.makeText(this,"Zapisano", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(this,"Błąd podczas zapisu pliku" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        wczytaj.setOnClickListener(v -> {
            try {
                FileInputStream strumienPliku = openFileInput(Dane);
                InputStreamReader odczytStrumienia = new InputStreamReader(strumienPliku);
                BufferedReader reader = new BufferedReader(odczytStrumienia);
                String linia;
                while((linia = reader.readLine()) != null){
                    listaNotatek.add(linia);
                }
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(this,"Wczytano", Toast.LENGTH_LONG).show();
            } catch(IOException e){
                Toast.makeText(this,"Błąd podczas wczytania pliku" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}