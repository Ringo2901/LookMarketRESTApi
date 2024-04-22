package by.bsuir.lookmanager;

import by.bsuir.lookmanager.utils.JwtValidator;
import com.cloudinary.Cloudinary;
import com.moesif.servlet.MoesifConfiguration;
import com.moesif.servlet.MoesifFilter;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;


@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfiguration {

    @Bean
    public Filter moesifFilter() {

        MoesifConfiguration config = new MoesifConfiguration() {
            @Override
            public String identifyUser(HttpServletRequest request, HttpServletResponse response) {
                String customerID = null;
                String secret = "8a7104032bd4c06dbb5e189f896e48b980de235592c16cb1176c694580bb98dc2257bc3ddb9c774e3e4d215d0d19ee5caba310190b198bb46431d35b612deb20ed640137d185b600daff2ccdd072ae2e2667139b10cffcde12c2854cb8c2e5d5aadd1a40588211735736705d97bdd3ecd655513a2efd330e9a8a773b167fba27";
                JwtValidator jwtValidator = new JwtValidator(secret);
                try {
                    String token = request.getHeader("Authorization");
                    customerID = String.valueOf(jwtValidator.validateTokenAndGetUserId(token));
                } catch (Exception e) {
                    System.out.print("Auth payload could not be parsed into JSON object");
                }
                return customerID;
            }
        };
        Dotenv dotenv = Dotenv.load();

        MoesifFilter moesifFilter = new MoesifFilter(dotenv.get("MOESIF_APPLICATION_ID"), config, false);

        moesifFilter.setLogBody(false);

        return moesifFilter;
    }
    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration)
                .build();
    }

}
