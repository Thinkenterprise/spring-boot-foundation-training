package com.thinkenterprise;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.thinkenterprise.domain.tracking.FlightStatus;
import com.thinkenterprise.domain.tracking.Tracking;
import com.thinkenterprise.message.jms.JmsReceiver;
import com.thinkenterprise.message.jms.JmsSender;

@SpringBootTest
@ActiveProfiles("test")
public class JmsMessageTest {
	
	@Autowired
	private JmsSender jmsSender;
	
	@Autowired
	private JmsReceiver jmsReceiver;
	
	@Test
	public void testJmsSendeAndReceiver() throws InterruptedException {
		
		Tracking tracking = new Tracking();
        tracking.setRouteId(40L);
        tracking.setFlightNumber("LH7902");
        tracking.setCurrentTime(LocalDateTime.now());
        tracking.setStatus(FlightStatus.DELAYED);
		
		boolean received = false;

		jmsSender.sendMessage(tracking);
		
		tracking= jmsReceiver.getTracking();
		
		for(int i =0; i < 3; ++i) {
			if(jmsReceiver.getTracking()!=null) {
				received=true;
				break;
			}
			Thread.sleep(1000L);
		}
		
		Assertions.assertTrue(received);
		
	}

}
