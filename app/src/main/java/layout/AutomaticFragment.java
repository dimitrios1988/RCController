package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.net.UnknownHostException;

import dimecho.com.rccontroller.R;
import dimecho.com.rccontroller.UDPClient;


public class AutomaticFragment extends Fragment {

    private TabHost tabhost;
    private SeekBar speedBar;
    private TextView speedIndicator;
    private Button haltButton;
    private Button semiGoBotton;
    private Button fullGoButton;
    private EditText metersText;
    private EditText secondsText;

    public AutomaticFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_automatic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        try {
            UDPClient.getInstance().sendData(new int[]{1,400,440});
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        tabhost = (TabHost) getView().findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec semitab = tabhost.newTabSpec("Semi - Auto");
        semitab.setContent(R.id.semiAuto);
        semitab.setIndicator("Semi - Auto");
        tabhost.addTab(semitab);

        TabHost.TabSpec fulltab = tabhost.newTabSpec("Full Auto");
        fulltab.setContent(R.id.fullAuto);
        fulltab.setIndicator("Full Auto");
        tabhost.addTab(fulltab);

        speedIndicator = (TextView) getView().findViewById(R.id.speedIndicator);
        speedBar = (SeekBar) getView().findViewById(R.id.speedBar);
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speedIndicator.setText(Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        haltButton = (Button) getView().findViewById(R.id.haltButton);
        haltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Halted", Toast.LENGTH_SHORT).show();
                try {
                    UDPClient.getInstance().sendData(new int[]{1,400,440});
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        semiGoBotton = (Button) getView().findViewById(R.id.semiGoBotton);
        semiGoBotton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int speed = speedBar.getProgress();
                try {
                    UDPClient.getInstance().sendData(new int[]{1,speed+400,440});
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        metersText = (EditText) getView().findViewById(R.id.metersInput);
        secondsText = (EditText) getView().findViewById(R.id.secondsInput);
        fullGoButton = (Button) getView().findViewById(R.id.fullGoBotton);
    }

}
