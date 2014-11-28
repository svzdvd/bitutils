# bitutils

[![Build Status](https://travis-ci.org/svzdvd/bitutils.png)](https://travis-ci.org/svzdvd/bitutils)

The BitUtils project provides bit packing utilities to serialize data in units of 1, 2 or 4 *bits*.

##### Serialize boolean values using 1 bit for every value

```java
File dataFile = new File("booleans.bin");
boolean[] values = new boolean[] { true, true, false, true, false, false, false, true, false, true };

// Writing
Buffer1BitWriter writer = new Buffer1BitWriter(dataFile, values.length);
try {
  for (int i = 0; i < values.length; i++) {
    writer.write(values[i] ? 1 : 0);
  }
} finally {
  writer.close();
}

// Reading
Buffer1BitReader reader = new Buffer1BitReader(dataFile);
try {
  // Read next value
  boolean value0 = reader.read() == 1;
  
  // Read a value at the specified position
  boolean value4 = reader.read(4) == 1;
} finally {
  reader.close();
}
```

##### Serialize integer values from 0 to 3 (inclusive) using 2 bit for every value
