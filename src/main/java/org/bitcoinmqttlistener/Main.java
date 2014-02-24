package org.bitcoinmqttlistener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class Main {
	DefaultMqttPahoClientFactory factory;
	MqttClient client = null;
	MyListener listener = new MyListener();

    
	public Main() throws Exception {
		factory = new DefaultMqttPahoClientFactory();
		factory.setPassword("guest");
		factory.setUserName("foobar");
		subscribe();
	}
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
	
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Messages Received ==> "+listener.getCounter();
    }
    
	public void subscribe() throws MqttException {
		client = factory.getClientInstance("tcp://localhost:1883", "foobar");
		client.connect();

		client.setCallback(listener);
		client.subscribe("xd.mqtt.test");
		System.out.println("SUBSCRIBED");

	}

	/**
	 * Callback method for all MQTT Messages.
	 * @author renfrg
	 *
	 */
	
	public class MyListener implements MqttCallback {

		int counter = 0;
		
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
			counter++;
			System.out.println(new String(arg1.getPayload()));
		}
		public int getCounter(){
			return counter;
		}

	}

}
