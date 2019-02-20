package net.uglevodov.restapi.repositories;

import net.uglevodov.restapi.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeedRepositoryImpl implements FeedRepository {

    @Autowired
    private RedisTemplate<String, Post> redisTemplate;

    @Override
    public Optional<List<Post>> findAllByUserId(Long userId) {
        List<Post> feed = redisTemplate.opsForList().range("feed_user"+userId, 0, redisTemplate.opsForList().size("feed_user"+userId));
        return Optional.of(feed);
    }

    @Override
    public Optional<List<Post>> addToFeedByUserId(Long userId, Post post) {
        redisTemplate.opsForList().leftPush("feed_user"+userId, post);
        List<Post> feed = redisTemplate.opsForList().range("feed_user"+userId, 0, redisTemplate.opsForList().size("feed_user"+userId));
        return Optional.of(feed);
    }

    @Override
    public void addToFeedByIds(List<Long> userIds, Post post) {
        List<Object> results = redisTemplate.executePipelined(
                new RedisCallback<Object>() {
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        for(Long userId : userIds) {
                            redisTemplate.opsForList().leftPush("feed_user"+userId, post);
                        }
                        return null;
                    }
                });
    }

    @Override
    public void deleteAllByPost(Post post) {

    }
}
