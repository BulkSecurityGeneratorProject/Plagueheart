package au.com.iglooit.searchcloud.aop.searchhistory;

import au.com.iglooit.searchcloud.config.Constants;
import au.com.iglooit.searchcloud.domain.SearchHistory;
import au.com.iglooit.searchcloud.service.SearchHistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

/**
 * Aspect for logging execution of search.
 */
@Aspect
public class SearchHistoryAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private SearchHistoryService searchHistoryService;

    @Pointcut("execution(au.com.iglooit.searchcloud.service.MemberSystemService.search(*))")
    public void loggingPointcut() {
    }

    @Around("loggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("put search args into search history");
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setQuery(Arrays.toString(joinPoint.getArgs()));
        searchHistory.setQueryDate(LocalDate.now());
        searchHistoryService.save(searchHistory);

        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }
}
