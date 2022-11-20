package com.wimir.piotrmomot.nbpcalc;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Button przycisk, button2, button3;
    TextView textView3, textView6;
    EditText editText;
    Spinner spinner, spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Waluty waluty = null;
        AsyncTaskRunner runner = new AsyncTaskRunner();
        try {
            runner.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        przycisk = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView6 = (TextView) findViewById(R.id.textView6);
        editText = (EditText) findViewById(R.id.editText);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner.setSelection(1);

        try {
            textView3.setText(waluty.getDate());
        } catch (Exception e){
            textView3.setText("Problem z internetem!\nDane nie zostały pobrane.");
            przycisk.setEnabled(false);
            button2.setVisibility(View.VISIBLE);
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( MainActivity.this )
                        .setTitle( "Pomoc" )
                        .setMessage( "PLN - polski złoty\n" +
                                "USD - dolar amerykański\n" +
                                "EUR - euro\n" +
                                "CHF - frank szwajcarski\n" +
                                "GBP - funt brytyjski\n" +
                                "THB - bat (Tajlandia)\n" +
                                "AUD - dolar australijski\n" +
                                "HKD - dolar Hongkongu\n" +
                                "CAD - dolar kanadyjski\n" +
                                "NZD - dolar nowozelandzki\n" +
                                "SGD - dolar singapurski\n" +
                                "UAH - hrywna (Ukraina)\n" +
                                "CZK - korona czeska\n" +
                                "DKK - korona duńska\n" +
                                "NOK - korona norweska\n" +
                                "SEK - korona szwedzka\n" +
                                "HRK - kuna (Chorwacja)\n" +
                                "RON - lej rumuński\n" +
                                "BGN - lew (Bułgaria)\n" +
                                "TRY - lira turecka\n" +
                                "ILS - nowy izraelski szekel\n" +
                                "PHP - peso filipińskie\n" +
                                "MXN - peso meksykańskie\n" +
                                "ZAR - rand (Republika Południowej Afryki)\n" +
                                "BRL - real (Brazylia)\n" +
                                "MYR - ringgit (Malezja)\n" +
                                "RUB - rubel rosyjski\n" +
                                "CNY - yuan renminbi (Chiny)\n" +
                                "XDR - SDR (MFW)\n\n(C) 2016 Piotr Momot, WIMIR, AGH"
                        )
                        .setPositiveButton( "Zamknij", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Positive" );
                            }
                        })
                        .show();
            }
        });

       button2.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                try {
                    runner.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    textView3.setText(waluty.getDate());
                    button2.setVisibility(View.INVISIBLE);
                    przycisk.setEnabled(true);
                } catch (Exception e) {
                    textView3.setText("Problem z internetem!\nDane nie zostały pobrane.");
                    przycisk.setEnabled(false);
                    button2.setVisibility(View.VISIBLE);
                }
            }
        });

        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    double editTextDouble = Double.parseDouble(editText.getText().toString());
                    String settxt = null;
                    try {
                        settxt = String.valueOf(waluty.convertX2Y(editTextDouble, spinner.getSelectedItem().toString(), spinner2.getSelectedItem().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textView6.setText(settxt);
                }
                else {
                    new AlertDialog.Builder( MainActivity.this )
                            .setTitle( "Błąd" )
                            .setMessage( "Nie wprowadzono kwoty początkowej!" )
                            .setPositiveButton( "Naprawię to", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d( "AlertDialog", "Positive" );
                                }
                            })
                            .show();
                }
            }
        });


    }


    private class AsyncTaskRunner extends AsyncTask<Waluty, Waluty, Waluty>{
        private String resp;
        private Waluty waluty = null;

        @Override
        protected Waluty doInBackground(Waluty... params) {
            try {
                waluty = new Waluty();
                return waluty;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
