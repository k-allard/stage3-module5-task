package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.repository.utils.JPAUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class TagRepository implements BaseRepository<Tag, Long> {

    private final JPAUtils jpaUtils;

    public TagRepository(JPAUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    @Override
    public List<Tag> readAll() {
        AtomicReference<List<Tag>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                resultList.set(
                        session.createQuery("select n from Tag n", Tag.class).getResultList()
                ));
        return resultList.get();
    }

    @Override
    public List<Tag> readAll(Integer pageNumber, Integer pageSize, String sortBy) {
        AtomicReference<List<Tag>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            StringBuilder queryString = new StringBuilder("select n from Tag n");
            if (sortBy != null)
                queryString.append(" order by ").append(sortBy);
            if (pageNumber != null) {
                resultList.set(
                        session.createQuery(queryString.toString(), Tag.class)
                                .setFirstResult((pageNumber - 1) * pageSize)
                                .setMaxResults(pageSize)
                                .getResultList());
            } else {
                resultList.set(
                        session.createQuery("select n from Tag n", Tag.class).getResultList());
            }
        });
        return resultList.get();
    }

    @Override
    public Optional<Tag> readById(Long id) {
        AtomicReference<Optional<Tag>> result = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                result.set(Optional.ofNullable(session.find(Tag.class, id))));
        return result.get();
    }

    @Override
    public Tag create(Tag newNews) {
        jpaUtils.doInSessionWithTransaction(session -> session.persist(newNews));
        return newNews;
    }

    @Override
    public Tag update(Tag news) {
        jpaUtils.doInSessionWithTransaction(session ->
                session.createQuery("update Tag n set " +
                                "n.name = :newName where n.id = :id")
                        .setParameter("newName", news.getName())
                        .setParameter("id", news.getId())
                        .executeUpdate());
        return readById(news.getId()).get();
    }

    @Override
    public boolean deleteById(Long id) {
        AtomicInteger numDeleted = new AtomicInteger();
        jpaUtils.doInSessionWithTransaction(session ->
                numDeleted.set(session.createQuery("delete from Tag where id = :id")
                        .setParameter("id", id)
                        .executeUpdate())
        );
        return numDeleted.get() == 1;
    }

    @Override
    public boolean existById(Long id) {
        return readById(id).isPresent();
    }
}
