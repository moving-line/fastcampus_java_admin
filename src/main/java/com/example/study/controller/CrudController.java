package com.example.study.controller;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.network.Header;
import org.springframework.web.bind.annotation.*;

public abstract class CrudController<Request, Response> implements CrudInterface<Request, Response> {

    protected CrudInterface<Request, Response> baseService;

    @Override
    @PostMapping("")
    public Header<Response> create(@RequestBody Header<Request> request) {
        return baseService.create(request);
    }

    @Override
    @GetMapping("{id}")
    public Header<Response> read(@PathVariable long id) {
        return baseService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<Response> update(@RequestBody Header<Request> request) {
        return baseService.update(request);
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable long id) {
        return baseService.delete(id);
    }
}
