import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;

import lejos.hardware.Bluetooth;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;

import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;

public class EV3Recieve { // This is the receiver

	public static void main(String[] args) throws Exception {
	
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.C, 43.2).offset(-72);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.B, 43.2).offset(72);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL); 
		MovePilot pilot = new MovePilot(chassis);
		
		NXTCommConnector connector =
		Bluetooth.getNXTCommConnector();
		
		System.out.println("Waiting for connection ...");
		NXTConnection con =
		connector.waitForConnection(0, NXTConnection.RAW);
		System.out.println("Connected");
		
		DataInputStream dis = con.openDataInputStream();
		DataOutputStream dos =
		con.openDataOutputStream();
		
		byte[] n = new byte[4];
		
//			float[] fl = new float[2];
		
		while(Button.getButtons() != Button.ID_ESCAPE ){
			
			try{
//					if (dis.readFloat() == -1)
				if (dis.read(n) == -1)
				break;
			} 
			
			catch (EOFException e){
					System.out.println("Error" + e);
				break;
			}
			
			if (n[0] == 1) {
				pilot.rotate(-25);
			}
			if (n[1] == 1) {
				pilot.rotate(25);
			}
			if (n[2] == 1) {
				pilot.forward();
			}
			if (n[3] == 1) {
				pilot.backward();
			}
			
//				fl[0] = dis.readFloat();
//				fl[1] = dis.readFloat();
			
//			System.out.println("Right" + n[0] + "Left" + n[1] +
//					"Forward" + n[2]+ "Back" + n[3]);

			dos.write(n);
//				dos.writeFloat(fl[0]);
			dos.flush();
			
		}
		
		Delay.msDelay(1000);
		dis.close();
		dos.close();
		con.close();
		
	}
}