package layout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zerokol.views.JoystickView;

import java.net.UnknownHostException;

import dimecho.com.rccontroller.R;
import dimecho.com.rccontroller.UDPClient;

public class ManualFragment extends Fragment {

    private JoystickView joystick;
    private JoystickView joystickLeft;
    private JoystickView joystickRight;
    private static int[] data = new int[]{0, 400, 400};

    public ManualFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        try {
            UDPClient.getInstance().sendData(new int[]{0,400,440});
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        joystick = (JoystickView) getView().findViewById(R.id.joystickView);
        joystickLeft = (JoystickView) getView().findViewById(R.id.joystickViewLeft);
        joystickRight = (JoystickView) getView().findViewById(R.id.joystickViewRight);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            activateRightJoystick();
            activateLeftJoystick();
        }
        else{
            activateCentralJoystick();
        }
    }

    private void activateCentralJoystick(){
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if (isGoingForward(direction)){
                    data[1] = 400 + power;
                }
                else{
                    data[1] = 400 - power;
                }
                int quadrant = getQuadrant(angle);
                if(quadrant == 1){
                    angle = angle>65?65:angle;
                    data[2] = 440 - Math.round((540 - 440)*angle/65);
                }
                else if (quadrant == 2){
                    angle = angle<-65?-65:angle;
                    data[2] = 440 - Math.round((540 - 440) * angle/65);
                }
                else if (quadrant == 3){
                    angle = angle>-115?-115:angle;
                    data[2] = 440 + Math.round((540 - 440) * (180 + angle)/65);
                }
                else if (quadrant == 4){
                    angle = angle<115?115:angle;
                    data[2] = 440 - Math.round((540 - 440) * (180 - angle)/65);
                }
                try {
                    UDPClient.getInstance().sendData(data);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                //System.out.println(data[0] + " " + data[1] + " " + data[2]);
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }

    private void activateLeftJoystick(){
        joystickLeft.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if(direction == JoystickView.FRONT){
                    data[1] = 400 + power;
                }
                else if(direction == JoystickView.BOTTOM){
                    data[1] = 400 - power;
                }
                try {
                    UDPClient.getInstance().sendData(data);
                    //System.out.println(data[0] + " " + data[1] + " " + data[2]);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }, 50L);

        joystickLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    data[1] = 400;
                    try {
                        UDPClient.getInstance().sendData(data);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    private void activateRightJoystick(){
        joystickRight.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                if(direction == JoystickView.RIGHT){
                    data[2] = 440 + power;
                }
                else if(direction == JoystickView.LEFT){
                    data[2] = 440 - power;
                }
                try {
                    UDPClient.getInstance().sendData(data);
                    //System.out.println(data[0] + " " + data[1] + " " + data[2]);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }, 50L);

        joystickRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    data[2] = 440;
                    try {
                        UDPClient.getInstance().sendData(data);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    private boolean isGoingForward(int direction){
        boolean forward = false;
        if(direction <=5){
            forward = true;
        }
        return forward;
    }

    private int getQuadrant(int angle){
        int quadrant = 1;
        if(angle >= 90){
            quadrant = 4;
        }
        else if(angle>=0){
            quadrant = 1;
        }
        else if(angle>-90){
            quadrant = 2;
        }
        else if(angle>= -180){
            quadrant = 3;
        }
        return quadrant;
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activateLeftJoystick();
            activateRightJoystick();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            activateCentralJoystick();
        }
    }*/
}
