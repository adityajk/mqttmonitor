package org.bitcoinmqttlistener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;


public class Main {
	DefaultMqttPahoClientFactory factory;
	MqttClient client = null;
	public Main() throws Exception {
		factory = new DefaultMqttPahoClientFactory();
		factory.setPassword("guest");
		factory.setUserName("foobar");
	}
	
	public static void main(String args[]) throws Exception{
		Main  listener = new Main();
		listener.subscribe();
	}
	public void subscribe() throws MqttException {
		client = factory.getClientInstance("tcp://localhost:1883", "foobar");
		client.connect();

		client.setCallback(new MyListener());
		client.subscribe("xd.mqtt.test");
		System.out.println("SUBSCRIBED");

	}

	public class MyListener implements MqttCallback {

		public void connectionLost(Throwable arg0) {
			System.out.println("Well you lost it " + arg0.getMessage());
			arg0.printStackTrace();

		}

		public void deliveryComplete(IMqttDeliveryToken arg0) {

			try {
				System.out.println(arg0.getMessage().getPayload());
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void messageArrived(String arg0, MqttMessage arg1)
				throws Exception {
			System.out.println(new String(arg1.getPayload()));

		}

	}

}
