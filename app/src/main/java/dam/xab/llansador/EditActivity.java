package dam.xab.llansador;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    EditText editText1, editText2;

    int idx;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idx = extras.getInt("idx");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String favText = String.format(Locale.getDefault(), "text%02d", idx);
            String text = prefs.getString(favText, "");
            editText1.setText(text);
            String favUrl = String.format(Locale.getDefault(), "url%02d", idx);
            String url = prefs.getString(favUrl, "");
            editText2.setText(url);
        }
    }

    @Override public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor ed = prefs.edit();
        String text = editText1.getText().toString().trim();
        String favText = String.format(Locale.getDefault(), "text%02d", idx);
        ed.putString(favText, text);
        String url = editText2.getText().toString().trim();
        String favUrl = String.format(Locale.getDefault(), "url%02d", idx);
        ed.putString(favUrl, url);
        ed.commit();
    }
}
