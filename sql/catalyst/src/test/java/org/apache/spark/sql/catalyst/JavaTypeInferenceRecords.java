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

package org.apache.spark.sql.catalyst;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test records for Java record encoder support.
 */
public class JavaTypeInferenceRecords {

  // Simple record with basic types
  public record SimpleRecord(String name, int age) {}

  // Record with BigInteger
  public record RecordWithBigInteger(BigInteger value) {}

  // Record with various primitive and boxed types
  public record LeafRecord(
      boolean primitiveBoolean,
      byte primitiveByte,
      short primitiveShort,
      int primitiveInt,
      long primitiveLong,
      float primitiveFloat,
      double primitiveDouble,
      Boolean boxedBoolean,
      Byte boxedByte,
      Short boxedShort,
      Integer boxedInt,
      Long boxedLong,
      Float boxedFloat,
      Double boxedDouble,
      String string,
      byte[] binary,
      BigDecimal bigDecimal,
      BigInteger bigInteger,
      java.time.LocalDate localDate,
      java.sql.Date date,
      java.time.Instant instant,
      java.sql.Timestamp timestamp,
      java.time.LocalDateTime localDateTime,
      java.time.Duration duration,
      java.time.Period period,
      java.time.Month monthEnum) {}

  // Record with nested record
  public record NestedRecord(String id, SimpleRecord inner) {}

  // Record with arrays
  public record ArrayRecord(
      int[] primitiveIntArray,
      String[] stringArray,
      SimpleRecord[] recordArray) {}

  // Generic record base
  public record GenericRecord<T>(T value) {}

  // Record extending generic record (simulated with composition since records can't extend)
  public record StringGenericRecord(GenericRecord<String> wrapped) {}

  // Record with @Nonnull annotation
  public record NonNullRecord(@javax.annotation.Nonnull String name, Integer age) {}
}
