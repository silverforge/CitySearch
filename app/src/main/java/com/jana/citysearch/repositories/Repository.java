package com.jana.citysearch.repositories;

import rx.Observable;

public interface Repository<T> {
    Observable<T> searchAsync(String text);
}