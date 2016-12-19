package org.esiea.pei_ji_ouyang_yvon.programationmobile;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {




    DatePickerDialog dpd;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv;
        rv=(RecyclerView) findViewById(R.id.rv_biere);

        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        final TextView tv_hw = (TextView) findViewById(R.id.tv_hello_world);
        Button btn_hw = (Button) findViewById(R.id.btn_services);
        Button btn_as = (Button) findViewById(R.id.activity_seconde);
        Button btn_GetBiersServices = (Button) findViewById(R.id.GetBiersServices);

        getString(R.string.hello_world);

        final String now = DateUtils.formatDateTime(getApplicationContext(), (new Date()).getTime(), DateFormat.FULL);
        tv_hw.setText(now);

        btn_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.hello_world), Toast.LENGTH_LONG).show();
                dpd.show();
                Intent login = new Intent(MainActivity.this, SecondeActivity.class);
                startActivity(login);
                Notification();
            }
        });

        btn_as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),getString(R.string.hello_world),Toast.LENGTH_LONG).show();
                //Intent start = new Intent(MainActivity.this,SecondeActivity.class);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Londre")));
                // Notification();
            }
        });


        btn_GetBiersServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetBiersServices.startActionBiers(MainActivity.this);
            }
        });



        DatePickerDialog.OnDateSetListener odsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tv_hw.setText(i + " years " + i1 + " months " + i2 + " jours ");
            }
        };

        dpd = new DatePickerDialog(this, odsl, 2000, 2, 20);
        //GetBiersServices.startActionBiers(this);
        //GetBiersServices.startActionBiers(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(),intentFilter);
    }

    public void Notification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.icone)
                .setContentTitle("YOLO")
                .setContentText("HelloWorld");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public static final String BIERS_UPDATE = "com.octip.cours.inf4042_11.BIERS_UPDATE";

    public class BierUpdate extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            Log.d("tag",getIntent().getAction());
            Toast.makeText(getApplicationContext(), "telechargement", Toast.LENGTH_LONG).show();
            //prevoir une action de notification ici
        }

    }

    public JSONArray getBiersFromFile(){

        try{
            InputStream is = new FileInputStream(getCacheDir()+"/"+"bieres.json");
            byte[] buffer = new byte [is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer,"UTF-8"));

        }catch (IOException e){
            e.printStackTrace();
            return new JSONArray();
        }catch (JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder> {

        private Arrays[] biersList;

        public BiersAdapter(Arrays[] biersList){this.biersList=biersList;}
        @Override
        public BiersAdapter.BierHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_bier_element, null);
            BierHolder bh = new BierHolder(itemLayoutView);
            return bh;

        }


        @Override
        public void onBindViewHolder(BiersAdapter.BierHolder holder, int position) {
            JSONArray bieres;
            bieres = getBiersFromFile();
            try{
                JSONObject jsonobject = bieres.getJSONObject(position);
                holder.modifierItemList(jsonobject.getString("name"));
                holder.modifierItemYear(jsonobject.getString("description"));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }


        public void setNewBiere(JSONArray jsonobject1){
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount(){return getBiersFromFile().length();}

        public class BierHolder extends RecyclerView.ViewHolder {


            private TextView itemView;
            private TextView itemViewYear;

            public BierHolder(View itemView){
                super(itemView);
                this.itemView=(TextView) itemView.findViewById(R.id.tv_hello_world);
                this.itemViewYear=(TextView) itemView.findViewById(R.id.tv_hello_world);

            }
            public void modifierItemYear(String s){
                itemViewYear.setText(s);
            }

            public void modifierItemList(String s){
                itemView.setText(s);
            }

        }
    }

}
