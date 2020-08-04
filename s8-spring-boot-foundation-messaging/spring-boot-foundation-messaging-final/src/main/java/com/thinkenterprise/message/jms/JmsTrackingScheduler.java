package com.thinkenterprise.message.jms;

import java.time.LocalDateTime;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thinkenterprise.domain.tracking.FlightStatus;
import com.thinkenterprise.domain.tracking.Tracking;

@Component
@Profile("!test")
public class JmsTrackingScheduler implements ApplicationContextAware {

	  private ApplicationContext applicationContext;
	
	  @Scheduled(initialDelay = 1000, fixedDelay = 5000)
	    public void sendTracking() {
	        Tracking tracking = new Tracking();
	        tracking.setRouteId(40L);
	        tracking.setFlightNumber("LH7902");
	        tracking.setCurrentTime(LocalDateTime.now());
	        tracking.setStatus(FlightStatus.DELAYED);

	        JmsSender sender = applicationContext.getBean(JmsSender.class);
	        sender.sendMessage(tracking);
	    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
		
	}

	
}
