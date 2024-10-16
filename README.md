# spring-data-mongodb-4812
Minimal reproducible example for Spring Data MongoDB issue #4812

## Minimal conversion example

See [AggregationContextTest](src/test/java/com/github/gbaso/springdatamongodb4812/AggregationContextTest.java) for a minimal sample to reproduce the issue. The test parses a document and then converts it using `AggregationContext#getMappedObject`. The test fails, due to the `args` array being converted from empty array to `null`.

## Realistic example

See [AggregatorIT](src/test/java/com/github/gbaso/springdatamongodb4812/AggregatorIT.java) for a real life example. The class under test creates an aggregation pipeline via the Aggregation API, which works correctly, and via parsing a json to create a pre-rendered `Document`. The latter fails when sent to MongoDB.

A workaround is provided where an unused argument is specified, preventing the conversion to `null`.
