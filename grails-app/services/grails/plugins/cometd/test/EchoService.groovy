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

package grails.plugins.cometd.test

import org.cometd.bayeux.server.ServerChannel

import org.springframework.beans.factory.InitializingBean

class EchoService implements InitializingBean
{
    def bayeux
    
    void afterPropertiesSet()
    {
        assert bayeux : 'bayeux object must be set'
        def localSession = bayeux.newLocalSession('echo')
        localSession.handshake()
        def serverSession = localSession.getServerSession()
        def channel = bayeux.getChannel('/echo', true)
        channel.addListener({ from, chan, msg ->
            if (from != localSession.getServerSession()) {
                from.deliver serverSession, chan.id, [echo: msg.data.msg], null
            }
            true
        } as ServerChannel.MessageListener)
    }
}
