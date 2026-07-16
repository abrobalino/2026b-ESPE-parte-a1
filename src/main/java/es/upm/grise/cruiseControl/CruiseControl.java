package es.upm.grise.cruiseControl;

import es.upm.grise.cruiseControl.exceptions.*;

public class CruiseControl {
	
	private RoadInformation roadInformation;
	private Speedometer speedometer;
	private Integer speedLimit;
	private Integer speedSet;
	private boolean enabled = false;
	
	/*
	 * Constructor
	 */
	
	public CruiseControl(RoadInformation roadInformation, Speedometer speedometer) {
		this.roadInformation = roadInformation;
		this.speedometer = speedometer;
		this.speedLimit = null;
		this.speedSet = null;
		this.enabled = false;
	}
	
	/*
	 * Method to code/test
	 */
	
	public void setSpeedSet(int speedSet) throws IncorrectSpeedSetException, SpeedSetAboveSpeedLimitException {
		if (speedSet <= 0) {
			throw new IncorrectSpeedSetException();
		} else {
			if (this.speedLimit != null) {
				if (speedSet > this.speedLimit) {
					throw new SpeedSetAboveSpeedLimitException();
				} else {
					this.speedSet = speedSet;
					this.enabled = true;
				}
			} else {
				this.speedSet = speedSet;
				this.enabled = true;
			}
		}
	}
	
	/* 
	 * Method to code/test
	 */
	
	public void setSpeedLimit(int speedLimit) throws IncorrectSpeedLimitException, CannotSetSpeedLimitException {
		if (speedLimit <= 0) {
			throw new IncorrectSpeedLimitException();
		} else {
			if (this.speedSet != null) {
				throw new CannotSetSpeedLimitException();
			} else {
				this.speedLimit = speedLimit;
			}
		}
	}
	
	/* 
	 * Method to code/test
	 */
	
	public void disable() {
		this.enabled = false;
		this.speedSet = null;
	}

	/*
	 * Method to code/test
	 */
	public Response nextCommand() {
		Response response = new Response();
		
		if (speedSet == null) {
			response.command = Command.IDLE;
		} else if (enabled == false) {
			response.command = Command.IDLE;
		} else {
			int currentSpeed = speedometer.getCurrentSpeed();
			int maxSpeed = roadInformation.getMaxSpeed();
			int minSpeed = roadInformation.getMinSpeed();
			
			if (currentSpeed < minSpeed) {
				response.command = Command.INCREASE;
			} else {
				if (currentSpeed > maxSpeed) {
					response.command = Command.REDUCE;
				} else {
					if (currentSpeed > speedSet) {
						response.command = Command.REDUCE;
					} else {
						if (currentSpeed < speedSet) {
							response.command = Command.INCREASE;
						} else {
							if (currentSpeed == speedSet) {
								response.command = Command.KEEP;
							}
						}
					}
				}
			}
		}
		
		return response;
	}
	
	/* 
	 * Others getters and setters
	 */
	
	public boolean isEnabled() {
		return enabled;
	}

	public Integer getSpeedLimit() {
		return speedLimit;
	}

	public Integer getSpeedSet() {
		return speedSet;
	}

}
