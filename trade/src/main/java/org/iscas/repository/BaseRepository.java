package org.iscas.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by andyren on 2016/8/27.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

    Optional<T> findOne(ID id);

    <S extends T> Optional<S> save(S entity);

    <S extends T> Iterable<S> save(Iterable<S> entities);

    boolean exists(ID id);

    List<T> findAll();

    List<T> findAll(Iterable<ID> ids);

    long count();

    void delete(ID id);

    void delete(T entity);

    void delete(Iterable<? extends T> entities);

    void deleteAll();
}
