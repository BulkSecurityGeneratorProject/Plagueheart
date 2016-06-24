package au.com.iglooit.searchcloud.config;

import au.com.iglooit.searchcloud.aop.logging.LoggingAspect;
import au.com.iglooit.searchcloud.aop.searchhistory.SearchHistoryAspect;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public SearchHistoryAspect searchHistoryAspect() {
        return new SearchHistoryAspect();
    }
}
