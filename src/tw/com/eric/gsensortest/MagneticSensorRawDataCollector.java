package tw.com.eric.gsensortest;

import java.util.ArrayList;
import java.util.List;

public class MagneticSensorRawDataCollector {
	
	public final static int TIMESTAMP_INDEX = 0;
	public final static int X_INDEX = 1;
	public final static int Y_INDEX = 2;
	public final static int Z_INDEX = 3;
	
	private ArrayList<String[]> rawData;
	
	public MagneticSensorRawDataCollector(){
		this.rawData = new ArrayList<String[]>();
	}
	
	public void addData(String[] data) {
		this.rawData.add(data);
	}
	
	public ArrayList<String[]> getAllRecords(){
		return this.rawData;
	}
	
	public String[] getRecord(int index){
		return this.rawData.get(index);
	}
	
	public int size(){
		return this.rawData.size();
	}

}
