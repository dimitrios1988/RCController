package dimecho.com.rccontroller;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.net.UnknownHostException;

import layout.AutomaticFragment;
import layout.ManualFragment;


public class MainActivity extends AppCompatActivity {

    private ToggleButton autoToggle;
    private Toolbar toolbar;
    private static ManualFragment manualFragment = new ManualFragment();
    private static AutomaticFragment automaticFragment = new AutomaticFragment();
    private static String selected = "manual";
    //private JoystickView joystick;
    //private int[] data = new int[]{0, 400, 400};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        autoToggle = (ToggleButton) findViewById(R.id.autoToggle);

        if(selected.equals("manual")){
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, manualFragment).commit();
        }
        else if(selected.equals("auto")){
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, automaticFragment).commit();
        }

        autoToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autoToggle.isChecked()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, automaticFragment).commit();
                    selected = "auto";
                    AsyncTask<Void, Void, Void> async_client = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Starting Image Recognition...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Image Recognition started successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    };
                    if (Build.VERSION.SDK_INT>=11) async_client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else async_client.execute();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, manualFragment).commit();
                    selected = "manual";
                    Toast.makeText(getApplicationContext(), "Image Recognition stopped", Toast.LENGTH_SHORT).show();
                    try {
                        UDPClient.getInstance().sendData(new int[]{0,400,440});
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
