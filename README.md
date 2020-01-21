# Spring-cloud-gateway-RetryFilterBug
This repository is reproducing the bug of Retry filter in spring cloud gateway

## Build and Run
- Build project:
```
./gradlew clean build
```
- Run application via docker-compose:
    - default(with console logs):
    ```
    docker-compose --build
    ```
    - silent mode:
    ```
    docker-compose up -d --remove-orphan --build
    ```
## Reproducing bug
Configuration contains only 1 route: /canary. Retry filter configuration for the route:
```yaml
- name: Retry
  args:
    retries: 3
    statuses: BAD_REQUEST
    backoff:
      firstBackoff: 1000ms
      maxBackoff: 1000ms
      factor: 2
      basedOnPreviousValue: false
```
Now, execute the following command
```
curl http://localhost:8080/canary?code=400
```
You'll see that the filter is working on specified status.
Next execute 
```
curl http://localhost:8080/canary?code=500
```
You'll see that filter is working, but the series is not specified in configuration.
The only solution is to specify series as `null`:
```yaml
- name: Retry
  args:
    retries: 3
    statuses: BAD_REQUEST
    series: null
    backoff:
      firstBackoff: 1000ms
      maxBackoff: 1000ms
      factor: 2
      basedOnPreviousValue: false
```
## The reason of the bug
`RetryGatewayFilterFactory` is configured so, that if the response code is not the code that specified in configuration 
and it not null, the factory will try to retry by series.
```java
if (!retryableStatusCode && statusCode != null) 
    retryableStatusCode = retryConfig.getSeries().stream()
	.anyMatch(series -> statusCode.series().equals(series));
}
```
If the series is not specified in configuration it will be trying the default series which is `Series.SERVER_ERROR` (5xx).
```java
public static class RetryConfig implements HasRouteId {

    /*other fields*/

    private List<Series> series = toList(Series.SERVER_ERROR);

    /*other code*/
}
```

So even the series is not specified in configuration the filter will always check the series.

## Possible solution
I think the better way to fix the bug is to initialize `series` with empty list.
```java
public static class RetryConfig implements HasRouteId {

    /*other fields*/

    private List<Series> series = Collections.emptyList();

    /*other code*/
}
```

In that case the filter will not check series that was not specified by user but only the status.
