/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.ingest.core;

import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.test.ESTestCase;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;


public class ConfigurationUtilsTests extends ESTestCase {
    private Map<String, Object> config;

    @Before
    public void setConfig() {
        config = new HashMap<>();
        config.put("foo", "bar");
        config.put("arr", Arrays.asList("1", "2", "3"));
        List<Integer> list = new ArrayList<>();
        list.add(2);
        config.put("int", list);
        config.put("ip", "127.0.0.1");
        Map<String, Object> fizz = new HashMap<>();
        fizz.put("buzz", "hello world");
        config.put("fizz", fizz);
    }

    public void testReadStringProperty() {
        String val = ConfigurationUtils.readStringProperty(null, null, config, "foo");
        assertThat(val, equalTo("bar"));
    }

    public void testReadStringPropertyInvalidType() {
        try {
            ConfigurationUtils.readStringProperty(null, null, config, "arr");
        } catch (ElasticsearchParseException e) {
            assertThat(e.getMessage(), equalTo("[arr] property isn't a string, but of type [java.util.Arrays$ArrayList]"));
        }
    }

    // TODO(talevy): Issue with generics. This test should fail, "int" is of type List<Integer>
    public void testOptional_InvalidType() {
        List<String> val = ConfigurationUtils.readList(null, null, config, "int");
        assertThat(val, equalTo(Collections.singletonList(2)));
    }
}
