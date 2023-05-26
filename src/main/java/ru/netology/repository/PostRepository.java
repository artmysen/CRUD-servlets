package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {
  AtomicLong countId;
  ConcurrentHashMap<Long, String> postsMap;
  public PostRepository() {
    postsMap = new ConcurrentHashMap<Long, String>();
    countId = new AtomicLong(1);
  }
  public ConcurrentHashMap all() {
    return postsMap;
  }
  public Optional<Post> getById(long id) {
    return Optional.of(new Post(id, postsMap.get(id)));
  }
  public Post save(Post post) {

    long valId = post.getId();

    if (valId != 0) {
      if (valId > countId.get() + 1) {
        valId = countId.get();
        countId.incrementAndGet();
      }
      postsMap.put(valId, post.getContent());
      return post;
    } else {
      postsMap.put(countId.get(), post.getContent());
      countId.incrementAndGet();
    }
    return post;
  }
  public void removeById(long id) {
    if (!postsMap.containsKey(id)) {
      return;
    }
    postsMap.remove(id);
  }
}