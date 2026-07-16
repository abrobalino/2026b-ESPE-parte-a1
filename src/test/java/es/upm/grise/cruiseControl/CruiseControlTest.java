package es.upm.grise.cruiseControl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import es.upm.grise.cruiseControl.exceptions.*;

class CruiseControlTest {

	@Test
	public void smokeTest() {}

	@Test
	void testSetSpeedSetSuccess() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(90);
		assertEquals(90, cc.getSpeedSet());
		assertTrue(cc.isEnabled());
	}

	@Test
	void testSetSpeedSetIncorrectThrowsException() {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		assertThrows(IncorrectSpeedSetException.class, () -> {
			cc.setSpeedSet(0);
		});
		assertThrows(IncorrectSpeedSetException.class, () -> {
			cc.setSpeedSet(-10);
		});
	}

	@Test
	void testSetSpeedLimitSuccess() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedLimit(110);
		assertEquals(110, cc.getSpeedLimit());
	}

	@Test
	void testSetSpeedLimitIncorrectThrowsException() {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		assertThrows(IncorrectSpeedLimitException.class, () -> {
			cc.setSpeedLimit(0);
		});
		assertThrows(IncorrectSpeedLimitException.class, () -> {
			cc.setSpeedLimit(-50);
		});
	}

	@Test
	void testSetSpeedLimitAfterSpeedSetThrowsException() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(80);
		assertThrows(CannotSetSpeedLimitException.class, () -> {
			cc.setSpeedLimit(100);
		});
	}

	@Test
	void testSetSpeedSetAboveSpeedLimitThrowsException() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedLimit(90);
		assertThrows(SpeedSetAboveSpeedLimitException.class, () -> {
			cc.setSpeedSet(100);
		});
	}

	@Test
	void testDisable() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(80);
		cc.disable();
		assertNull(cc.getSpeedSet());
		assertFalse(cc.isEnabled());
	}

	@Test
	void testNextCommandIdleWhenNotInitialized() {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		Response res = cc.nextCommand();
		assertEquals(Command.IDLE, res.command);
	}

	@Test
	void testNextCommandIdleWhenDisabled() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(80);
		cc.disable();
		Response res = cc.nextCommand();
		assertEquals(Command.IDLE, res.command);
	}

	@Test
	void testNextCommandBelowMinSpeed() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(50);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(40);
		Response res = cc.nextCommand();
		assertEquals(Command.INCREASE, res.command);
	}

	@Test
	void testNextCommandAboveMaxSpeed() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(130);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(140);
		Response res = cc.nextCommand();
		assertEquals(Command.REDUCE, res.command);
	}

	@Test
	void testNextCommandBelowSpeedSet() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(80);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(90);
		Response res = cc.nextCommand();
		assertEquals(Command.INCREASE, res.command);
	}

	@Test
	void testNextCommandAboveSpeedSet() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(100);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(90);
		Response res = cc.nextCommand();
		assertEquals(Command.REDUCE, res.command);
	}

	@Test
	void testNextCommandEqualSpeedSet() throws Exception {
		RoadInformation ri = new RoadInformation(120, 60);
		Speedometer s = new Speedometer(90);
		CruiseControl cc = new CruiseControl(ri, s);
		cc.setSpeedSet(90);
		Response res = cc.nextCommand();
		assertEquals(Command.KEEP, res.command);
	}

}
