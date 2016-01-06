package com.concur.servicename.service.repository;

import com.concur.servicename.api.model.v1_0.__ServiceName_Model;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import rx.Observable;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by mtalbot on 17/08/2015.
 */
public interface CrudRepository<idType, domainType> {
    @HystrixCommand
    Observable<domainType> findAll();

    @HystrixCommand
    Observable<domainType> findById(idType id);

    @HystrixCommand
    Observable<domainType> save(idType id, domainType domain);

    @HystrixCommand
    Observable<Boolean> delete(idType id);
}
