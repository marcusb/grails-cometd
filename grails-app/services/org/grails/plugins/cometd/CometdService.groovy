package org.grails.plugins.cometd

import org.cometd.ClientBayeuxListener
import org.cometd.MessageListener
import org.cometd.Client
import org.cometd.Message
import java.util.concurrent.ConcurrentHashMap
import org.cometd.Bayeux
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CometdService implements ClientBayeuxListener, MessageListener {
  static Logger logger = LoggerFactory.getLogger(CometdService.class);
  boolean transactional = false
  static version;
  def clients = [:] as ConcurrentHashMap

  def getCount() { return clients.size() }

  def bayeux;

  def init() {
    if (logger.isInfoEnabled()) logger.info("init()")
    bayeux.newClient(this.class.name).with {agent ->
      agent.addListener(this)
      bayeux.getChannel('/**', true).subscribe(agent)
      bayeux.addListener(this)
    }
  }

  public void clientAdded(Client client) {
    clients.put(client.id, client)
    if (logger.isDebugEnabled()) logger.debug("clientAdded() - client.id: ${client.id}, count: ${count}")
    //TODO remove all println once Grails has fixed the logger problem
    //println "clientAdded() - client.id: ${client.id}, count: ${count}"
  }

  public void clientRemoved(Client client) {
    clients.remove(client.id)
    if (logger.isDebugEnabled()) logger.debug("clientRemoved() - client.id: ${client.id}, count: ${count}")
    //println "clientRemoved() - client.id: ${client.id}, count: ${count}"
  }

  public void deliver(Client from, Client to, Message message) {
    if (message.get(Bayeux.CHANNEL_FIELD) in ['/meta/handshake', '/meta/connect', '/meta/disconnect'])
      if (logger.isDebugEnabled()) logger.debug("deliver() - message: ${message}")
    //println "deliver() - message: ${message}"
  }
}
