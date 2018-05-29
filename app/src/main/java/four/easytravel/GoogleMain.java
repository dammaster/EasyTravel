package four.easytravel;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Spinner;



public class GoogleMain extends AppCompatActivity {

    GoogleTranslate translator;
    EditText translateedittext;
    TextView translatabletext;
    public String selectedToLang;
    public String selectedFromLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_translate);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        translateedittext = (EditText) findViewById(R.id.translateedittext);
        Button translatebutton = (Button) findViewById(R.id.translatebutton);
        translatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnglishToTagalog().execute();
            }
        });
    }
    private class EnglishToTagalog extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        protected void onError(Exception ex) {
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                translator = new GoogleTranslate("AIzaSyBTBtfh9fHkvQbdev2UZPscb64J0m9c3ws");
                Thread.sleep(1000);
            } catch (Exception e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        @Override
        protected void onPreExecute() {
            //start the progress dialog
            progress = ProgressDialog.show(GoogleMain.this, null, "Translating...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            super.onPostExecute(result);
            translated();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

    }


    public void translated(){
        Spinner fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        selectedFromLang = fromSpinner.getSelectedItem().toString();

        Spinner toSpinner = (Spinner) findViewById(R.id.toSpinner);
        selectedToLang = toSpinner.getSelectedItem().toString();

        String translatetotagalog = translateedittext.getText().toString();//get the value of text
        String text = translator.translte(translatetotagalog, selectedFromLang, selectedToLang);
        translatabletext = (TextView) findViewById(R.id.translatabletext);
        translatabletext.setText(text);

    }
}
