package com.mjc.school.service.validator;

import com.mjc.school.service.dto.ServiceAuthorRequestDto;
import com.mjc.school.service.dto.ServiceCommentRequestDto;
import com.mjc.school.service.dto.ServiceNewsRequestDto;
import com.mjc.school.service.dto.ServiceTagDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@EnableAspectJAutoProxy
public class ValidationAspect {

    private final NewsRequestDtoValidator newsValidator;

    private final AuthorRequestDtoValidator authorValidator;
    private final TagDtoValidator tagValidator;
    CommentRequestDtoValidator commentValidator;

    public ValidationAspect(NewsRequestDtoValidator newsValidator,
                            AuthorRequestDtoValidator authorValidator,
                            TagDtoValidator tagValidator,
                            CommentRequestDtoValidator commentValidator) {
        this.newsValidator = newsValidator;
        this.authorValidator = authorValidator;
        this.tagValidator = tagValidator;
        this.commentValidator = commentValidator;
    }

    @Before(value = "@annotation(com.mjc.school.service.validator.annotations.ValidateInput)")
    public void validateInput(JoinPoint joinPoint) {
        log.debug("Entered validateInput advice for method: " + joinPoint.getSignature());
        Object[] requestObject = joinPoint.getArgs();

        if (requestObject.length == 0) {
            log.error(
                    "@ValidateInput annotation should be placed on method with at least 1 parameter.\n" +
                            "No validation will be performed");
        } else if (requestObject[0] instanceof ServiceNewsRequestDto news) {
            log.debug("Started executing validateInput advice for NewsRequestDto parameter");
            newsValidator.validateNewsDTORequest(news);
            if (news.getAuthorId() != null)
                newsValidator.validateAuthorId(news.getAuthorId());
            if (news.getNewsTagsIds() != null) {
                for (Long tag : news.getNewsTagsIds()) {
                    tagValidator.validateTagId(tag);
                }
            }
            if (joinPoint.getSignature().getName().equals("update")) {
                newsValidator.validateNewsId(news.getId());
            }
        } else if (requestObject[0] instanceof Long id) {
            log.debug("Started executing validateInput advice for Long id parameter");
            String className = joinPoint.getSourceLocation().getWithinType().getTypeName();
            if (className.contains("AuthorService"))
                authorValidator.validateAuthorId(id);
            else if (className.contains("NewsService")) {
                log.debug("Invoking NewsValidator. . .");
                newsValidator.validateNewsId(id);
            } else if (className.contains("TagService"))
                tagValidator.validateTagId(id);
            else if (className.contains("CommentService"))
                commentValidator.validateCommentId(id);
            else {
                log.error("@ValidateInput does not know how to validate IDs in "
                        + className + " yet\n" + "No validation will be performed");
            }
        } else if (requestObject[0] instanceof ServiceAuthorRequestDto author) {
            log.debug("Started executing validateInput advice for AuthorRequestDto parameter");
            authorValidator.validateAuthorDTO(author);
            if (joinPoint.getSignature().getName().equals("update")) {
                authorValidator.validateAuthorId(author.getId());
            }
        } else if (requestObject[0] instanceof ServiceTagDto tag) {
            log.debug("Started executing validateInput advice for ServiceTagDto parameter");
            tagValidator.validateTagDTO(tag);
            if (joinPoint.getSignature().getName().equals("update")) {
                tagValidator.validateTagId(tag.getId());
            }
        } else if (requestObject[0] instanceof ServiceCommentRequestDto comment) {
            log.debug("Started executing validateInput advice for ServiceCommentRequestDto parameter");
            commentValidator.validateCommentDTO(comment);
            if (joinPoint.getSignature().getName().equals("update")) {
                commentValidator.validateCommentId(comment.getId());
            }
        } else {
            log.warn("@ValidateInput annotation does not support validation for parameter of "
                    + requestObject[0].getClass());
        }
        log.debug("Completed executing validateInput advice");
    }
}