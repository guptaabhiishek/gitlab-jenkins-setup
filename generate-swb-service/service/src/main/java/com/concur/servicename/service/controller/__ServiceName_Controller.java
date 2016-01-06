package com.concur.servicename.service.controller;

import com.concur.servicename.api.model.v1_0.__ServiceName_Model;
import com.concur.servicename.api.resource.v1_0.__ServiceName_Resource;
import com.concur.servicename.service.component.RequestContextStashOperator;
import com.concur.servicename.service.component.ResponseMapObservableOperator;
import com.concur.servicename.service.repository.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rx.Observable;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by mtalbot on 11/08/2015.
 */
@Controller
public class __ServiceName_Controller implements __ServiceName_Resource {

    private final CrudRepository<UUID, __ServiceName_Model> repo;

    @Autowired
    public __ServiceName_Controller(CrudRepository<UUID, __ServiceName_Model> repo) {
        this.repo = repo;
    }

    private Resource<__ServiceName_Model> getResource(__ServiceName_Model model) {
        return new Resource<>(model, linkTo(methodOn(__ServiceName_Controller.class).get(model.getId())).withSelfRel());
    }

    @Override
    public Observable<ResponseEntity<Resources<Resource<__ServiceName_Model>>>> list() {
        return repo.
                findAll().
                lift(new RequestContextStashOperator<>()).
                map(this::getResource).
                toList().
                map(models -> new Resources<>(models, linkTo(methodOn(__ServiceName_Controller.class).list()).withSelfRel())).
                lift(new ResponseMapObservableOperator<>(HttpStatus.OK));
    }

    @Override
    public Observable<Collection<Link>> view() {
        return repo.
                findAll().
                lift(new RequestContextStashOperator<>()).
                map(this::getResource).
                map(Resource::getId).
                toList().
                map(links -> (Collection<Link>)links);
    }

    @Override
    public Observable<ResponseEntity<Resource<__ServiceName_Model>>> get(@PathVariable UUID id) {
        return repo.
                findById(id).
                lift(new RequestContextStashOperator<>()).
                map(this::getResource).
                lift(new ResponseMapObservableOperator<>(HttpStatus.OK));
    }

    @Override
    public Observable<ResponseEntity<Resource<__ServiceName_Model>>> save(@PathVariable UUID id, @RequestBody @Valid __ServiceName_Model entity) {
        return repo.
                save(id, entity).
                lift(new RequestContextStashOperator<>()).
                map(this::getResource).
                lift(new ResponseMapObservableOperator<>(HttpStatus.ACCEPTED));
    }

    @Override
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return repo.
                delete(id).
                map(deleted ->
                                new ResponseEntity(
                                        deleted,
                                        deleted ? HttpStatus.OK : HttpStatus.NOT_FOUND
                                )
                ).
                toBlocking().
                first();
    }
}
