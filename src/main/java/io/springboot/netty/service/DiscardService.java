package io.springboot.netty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiscardService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DiscardService.class);
	
	public void discard (String message) {
		LOGGER.info("丢弃消息:{}", message);
	}
}
