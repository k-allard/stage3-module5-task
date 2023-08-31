package com.mjc.school.repository.impl;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.ExtendedRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.Comment;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.repository.utils.JPAUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class NewsRepository implements BaseRepository<News, Long>, ExtendedRepository {

    private final JPAUtils jpaUtils;

    public NewsRepository(JPAUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    @Override
    public List<News> readAll() {
        AtomicReference<List<News>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                resultList.set(
                        session.createQuery("select n from News n", News.class).getResultList()
                ));
        return resultList.get();
    }

    @Override
    public List<News> readAll(Integer pageNumber, Integer pageSize, String sortBy) {
        AtomicReference<List<News>> resultList = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            StringBuilder queryString = new StringBuilder("select n from News n");
            if (sortBy != null)
                queryString.append(" order by ").append(sortBy);
            if (pageNumber != null) {
                resultList.set(
                        session.createQuery(queryString.toString(), News.class)
                                .setFirstResult((pageNumber - 1) * pageSize)
                                .setMaxResults(pageSize)
                                .getResultList());
            } else {
                resultList.set(
                        session.createQuery("select n from News n", News.class).getResultList());
            }
        });
        return resultList.get();
    }

    @Override
    public Optional<News> readById(Long id) {
        AtomicReference<Optional<News>> result = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session ->
                result.set(Optional.ofNullable(session.find(News.class, id))));
        return result.get();
    }

    @Override
    public News create(News newNews) {
        jpaUtils.doInSessionWithTransaction(session -> {
            if (newNews.getAuthor() != null)
                newNews.setAuthor(session.merge(newNews.getAuthor()));
            if (newNews.getNewsTags() != null) {
                List<Tag> newTags = new ArrayList<>();
                for (Tag tag : newNews.getNewsTags()) {
                    newTags.add(session.merge(tag));
                }
                newNews.setNewsTags(newTags);
            }
            session.persist(newNews);
        });
        return newNews;
    }

    @Override
    public News update(News news) {
        AtomicReference<News> newsAtomicReference = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            News newsFromRepo = session.find(News.class, news.getId());
            if (news.getTitle() != null)
                newsFromRepo.setTitle(news.getTitle());
            if (news.getContent() != null)
                newsFromRepo.setContent(news.getContent());
            if (news.getAuthor() != null)
                newsFromRepo.setAuthor(session.merge(news.getAuthor()));
            if (news.getNewsTags() != null) {
                List<Tag> newTags = new ArrayList<>();
                for (Tag tag : news.getNewsTags()) {
                    newTags.add(session.merge(tag));
                }
                newsFromRepo.setNewsTags(newTags);
            }
            newsFromRepo.setLastUpdateDate(LocalDateTime.now());
            newsAtomicReference.set(newsFromRepo);
        });
        return newsAtomicReference.get();
    }

    @Override
    public boolean deleteById(Long id) {
        AtomicInteger numDeleted = new AtomicInteger();
        jpaUtils.doInSessionWithTransaction(session ->
                numDeleted.set(session.createQuery("delete from News where id = :id")
                        .setParameter("id", id)
                        .executeUpdate())
        );
        return numDeleted.get() == 1;
    }

    @Override
    public boolean existById(Long id) {
        return readById(id).isPresent();
    }

    @Override
    public List<News> readNewsByParams(List<Long> tagsIds,
                                       String tagName,
                                       String authorName,
                                       String title,
                                       String content) {
        AtomicReference<List<News>> result = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
            Root<News> root = criteriaQuery.from(News.class);

            List<Predicate> predicates = new ArrayList<>();

            if (tagName != null || tagsIds != null) {
                Join<Tag, News> newsTags = root.join("newsTags");
                if (tagName != null) {
                    predicates.add(newsTags.get("name").in(tagName));
                }
                if (tagsIds != null) {
                    predicates.add(newsTags.get("id").in(tagsIds));
                }
            }
            if (authorName != null) {
                Join<Author, News> newsAuthor = root.join("author");
                predicates.add(criteriaBuilder.equal(newsAuthor.get("name"), authorName));
            }
            if (title != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (content != null) {
                predicates.add(criteriaBuilder.like(root.get("content"), "%" + content + "%"));
            }
            criteriaQuery.select(root).distinct(true).where(predicates.toArray(new Predicate[0]));
            result.set(session.createQuery(criteriaQuery).getResultList());
        });
        return result.get();
    }

    @Override
    public List<Tag> getTagsByNewsId(Long newsId) {
        AtomicReference<List<Tag>> result = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            List<Tag> newsTags = session.find(News.class, newsId).getNewsTags();
            newsTags.size();
            result.set(newsTags);
        });
        return result.get();
    }

    @Override
    public List<Comment> getCommentsByNewsId(Long newsId) {
        AtomicReference<List<Comment>> result = new AtomicReference<>();
        jpaUtils.doInSessionWithTransaction(session -> {
            List<Comment> comments = session.find(News.class, newsId).getComments();
            comments.size();
            result.set(comments);
        });
        return result.get();
    }
}
