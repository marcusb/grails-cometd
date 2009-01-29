package org.grails.plugins.cometd

import org.cometd.ClientBayeuxListener
import org.cometd.MessageListener
import org.cometd.Client
import org.cometd.Message
import java.util.concurrent.ConcurrentHashMap
import org.cometd.Bayeux

class CometdService implements ClientBayeuxListener, MessageListener {
  boolean transactional = false

  def clients = [:] as ConcurrentHashMap

  def getCount() { return clients.size() }

  public void clientAdded(Client client) {
    clients.put(client.id, client)
    if (log.isDebugEnabled()) log.debug("clientAdded() - client.id: ${client.id}, count: ${count}")
  }

  public void clientRemoved(Client client) {
    clients.remove(client.id)
    if (log.isDebugEnabled()) log.debug("clientRemoved() - client.id: ${client.id}, count: ${count}")
  }

  public void deliver(Client from, Client to, Message message) {
    if (message.get(Bayeux.CHANNEL_FIELD) in ['/meta/handshake', '/meta/connect', '/meta/disconnect'])
      println "deliver() - message.clientId: ${message.clientId}"
    if (log.isDebugEnabled()) log.debug("deliver() - message.clientId: ${message.clientId}, message: ${message}")
  }
}
