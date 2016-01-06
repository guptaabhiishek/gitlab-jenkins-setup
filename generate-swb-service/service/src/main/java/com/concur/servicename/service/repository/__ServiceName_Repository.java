package com.concur.servicename.service.repository;

import com.concur.servicename.api.model.v1_0.__ServiceName_Model;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import rx.Observable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Created by mtalbot on 17/08/2015.
 */
@Repository
public class __ServiceName_Repository implements CrudRepository<UUID, __ServiceName_Model> {

    private final Map<UUID, __ServiceName_Model> data = LongStream.
            range(1, 20).
            mapToObj(c -> new __ServiceName_Model(UUID.randomUUID(), "Test" + Long.toString(c), DateTime.now(), c)).
            collect(Collectors.toMap(__ServiceName_Model::getId, model -> model));

    private <result> HystrixObservableCommand<result> getCommand(String commandType, Supplier<Observable<result>> generate) {
        return new HystrixObservableCommand<result>(
                HystrixObservableCommand.
                        Setter.
                        withGroupKey(
                                HystrixCommandGroupKey.
                                        Factory.
                                        asKey(this.getClass().getSimpleName())
                        ).andCommandKey(
                        HystrixCommandKey.
                                Factory.
                                asKey(commandType)
                ).andCommandPropertiesDefaults(HystrixCommandProperties.
                                Setter().
                                withCircuitBreakerEnabled(true).
                                withCircuitBreakerErrorThresholdPercentage(20).
                                withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE).
                                withExecutionIsolationSemaphoreMaxConcurrentRequests(1000000)
                )
        ) {
            @Override
            protected Observable<result> construct() {
                return generate.get();
            }
        };
    }

    @Override
    public Observable<__ServiceName_Model> findAll() {
        return getCommand("findAll", () -> Observable.from(data.values())).observe();
    }

    @Override
    public Observable<__ServiceName_Model> findById(UUID id) {
        return getCommand("findById", () -> data.containsKey(id) ?
                        Observable.just(data.get(id)) : Observable.empty()
        ).observe();
    }

    @Override
    public Observable<__ServiceName_Model> save(UUID id, __ServiceName_Model domain) {
        return getCommand("save", () -> {
                    data.put(id, domain);

                    return Observable.just(data.get(id));
                }
        ).observe();
    }

    @Override
    public Observable<Boolean> delete(UUID id) {
        return getCommand("delete", () -> {
                    if (data.containsKey(id)) {
                        data.remove(id);

                        return Observable.just(true);
                    } else {
                        return Observable.just(false);
                    }
                }
        ).observe();
    }
}
