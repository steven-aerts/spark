/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.org.apache.spark.sql;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.test.TestSparkSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;

/**
 * Suite to test Java record encoding and decoding.
 */
public class JavaRecordEncoderSuite implements Serializable {

  private transient SparkSession spark;

  // Simple Java record
  public record Person(String name, int age) {}

  // Nested Java record
  public record Employee(String id, Person person, double salary) {}

  // Record with arrays
  public record Team(String name, Person[] members) {}

  @BeforeEach
  public void setUp() {
    spark = new TestSparkSession();
  }

  @AfterEach
  public void tearDown() {
    spark.stop();
    spark = null;
  }

  @Test
  public void testSimpleRecordEncoding() {
    List<Person> data = List.of(
      new Person("Alice", 25),
      new Person("Bob", 30),
      new Person("Charlie", 35)
    );

    Dataset<Person> ds = spark.createDataset(data, Encoders.bean(Person.class));
    List<Person> result = ds.collectAsList();

    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals("Alice", result.get(0).name());
    Assertions.assertEquals(25, result.get(0).age());
    Assertions.assertEquals("Bob", result.get(1).name());
    Assertions.assertEquals(30, result.get(1).age());
  }

  @Test
  public void testNestedRecordEncoding() {
    List<Employee> data = List.of(
      new Employee("E001", new Person("Alice", 25), 50000.0),
      new Employee("E002", new Person("Bob", 30), 60000.0)
    );

    Dataset<Employee> ds = spark.createDataset(data, Encoders.bean(Employee.class));
    List<Employee> result = ds.collectAsList();

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("E001", result.get(0).id());
    Assertions.assertEquals("Alice", result.get(0).person().name());
    Assertions.assertEquals(25, result.get(0).person().age());
    Assertions.assertEquals(50000.0, result.get(0).salary(), 0.01);
  }

  @Test
  public void testRecordWithArrays() {
    Team team = new Team("Engineering", new Person[] {
      new Person("Alice", 25),
      new Person("Bob", 30)
    });

    List<Team> data = List.of(team);
    Dataset<Team> ds = spark.createDataset(data, Encoders.bean(Team.class));
    List<Team> result = ds.collectAsList();

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("Engineering", result.get(0).name());
    Assertions.assertEquals(2, result.get(0).members().length);
    Assertions.assertEquals("Alice", result.get(0).members()[0].name());
  }

  @Test
  public void testRecordMap() {
    List<Person> data = List.of(
      new Person("Alice", 25),
      new Person("Bob", 30)
    );

    Dataset<Person> ds = spark.createDataset(data, Encoders.bean(Person.class));
    Dataset<String> names = ds.map(
      (MapFunction<Person, String>) Person::name,
      Encoders.STRING()
    );

    List<String> result = names.collectAsList();
    Assertions.assertEquals(List.of("Alice", "Bob"), result);
  }
}
