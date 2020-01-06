package com.example.study.ifs;

import com.example.study.model.network.Header;

public interface CrudInterface<Request, Response> {

    Header<Response> create(Header<Request> request);

    Header<Response> read(long id);

    Header<Response> update(Header<Request> request);

    Header delete(long id);
}
