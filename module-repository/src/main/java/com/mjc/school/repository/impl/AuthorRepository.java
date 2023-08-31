package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.utils.JPAUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class AuthorRepository implements BaseRepository<Author, Long> {
    private final JPAUtils jpaUtils;

    public AuthorRepository(JPAUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    @Override
    public List<Author> readAll() {
        AtomicReference<List<Author>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                resultList.set(
                        session.createQuery("select a from Author a", Author.class).getResultList()
                ));
        return resultList.get();
    }

    @Override
    public List<Author> readAll(Integer pageNumber, Integer pageSize, String sortBy) {
        AtomicReference<List<Author>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            StringBuilder queryString = new StringBuilder("select a from Author a");
            if (sortBy != null)
                queryString.append(" order by ").append(sortBy);
            if (pageNumber != null) {
                resultList.set(
                        session.createQuery(queryString.toString(), Author.class)
                                .setFirstResult((pageNumber - 1) * pageSize)
                                .setMaxResults(pageSize)
                                .getResultList());
            } else {
                resultList.set(
                        session.createQuery("select a from Author a", Author.class).getResultList());
            }
        });
        return resultList.get();
    }

    @Override
    public Optional<Author> readById(Long id) {
        if (id == null)
            return Optional.empty();
        AtomicReference<Optional<Author>> result = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                result.set(Optional.ofNullable(session.find(Author.class, id))));
        return result.get();
    }

    @Override
    public Author create(Author newAuthor) {
        jpaUtils.doInSessionWithTransaction(session -> session.persist(newAuthor));
        return newAuthor;
    }

    @Override
    public Author update(Author author) {
        jpaUtils.doInSessionWithTransaction(session ->
                session.createQuery("update Author a set " +
                                "a.name = :newName, " +
                                "a.lastUpdateDate = CURRENT_TIMESTAMP " +
                                "where a.id = :id")
                        .setParameter("newName", author.getName())
                        .setParameter("id", author.getId())
                        .executeUpdate());
        return readById(author.getId()).get();
    }

    @Override
    public boolean deleteById(Long id) {
        AtomicInteger numDeleted = new AtomicInteger();

        jpaUtils.doInSessionWithTransaction(session -> {
            for (News news : session.getReference(Author.class, id).getNews()) {
                news.setAuthor(null);
            }
        });

        jpaUtils.doInSessionWithTransaction(session ->
                numDeleted.set(session.createQuery("delete from Author where id = :id")
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
