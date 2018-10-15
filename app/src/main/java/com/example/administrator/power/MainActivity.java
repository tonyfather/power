package com.example.administrator.power;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.util.Log;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import android.widget.ImageView;


import com.rscja.deviceapi.CardWithBYL;
import com.rscja.deviceapi.Module;
import com.rscja.deviceapi.ScanerLedLight;
import com.rscja.deviceapi.exception.ConfigurationException;

public class MainActivity extends AppCompatActivity {
    private ToggleButton toggleButton;
	Module module=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		try {
			module= Module.getInstance();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		toggleButton=(ToggleButton)findViewById(R.id.tb);
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
		boolean powerState = pref.getBoolean("power", false);
		toggleButton.setChecked(powerState);



        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(!isChecked){
						SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
						editor.putBoolean("power", false);
						editor.apply();
						powerOff();
						Toast.makeText(MainActivity.this, "Power off.", Toast.LENGTH_SHORT).show();
						Log.d("MainActivity", "powerOff");
					}else{
						SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
						editor.putBoolean("power", true);
						editor.apply();
						powerOn();
						Toast.makeText(MainActivity.this, "Power on.", Toast.LENGTH_SHORT).show();
						Log.d("MainActivity", "powerOn");
					}
            }
        });
    }
	private  synchronized  void  powerOn() {
		module.ioctl_gpio(1, true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		module.ioctl_gpio(60, true);
	}
	private void  powerOff(){
		module.ioctl_gpio(60, false);
	}
}