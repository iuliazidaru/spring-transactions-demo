package org.mytests.nestedtransactions.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.mytests.nestedtransactions.model.Collection;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * Implementation of Contentservice which puts the information in an 
 * activeMQ queue.
 * 
 * @author Iulia
 *
 */
public class JMSProducerContentService implements ContentService{
    private JmsTemplate jmsTemplate = null;
    
    private boolean fail = false;
    
	@Override
	public Collection createCollection(final Collection c) {
		if(fail){
			throw new RuntimeException();
		}
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage objectMessage = session.createObjectMessage(c);				
				return objectMessage;
			}
		});
		return null;
	}

	@Override
	public void setFail(boolean fail) {
		this.fail = fail;
	}

	@Override
	public Collection loadCollection(Long id) {
		 Message message = jmsTemplate.receive();
		 if(message != null){
			 try {
				return (Collection)((ObjectMessage)message).getObject();
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		 }
		 return null;
	}

	public void setJmsTemplate(JmsTemplate template) {
		this.jmsTemplate = template;
	}

	
}
