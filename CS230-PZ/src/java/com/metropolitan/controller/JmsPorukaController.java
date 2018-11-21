/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metropolitan.controller;

import com.metropolitan.jmsmodel.JmsPorukaModel;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 *
 * @author Milos
 */
@Named
@RequestScoped
public class JmsPorukaController {

    @Inject
    private JmsPorukaModel jmsPorukaModel;
    
    @Resource(mappedName = "jms/mojRed")
    private Queue mojRed;

    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;

    public String sendMsg() {
        sendJMSMessageToMojRed(jmsPorukaModel.getMsgText());
        return "index";
    }

    private void sendJMSMessageToMojRed(String messageData) {
        context.createProducer().send(mojRed, messageData);
    }

}
