/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.examples.spring.clustering.hazelcast;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * @author Thomas Segismont
 */
public class HelloVerticle extends AbstractVerticle {
	private static final String ID = UUID.randomUUID().toString();

	private final  HazelcastInstance hazelcastInstance;

	HelloVerticle(HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("helloVerticle id" + ID);
		vertx.eventBus().<String>consumer("hello", message -> {
			IQueue<String> names = hazelcastInstance.getQueue("name");
			String name = message.body();
			names.add(name);

			Iterator ss = names.iterator();
			System.out.println("<<<<<<<<<<<<<<-----------------");
			while (ss.hasNext()) {
				String pname = (String) ss.next();
				System.out.println("现在有:" + pname);
			}
			System.out.println("----------------->>>>>>>>>>>>>>");
			message.reply("Hello " + name + " from " + ID);

		}).completionHandler(startFuture.completer());
	}

}
