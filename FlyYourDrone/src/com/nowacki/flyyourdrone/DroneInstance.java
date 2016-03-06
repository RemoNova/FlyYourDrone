package com.nowacki.flyyourdrone;

import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import android.app.Application;
import android.database.Cursor;
import android.location.Location;

public class DroneInstance extends Application {

	private IARDrone drone;
	private Database droneDatabase;
	private boolean steeringLedAnim;
	private int veriticalSpeed;
	private int rotationSpeed;
	private int maxAltitiude;
	private double eulerAngle;
	private int flightNumber;
	private boolean serverReadyFlag;
	
	private double tmpHeightValue = 0;

	
	public void onCreate()
	{
		drone = new ARDrone("192.168.1.1", null);
		
		droneDatabase = new Database(getApplicationContext());
	    Cursor cursor=droneDatabase.getOptions();
	    
		steeringLedAnim = Boolean.valueOf(cursor.getString(2).toString());
		veriticalSpeed = Integer.valueOf(cursor.getString(5).toString());
		rotationSpeed = Integer.valueOf(cursor.getString(4).toString());
		eulerAngle = Double.valueOf(cursor.getString(1).toString());
		maxAltitiude = Integer.valueOf(cursor.getString(3).toString());
		
		flightNumber = 1;
		
	}
	public void closeInstance()
	{
		droneDatabase.close();
		
		System.exit(0);
	}
	
	
	public boolean getServerFlag(){
		return this.serverReadyFlag;
	}
	
	public boolean setServerFlag(boolean serverReady){
		return this.serverReadyFlag = serverReady;
	}
	
	public double getTmpHeight(){
		return this.tmpHeightValue;
	}
	
	public double setTmpHeight(double heightValue){
		return this.tmpHeightValue = heightValue;
	}
	
	public int getFlightNumber()
	{
		return flightNumber;
	}
	
	public int setFlightNumber(int flightNumber)
	{
		return this.flightNumber = flightNumber;
	}

	public void insertLocation(Location location, int flightId, double height)
	{
		this.droneDatabase.insertLocation(location, flightId, height);
	}
	
	public Cursor getDroneLocations(int flightId)
	{
		return this.droneDatabase.getLocationsData(flightId);
	}
	
	public Cursor getDroneFlights()
	{
		return this.droneDatabase.getFlightsData();
	}
	
	public Cursor getDroneFlights(int flightId)
	{
		return this.droneDatabase.getFlightsData(flightId);
	}
	
	public void insertDroneFlights(double maxHeight)
	{
		this.droneDatabase.insertFlightsData(maxHeight);
	}
	
	public int getLastFlightId()
	{
		return this.droneDatabase.getLastFlightNumber();
	}
	
	public IARDrone getARDrone()
	{
		return this.drone;
	}
	
	public boolean getSteeringLedAnim()
	{
		return this.steeringLedAnim;
	}
	
	public boolean setSteeringLedAnim(boolean takenSteeringLedAnim)
	{
		this.droneDatabase.updateSteeringLedAnim(takenSteeringLedAnim);
		return this.steeringLedAnim = takenSteeringLedAnim;
		
	}
	
	public int getVeriticalSpeed()
	{
		return this.veriticalSpeed;
	}
	
	public int setVeriticalSpeed(int veriticalSpeed)
	{
		this.droneDatabase.updateVeriticalSpeed(veriticalSpeed);
		return this.veriticalSpeed = veriticalSpeed;
	}
	
	public int getRotationSpeed()
	{
		return this.rotationSpeed;
	}
	
	public int setRotationSpeed(int rotationSpeed)
	{
		this.droneDatabase.updateRotationSpeed(rotationSpeed);
		return this.rotationSpeed = rotationSpeed;
	}
	
	public int getMaxAltitiude()
	{
		return this.maxAltitiude;
	}
	
	public int setMaxAltitiude(int maxAltitiude)
	{
		this.droneDatabase.updateMaxAltitiude(maxAltitiude);
		return this.maxAltitiude = maxAltitiude;
	}
	
	public double setEulerAngle(double eulerAngle)
	{
		this.droneDatabase.updateEulerAngle(eulerAngle);
		return this.eulerAngle = eulerAngle;
	}
	
	public double getEulerAngle()
	{
		return this.eulerAngle;
	}
}
