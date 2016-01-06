package com.concur.servicename.api.resource.v1_0;

import com.concur.servicename.api.model.v1_0.__ServiceName_Model;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rx.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by mtalbot on 11/08/2015.
 */
@RequestMapping("/__ServiceName_/v1.0/")
@ExposesResourceFor(__ServiceName_Model.class)
@RestController
public interface __ServiceName_Resource {

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(code = 200, value = "list__ServiceName_s")
    Observable<ResponseEntity<Resources<Resource<__ServiceName_Model>>>> list();

    @RequestMapping(value = "/view", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(code = 200, value = "view__ServiceName_s")
    Observable<Collection<Link>> view();

    @RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(code = 200, value = "get__ServiceName_")
    Observable<ResponseEntity<Resource<__ServiceName_Model>>> get(@PathVariable("id") UUID id);

    @RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    @ApiOperation(code = 202, value = "save__ServiceName_")
    Observable<ResponseEntity<Resource<__ServiceName_Model>>> save(@PathVariable("id") UUID id, @RequestBody @Valid __ServiceName_Model entity);

    @ApiOperation(code = 200, value = "delete__ServiceName_")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Boolean> delete(@PathVariable("id") UUID id);
}
