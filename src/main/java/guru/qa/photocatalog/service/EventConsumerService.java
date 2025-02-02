package guru.qa.photocatalog.service;

import guru.qa.photocatalog.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EventConsumerService {

  private static final Logger LOG = LoggerFactory.getLogger(EventConsumerService.class);

  @KafkaListener(topics = "events", groupId = "photocatalog-consumer")
  public void listener(@Payload Event event) {
    LOG.info("### Event received: " + event);
  }
}
