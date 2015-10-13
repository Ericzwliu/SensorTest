package tw.com.eric.gsensortest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btnStart;
	private Button btnStop;
	private TextView tvX;
	private TextView tvY;
	private TextView tvZ;
	private Sensor acceleSensor;
	private Sensor gyroscSensor;
	private Sensor magneticSensor;
	private SensorManager sensorManager;
	private AccelerometerSensorRawDataCollector aCollector , gCollector, mCollector;
	
	private final static int X_INDEX = 0;
	private final static int Y_INDEX = 1;
	private final static int Z_INDEX = 2;
	
	private SensorEventListener aSensorEnvetListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			tvX.setText("X = " + event.values[X_INDEX]);
			tvY.setText("Y = " + event.values[Y_INDEX]);
			tvZ.setText("Z = " + event.values[Z_INDEX]);
			
			
			
			String[] data = new String[4];
			data[AccelerometerSensorRawDataCollector.TIMESTAMP_INDEX] = ( getCurrentTimeStamp() - aCollector.startTimestamp ) +"";
			data[AccelerometerSensorRawDataCollector.X_INDEX] = event.values[X_INDEX] + "";
			data[AccelerometerSensorRawDataCollector.Y_INDEX] = event.values[Y_INDEX] + "";
			data[AccelerometerSensorRawDataCollector.Z_INDEX] = event.values[Z_INDEX] + "";
					
			aCollector.addData( data );
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private SensorEventListener gSensorEnevtListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			String[] data = new String[4];
			data[AccelerometerSensorRawDataCollector.TIMESTAMP_INDEX] = ( getCurrentTimeStamp() - gCollector.startTimestamp )+"";
			data[AccelerometerSensorRawDataCollector.X_INDEX] = event.values[X_INDEX] + "";
			data[AccelerometerSensorRawDataCollector.Y_INDEX] = event.values[Y_INDEX] + "";
			data[AccelerometerSensorRawDataCollector.Z_INDEX] = event.values[Z_INDEX] + "";
			
			gCollector.addData( data );
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private SensorEventListener mSensorEventListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			
			String[] data = new String[4];
			data[AccelerometerSensorRawDataCollector.TIMESTAMP_INDEX] = ( getCurrentTimeStamp() - mCollector.startTimestamp ) +"";
			data[AccelerometerSensorRawDataCollector.X_INDEX] = event.values[X_INDEX] + "";
			data[AccelerometerSensorRawDataCollector.Y_INDEX] = event.values[Y_INDEX] + "";
			data[AccelerometerSensorRawDataCollector.Z_INDEX] = event.values[Z_INDEX] + "";
			
			mCollector.addData(data);
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		aCollector = new AccelerometerSensorRawDataCollector();
		gCollector = new AccelerometerSensorRawDataCollector();
		mCollector = new AccelerometerSensorRawDataCollector();
		
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		
		tvX = (TextView) findViewById(R.id.tvX);
		tvY = (TextView) findViewById(R.id.tvY);
		tvZ = (TextView) findViewById(R.id.tvZ);		
		
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				long st = getCurrentTimeStamp();
				aCollector.setStartTimeStamp(st);
				gCollector.setStartTimeStamp(st);
				mCollector.setStartTimeStamp(st);
				
				sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
				
				acceleSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				sensorManager.registerListener(aSensorEnvetListener, acceleSensor, sensorManager.SENSOR_DELAY_NORMAL);
				
				gyroscSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
				sensorManager.registerListener(gSensorEnevtListener, gyroscSensor, sensorManager.SENSOR_DELAY_NORMAL);
				
				if(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
					magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
					sensorManager.registerListener(mSensorEventListener, magneticSensor, sensorManager.SENSOR_DELAY_NORMAL);
				} else {
					magneticSensor = null;
					Log.d("GSensorTest", "Magnetic has not found");
				}

				
			}
		});
		
		btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sensorManager.unregisterListener(aSensorEnvetListener);
				sensorManager.unregisterListener(gSensorEnevtListener);
				sensorManager.unregisterListener(mSensorEventListener);
				
				Toast.makeText(MainActivity.this, "Unregister accelerometer Listener", Toast.LENGTH_SHORT).show();
				
				exportRawDataCSV( "aCollector", aCollector );
				exportRawDataCSV( "gCollector", gCollector );
				exportRawDataCSV( "mCollector", mCollector );
				
			}
		});
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		sensorManager.unregisterListener(aSensorEnvetListener);
		sensorManager.unregisterListener(gSensorEnevtListener);
		sensorManager.unregisterListener(mSensorEventListener);
		Toast.makeText(this, "Unregister accelerometer Listener", Toast.LENGTH_SHORT).show();
		super.onPause();
	}



	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	}



	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void exportRawDataCSV( final String fileTypr ,final AccelerometerSensorRawDataCollector c ) {
		
		File folder = new File(Environment.getExternalStorageDirectory() + "/GSensorRawData");
		
		
		
		
		boolean var = false;
		if(!folder.exists()) {
			var = folder.mkdir();
		}
		
		final String filename = folder.toString() + "/" + fileTypr + "_RawData_" + getCurrentTimeStamp() + ".csv";
		Toast.makeText(MainActivity.this, filename, Toast.LENGTH_LONG).show();
		Log.d("GSensorTest",filename);
		
		CharSequence contentTitle = getString(R.string.app_name);
		final ProgressDialog progDialog = ProgressDialog.show(
				MainActivity.this, 
				contentTitle, 
				"Exporting raw data ...",
				true
		);
		
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				try {
					FileWriter fw = new FileWriter(filename);
					fw.append("TimeStamp");
					fw.append(',');
					fw.append("X");
					fw.append(',');
					fw.append("Y");
					fw.append(',');
					fw.append("Z");
					fw.append(',');
					fw.append('\n');
					
					for(int i=0; i<c.size(); i++) {
						String[] data = c.getRecord(i);
						fw.append(data[AccelerometerSensorRawDataCollector.TIMESTAMP_INDEX]);
						fw.append(',');
						fw.append(data[AccelerometerSensorRawDataCollector.X_INDEX]);
						fw.append(',');
						fw.append(data[AccelerometerSensorRawDataCollector.Y_INDEX]);
						fw.append(',');
						fw.append(data[AccelerometerSensorRawDataCollector.Z_INDEX]);
						fw.append(',');
						fw.append('\n');
					}
					
					fw.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				handler.sendEmptyMessage(0);
				progDialog.dismiss();
			}
			
		}.start();
		
	}
	
	public long getCurrentTimeStamp(){
		long timestamp = System.currentTimeMillis();
		Timestamp tsTemp = new Timestamp(timestamp);
		return tsTemp.getTime();
	}
	
	
}
