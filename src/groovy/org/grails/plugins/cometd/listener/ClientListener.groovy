package org.grails.plugins.cometd.listener

import org.cometd.ClientBayeuxListener
import org.cometd.Client
import org.cometd.MessageListener
import org.cometd.Message

public class ClientListener implements ClientBayeuxListener, MessageListener.Asynchronous {

  public void clientAdded(Client client) {
    println "clientAdded() - client.id: ${client.id}"
  }

  public void clientRemoved(Client client) {
    println "clientRemoved() - client.id: ${client.id}"
  }

  public void deliver(Client client, Client client1, Message message) {
    println "deliver() - message.clientId: ${message.clientId}"
  }

}