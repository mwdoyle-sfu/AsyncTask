package com.example.matt.asynctask;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

/*
    Demonstation of how to use an AsyneTask by cracking a "password"
 */

public class MainActivity extends AppCompatActivity {

    private static final long PASSWORD = 12345;
    ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        progressBar = (ProgressBar) findViewById(R.id.progress_spinner2);
        setSupportActionBar(toolbar);
        progressBar.setVisibility(View.INVISIBLE);


    }

    public void  onClick_StartTask(View v) {
        final long PUBLISH_RATE = 1000;
        final long RANGE = 10000000;



        PasswordGuesserTask crackerTask = new PasswordGuesserTask();
        crackerTask.execute(RANGE, PUBLISH_RATE);
    }

    private void displayProgress(String message) {
        TextView textView = (TextView) findViewById(R.id.txtStatus);
        textView.setText(message);
    }

    private void displayAnswer(Long answer) {
        String message = "I know the password: " + answer;

        TextView textView = (TextView) findViewById(R.id.txtFinal);
        textView.setText(message);
    }

    /*
        Background task to crack the password
     */
    // Generics:
    // 1. Long: Type of reference(s) passed to doInBackground()
    // 2. String:  Type of reference passed to onProgressUpdate()
    // 3. Integer: Type of reference returned by doInBackground
    //             value passed to onPostExecute()
    private class PasswordGuesserTask extends AsyncTask<Long, String, Long>{

        // Executed on main UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        // Run on background thread
        @Override
        protected Long doInBackground(Long... arguments) {
            // Extract arguments
            long range = arguments[0];
            long publisgRate = arguments[1];

            long guess = 0;
            long count = 0;
            Random rand = new Random();
            while(guess != PASSWORD){
                guess = Math.abs(rand.nextLong()) % range;
                count++;

                if (count % publisgRate == 0){
                    publishProgress("Guess #: " + count,
                                            "Last guess: " + guess);
                }
            }

            return guess;
        }

        // Executed on main UI thread
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String message = "";
            for (String str : values){
                message += str + ", ";
            }

            displayProgress(message);
        }

        // Executed on main UI thread
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            displayAnswer(result);

            progressBar.setVisibility(View.INVISIBLE);

        }
    }
}
