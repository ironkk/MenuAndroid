package dam.xab.llansador;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    // atributos para lanzar la camara
    private Button btn_photo;
    private boolean isResource = true;
    private ImageView imgView;

    Button[] btn;
    int[] btnId = { R.id.button6
};

    class BtnInfo {
        int idx;
        String text;
        String url;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = new Button[btnId.length];
        for (int i = 0; i < btnId.length; i++) {
            btn[i] = (Button) findViewById(btnId[i]);
            btn[i].setOnClickListener(this);
        }

        btn_photo = (Button) findViewById(R.id.button7);
        btn_photo.setOnClickListener(this);


        Button btnAbrirUrl = (Button) findViewById(R.id.button);
        btnAbrirUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OBRIM UNA URL
                Uri uri = Uri.parse("http://www.movifilms.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);

            }
        });

        Button btnLlamada = (Button) findViewById(R.id.button2);
        btnLlamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamada
                Uri uri = Uri.parse("tel:628227327");
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);

                startActivity(intent);

            }
        });

        Button btnSMS = (Button) findViewById(R.id.button3);
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SMS
                Uri uri = Uri.parse("smsto:628227327");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri); intent.putExtra("sms_body", "Contingut del SMS");

                startActivity(intent);

            }
        });

        Button btnEmail = (Button) findViewById(R.id.button4);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EMAIL
                Intent intent = new Intent(Intent.ACTION_SEND); intent.putExtra(Intent.EXTRA_EMAIL, "xavi.manzano1@gmail.com");
                intent.putExtra(Intent.EXTRA_TEXT, "Información");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Tria el client de correu"));

            }
        });


        Button btnGoogle = (Button) findViewById(R.id.button5);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // BUSCAR GOOGLE PLAY APP

                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.king.candycrushsaga&hl=es");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        //ACTION_ANSWER
        //Gestionar trucades entrants


    }

    @Override public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String undef = getResources().getString(R.string.undefined);
        for (int i = 0; i < btn.length; i++) {
            String prefText = String.format(Locale.getDefault(), "text%02d", i + 1);
            String text = prefs.getString(prefText, undef);
            if (text.isEmpty()) text = undef;
            String prefUrl = String.format(Locale.getDefault(), "url%02d", i + 1);
            String url = prefs.getString(prefUrl, "");
            BtnInfo info = new BtnInfo();
            info.idx = i + 1;
            info.text = text;
            info.url = url;
            btn[i].setText(text);
            btn[i].setTag(info);
        }
    }

    // CAMARA DE FOTOS 1

    public void emptyImageViewer() {
        if (this.isResource) return;
        Drawable drawable = imgView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if (!bitmap.isRecycled()) bitmap.recycle();
        }
        imgView.setImageResource(android.R.drawable.ic_menu_camera);
        this.isResource = true;
    }

    //2
    private final static int CAPTURE_PHOTO = 2;
    private Uri photoUri;

    public void takePhoto() {
        File dir = getExternalFilesDir(null);
        if (dir == null) {
            Toast.makeText(this, "Unable to mount the filesystem",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(dir, "photo.jpg");
        photoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(i, CAPTURE_PHOTO);
    }



    //3
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        emptyImageViewer();
        if (requestCode == CAPTURE_PHOTO) {
            imgView.setImageURI(photoUri);
            this.isResource = false;
        }
        else {
            Uri img = data.getData();
            if (img != null) {
                imgView.setImageURI(img);
                this.isResource = false;
            }
        }
        imgView.invalidate();
    }

    // CREAR provider_paths.xml por el photoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override public void onClick(View v) {
            takePhoto();

        }
}
