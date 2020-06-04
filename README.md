# LiveIntent's QA Coding Challenge

The goal of this coding challenge is to have you produce automated tests that shows us in concrete 
terms how you think about QA software engineering in your professional life. We want you to use the 
languages, tools, and setup with which you feel most comfortable.

**We'll be looking at various aspects of the solution:**
* code readability
* code structure
* maintainability
* edge cases coverage
* configurable scenarios, e.g. we'd like to test this service on an actual environment using real AWS Kinesis.

## Functional requirements

For this challenge, we ask you to write a test suite that covers the routing service for a fictional 
router system. This scenario is an extremely simplified version of some of the challenges that our 
engineering teams face at LiveIntent.

The service that you are going to test exposes a HTTP - GET - endpoint on `http://localhost:9000/route/:seed`.
It routes requests to two different [kinesis stream][kinesis] according to simple rules:
* If the `seed` received in the request is odd then it ends up in the `li-stream-odd` Kinesis stream
* If the `seed` received in the request is even then it ends up in the `li-stream-even` Kinesis stream

The routing service returns 200 when a valid number is received in the seed param and a message was sent
to kinesis stream. The response body is empty and a custom header `X-Transaction-Id` was added to Response Header.

**Request / Response sample:**

```shell script
curl --location -D - --request GET 'http://localhost:9000/route/1'

HTTP/1.1 200 OK
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Frame-Options: DENY
X-Transaction-Id: 325439c2-4b4e-45f1-98ee-75bc9e14d877
X-XSS-Protection: 1; mode=block
X-Content-Type-Options: nosniff
X-Permitted-Cross-Domain-Policies: master-only
Date: Mon, 18 May 2020 11:50:23 GMT
Content-Type: text/plain; charset=UTF-8
Content-Length: 0
```

In that case, with `seed` being 1(an odd number), a message is sent to `li-stream-odd` stream.

```json
{"uuid": "325439c2-4b4e-45f1-98ee-75bc9e14d877", "seed": 1}
```

If `seed` is even, e.g. 2,4,6,8 etc, a message with same format is sent to `li-stream-even` stream.

We would like to check if messages are routing to respective streams correctly. If the seed is not a number
the services returns a bad request.

In order to do that, you will need to read messages from both Kinesis Streams(`li-stream-even` and `li-stream-odd`), and verify that the routing works correctly by asserting that the correct request produced a record in the one stream, and did not produce a record in the other.

To solve the challenge, you will need to quickly research how to read records from a Kinesis Stream, if you haven't used it already.

We recommend that you use standard AWS SDK for any programming language you choose, e.g [how to read kinesis records with Java](https://docs.aws.amazon.com/streams/latest/dev/developing-consumers-with-sdk.html).

## Running the Route Service Locally

You will need Kinesis running on port 4568 and the routing service running on port 9000. We are providing a
docker compose file that manages the start / link of these two containers. You should run only:

```shell script
docker-compose up -d
```

To shutdown, run:

```shell script
docker-compose down
```

[kinesis]: https://aws.amazon.com/kinesis/data-streams/
