package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.utils.JPAUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class CommentRepository implements BaseRepository<Comment, Long> {
    private final JPAUtils jpaUtils;

    public CommentRepository(JPAUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    @Override
    public List<Comment> readAll() {
        AtomicReference<List<Comment>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                resultList.set(
                        session.createQuery("select c from Comment c", Comment.class).getResultList()
                ));
        return resultList.get();
    }

    @Override
    public List<Comment> readAll(Integer pageNumber, Integer pageSize, String sortBy) {
        AtomicReference<List<Comment>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            StringBuilder queryString = new StringBuilder("select c from Comment c");
            if (sortBy != null)
                queryString.append(" order by ").append(sortBy);
            if (pageNumber != null) {
                resultList.set(
                        session.createQuery(queryString.toString(), Comment.class)
                                .setFirstResult((pageNumber - 1) * pageSize)
                                .setMaxResults(pageSize)
                                .getResultList());
            } else {
                resultList.set(
                        session.createQuery("select c from Comment c", Comment.class).getResultList());
            }
        });
        return resultList.get();
    }

    @Override
    public Optional<Comment> readById(Long id) {
        if (id == null)
            return Optional.empty();
        AtomicReference<Optional<Comment>> result = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                result.set(Optional.ofNullable(session.find(Comment.class, id))));
        return result.get();
    }

    @Override
    public Comment create(Comment newComment) {
        jpaUtils.doInSessionWithTransaction(session -> {
            if (newComment.getNews() != null)
                newComment.setNews(session.merge(newComment.getNews()));
            session.persist(newComment);
        });
        return newComment;
    }

    @Override
    public Comment update(Comment comment) {
        AtomicReference<Comment> commentAtomicReference = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            Comment commentFromRepo = session.find(Comment.class, comment.getId());
            commentFromRepo.setContent(comment.getContent());
            commentFromRepo.setLastUpdateDate(LocalDateTime.now());
            commentAtomicReference.set(commentFromRepo);
        });
        return commentAtomicReference.get();
    }

    @Override
    public boolean deleteById(Long id) {
        AtomicInteger numDeleted = new AtomicInteger();

        jpaUtils.doInSessionWithTransaction(session ->
                session.getReference(Comment.class, id).getNews().getComments().remove(new Comment(id))
        );

        jpaUtils.doInSessionWithTransaction(session ->
                numDeleted.set(session.createQuery("delete from Comment where id = :id")
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
