/*
 * Copyright © 2010 MBTE Sweden AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import functionaltestplugin.FunctionalTestCase

import java.util.concurrent.CountDownLatch
import static java.util.concurrent.TimeUnit.SECONDS

import org.cometd.bayeux.Channel
import org.cometd.bayeux.client.ClientSessionChannel
import org.cometd.client.BayeuxClient
import org.cometd.client.transport.LongPollingTransport

class CometTests extends FunctionalTestCase
{
    def client
    
    void setUp()
    {
        super.setUp()
        client = new BayeuxClient(makeRequestURL(null, '/cometd').toString(), LongPollingTransport.create(null))
    }

    void testHandshake()
    {
        def connectedLatch = new CountDownLatch(1)
        client.getChannel(Channel.META_HANDSHAKE).addListener({ channel, message ->
            connectedLatch.countDown()
        } as ClientSessionChannel.MessageListener)
        client.handshake()
        assert connectedLatch.await(1, SECONDS) : 'handshake timeout'
    }
    
    // Simple echo service test to make sure that the bayeux bean was hooked up correctly to the CometdServlet.
    void testEcho()
    {
        def latch = new CountDownLatch(1)
        def echoChannel = client.getChannel('/echo')
        client.getChannel(Channel.META_HANDSHAKE).addListener({ channel, message ->
            echoChannel.publish([msg: 'hello'])
        } as ClientSessionChannel.MessageListener)
        echoChannel.addListener({ channel, message ->
            if (message.data?.echo) {
                assert message.data.echo == 'hello'
                latch.countDown()
            }
        } as ClientSessionChannel.MessageListener)
        client.handshake()
        assert latch.await(2, SECONDS) : 'timeout'
    }
}
